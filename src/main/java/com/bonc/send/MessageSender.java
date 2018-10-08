package com.bonc.send;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.bonc.pojo.Configuration;
import com.bonc.pojo.Constant;
import com.bonc.pojo.MessageSenderConfiguration;
import com.bonc.pojo.MessageTask;
import com.bonc.pool.SendThreadPool;
import com.bonc.service.MessageService;
import com.bonc.util.SpringUtil;
import com.bonc.util.StringUtil;

import lombok.extern.slf4j.Slf4j;
import spApi.Bind;
import spApi.BindResp;
import spApi.SGIP_Command;
import spApi.SGIP_Exception;
import spApi.Submit;
import spApi.SubmitResp;
import spApi.Unbind;

/**
 * 短消息发送类，封装了短信发送方法
 **/
@Slf4j
public class MessageSender {
	public static final String DEFAULT_CP = "10016";
	// sp_api相关
	private Bind bind = null;
	private Unbind unBind = null;
	private SGIP_Command sgipCommand = new SGIP_Command();
	// 网管连接
	private Socket socket = null;
	// 推送至网关的连接输出流
	private OutputStream socketOutputStream = null;
	// 网管接收推送短信返回流
	private InputStream socketInputStream = null;


	private MessageSenderConfiguration senderConfiguration;
	private Configuration configuration;

	public MessageSender() {
		// 发送器其他配置
		this.configuration = (Configuration) SpringUtil.getBean("configuration");
	}

	public void setSenderConfiguration(MessageSenderConfiguration senderConfiguration) {
		// 发送器核心配置
		this.senderConfiguration = senderConfiguration;
	}

	/**
	 * 发送短信任务
	 * 
	 * @param messageTask
	 */
	public void send(MessageTask messageTask) {

		// 绑定
		if (!this.bind()) {
			this.unBind();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.bind();
		}

		// 消息内容
		String smsContent = messageTask.getContent();
		// 消息长度
		Integer smsLength = smsContent.length();
		switchLong(smsLength);

		// 目标用户集合
		List<String> userNumber = messageTask.getUserNumbers();

		// 逐条逐个用户发送 deviceNumber //目标用户号码
		for (int i = 0; i < userNumber.size(); i++) {
			if (null != userNumber.get(i)) {
				this.send(userNumber.get(i), smsContent);
			}
		}
		// 接触绑定
		this.unBind();
	}

	public boolean bind() {
		SocketAddress serverSocketAddress = new InetSocketAddress(senderConfiguration.getServerIp(), senderConfiguration.getServerPort());
		boolean isConnect = false;
		while (!isConnect) {
			try {
				socket = new Socket();
				socket.connect(serverSocketAddress, 5000);

				Thread.sleep(1000);
				isConnect = true;
				log.info("Connect server socket at address: " + socket.toString());
			} catch (Exception e) {
				e.printStackTrace();
				isConnect = false;
			}
		}

		try {
			socketOutputStream = new DataOutputStream(socket.getOutputStream());
			socketInputStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			log.error("获取管道流失败" + e.getMessage());
			return false;
		}

		bind = new Bind(Long.parseLong(senderConfiguration.getNodeId()), // nodeID 3+CP_ID
				1, // login type
				senderConfiguration.getLoginUser(), // login name
				senderConfiguration.getLoginPass()); // login password
		bind.write(socketOutputStream);
		log.debug("bind.......{},{}", senderConfiguration.getLoginUser(), senderConfiguration.getLoginPass());
		SGIP_Command temp = null;
		try {
			temp = sgipCommand.read(socketInputStream);
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}

		if (temp.getCommandID() == SGIP_Command.ID_SGIP_BIND_RESP) {
			BindResp bindResp = (BindResp) temp;
			bindResp.readbody();
		}
		log.debug("bind.......success");
		return true;
	}


	public void unBind() {
		try {
			unBind = new Unbind(Long.parseLong(senderConfiguration.getNodeId()));
			unBind.write(socketOutputStream); // 发送unbind

			socketOutputStream.close();
			socketInputStream.close();
			socket.close();
		} catch (Exception e) {
			log.error("[unBind] Exception: {}", e.getMessage());
		}
	}

	/**
	 * 逐条发送短信
	 * 
	 * @param userNumber 用户号码
	 * @param message    短信内容
	 * @return
	 */
	public boolean send(String userNumber, String message) {
		userNumber =  configuration.getPrefix() + userNumber;
		
		if (configuration.isLongMessage()) {
			// 长短信发送
			return sendSubsectionLong(userNumber, message, socketOutputStream);
		} else {
			// 短短信发送
			return sendSubsection(userNumber, message, socketOutputStream);
		}
	}


	/**
	 * 消息长度<70
	 * @param mobileNumber
	 * @param message
	 * @param fee
	 * @param out
	 * @return
	 */
	private boolean sendSubsection(String mobileNumber, String message, OutputStream out) {
		try {
			String subMessage = message;

			while (subMessage.length() > configuration.getGiMsgNum()) {
				String sTemp = subMessage.substring(0, configuration.getGiMsgNum());
				if (!sendMessage(mobileNumber, sTemp))
					return false;

				subMessage = subMessage.substring(configuration.getGiMsgNum());

				Thread.sleep(configuration.getSleepInter());
			}

			if (subMessage.length() > 0) {
				if (!sendMessage(mobileNumber, subMessage))
					return false;
			}

			log.info(mobileNumber.substring(configuration.getPrefix().length()) + "[Short]: " + message);
			return true;
		} catch (Exception e) {
			log.error("[sendSubsection] Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 长短信发送(消息长度>70)
	 * 
	 * @param mobileNumber
	 * @param message
	 * @param fee
	 * @param out
	 * @return
	 */
	@SuppressWarnings("static-access")
	private boolean sendSubsectionLong(String mobileNumber, String message,  OutputStream out) {
		try {
			Thread thread = new Thread();
			thread.start();

			int totalLength = message.length();
			int everyLength = configuration.getGiMsgNum() / 2;

			// 一条短信拆分为几个部分
			int part = (int) Math.ceil((double) totalLength / everyLength);

			for (int i = 0; i < part; i++) {
				int lastIndex = Math.min((i + 1) * everyLength, totalLength);

				String subString = message.substring(i * everyLength, lastIndex);
				byte[] messageUCS2 = subString.getBytes("iso-10646-ucs-2");
				int messageByteLength = messageUCS2.length;

				// 定义短信包头
				byte[] byteContent = new byte[messageByteLength + 6];
				byteContent[0] = 0x05;
				byteContent[1] = 0x00;
				byteContent[2] = 0x03;
				byteContent[3] = (byte) configuration.getSequenceNumber();
				byteContent[4] = (byte) part;
				byteContent[5] = (byte) (i + 1);

				// 数组拷贝
				System.arraycopy(messageUCS2, 0, byteContent, 6, messageByteLength);

				String content = new String(byteContent, "ISO8859-1");

				if (!sendMessage(mobileNumber, content))
					return false;

				thread.sleep(configuration.getSleepInter());
			}

			thread = null;
			return true;
		} catch (Exception e) {
			log.error("[sendSubsectionLong] Exception:  {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 具体调用发送短信
	 * 
	 * @param mobileNumber
	 * @param messageContent
	 * @param fee
	 * @return
	 */
	private boolean sendMessage(String mobileNumber, String messageContent) {
		// 对相同的接入号加锁 
		synchronized (senderConfiguration.getCpPhone()+"") {
			
			int sumThread = 0;
			long count =0;
			// 查询相同接入号的ThreadNumber
			MessageService messageService = (MessageService) SpringUtil.getBean("messageService");
			List<Integer> threadNumbers = messageService.selectAllThreadNumberByCpHone(senderConfiguration.getCpPhone());
			for (Integer integer : threadNumbers) {
				ThreadPoolExecutor threadPoolExecutor = SendThreadPool.threadPools.get(senderConfiguration.getThreadNumber()+"_thread");
				if(threadPoolExecutor!=null) {
					int activeCount = threadPoolExecutor.getActiveCount();
					sumThread+=activeCount;
					count+=Constant.countMessageNumbers[Integer.parseInt(senderConfiguration.getThreadNumber()+"")];
					log.debug("当前任务-->{}活动线程数为-->{}",senderConfiguration.getThreadNumber(),activeCount);
				}
			}
			
			long maxLock = senderConfiguration.getMaxMessage();
			if (count % maxLock == 0 
					&& (1000 - maxLock  * configuration.getSleepInter()) > 0) {
				try {
					Thread.sleep(1000 - (maxLock/sumThread) * (configuration.getSleepInter()));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Constant.countMessageNumbers[Integer.parseInt(senderConfiguration.getThreadNumber()+"")] 
					= ++Constant.countMessageNumbers[Integer.parseInt(senderConfiguration.getThreadNumber()+"")] % senderConfiguration.getMaxMessage();
		}

		if (StringUtil.isNullOrEmpty(configuration.getExpireTime())) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyMMddHHmmss032+");
			Calendar calendar = Calendar.getInstance();
			// 设置10分钟的短信过期时间
			// calendar.add(Calendar.MINUTE, 10);
			// 设置72小时的短信过期时间
			calendar.add(Calendar.DATE, 3);
			configuration.setExpireTime(dateFormatter.format(calendar.getTime()));

		}


		try {
			int userCount = 1;
			int messageLength = messageContent.length();

			Submit submit = new Submit(Long.parseLong(senderConfiguration.getNodeId()), // node id同上
					senderConfiguration.getCpPhone(), // cp_phone
					senderConfiguration.getChargeNumber(), // chargeNumber// 付费号码
					userCount, // 接收短消息的手机数
					mobileNumber, // 手机号码前面加86
					senderConfiguration.getCorpId(), // cp_id QYDM
					configuration.getServiceType(), // 业务代码
					configuration.getFeeType(), // 计费类型
					configuration.getFee(), // 短消息收费值
					configuration.getGivenFee(), // 赠送话费
					configuration.getAgentFlag(), // 代收标志
					configuration.getMot(), // 引起MT的原因
					configuration.getPriority(), // 优先级
					configuration.getExpireTime(), // 短消息终止时间
					configuration.getScheduleTime(), // 011125120000032+短消息定时发送时间
					configuration.getReportFlag(), // 状态报告标志
					configuration.getTp_pid(), // GSM协议类型
					configuration.getItp_udhi(), // GSM协议类型
					configuration.getMessageCode(), // 短消息编码格式
					configuration.getMessageType(), // 信息类型
					messageLength, // 短消息长度
					messageContent); // 短消息内容

			submit.write(socketOutputStream); // 发送submit

			SGIP_Command temp = sgipCommand.read(socketInputStream);

			SubmitResp submitresp = null;
			submitresp = (SubmitResp) temp;
			if (temp.getCommandID() == SGIP_Command.ID_SGIP_SUBMIT_RESP) {

				submitresp.readbody();
				log.info("网关接收，返回码： " + submitresp.getResult());
				if (submitresp.getResult() != 0) {
					log.error("号码：" + mobileNumber + "，网关接收失败，错误码： " + submitresp.getResult());
					return false;
				}

			}
			return true;
		} catch (SGIP_Exception e) {
			log.error("[sendMessage] SGIP_Exception: {}", e.getMessage());
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			log.error("[sendMessage] Exception:{}", e.getMessage());
			return false;
		}
	}

	
	
	/**
	 * 通过短信消息长度修改配置参数
	 * 
	 * @param smsLength 短信内容长度
	 */
	private void switchLong(Integer smsLength) {
		if (smsLength >= 70) {
			// 长短信
			configuration.setTp_pid(0);
			configuration.setItp_udhi(1);
			configuration.setMessageCode(8);
			configuration.setGiMsgNum(130);
			this.setLongMessage(true);
		} else {
			// 短短信
			configuration.setTp_pid(1);
			configuration.setItp_udhi(0);
			configuration.setMessageCode(15);
			configuration.setGiMsgNum(140);
			this.setLongMessage(false);
		}
	}

	public void setLongMessage(boolean isLongMessage) {
		configuration.setLongMessage(isLongMessage);
		if (senderConfiguration.getCpPhone().equals(DEFAULT_CP)) {
			this.configuration.setSleepInter(configuration.getDefaultSleepInter());
		} else {
			// 如果不是用默认的号码发送短信，则设置等待更长时间
			this.configuration.setSleepInter(
					isLongMessage ? configuration.getOtherLongSleepInter() : configuration.getOtherShortSleepInter());
		}
	}

}