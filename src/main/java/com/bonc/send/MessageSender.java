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
import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import spApi.Bind;
import spApi.BindResp;
import spApi.SGIP_Command;
import spApi.SGIP_Exception;
import spApi.Submit;
import spApi.SubmitResp;
import spApi.Unbind;

import com.bonc.pojo.Configuration;
import com.bonc.pojo.Constant;
import com.bonc.pojo.MessageSenderConfiguration;
import com.bonc.pojo.MessageTask;
import com.bonc.util.SpringUtil;
import com.bonc.util.StringUtil;

/**
 * 短消息发送类，封装了短信发送方法
 **/
@Slf4j
public class MessageSender {
	public static final String DEFAULT_CP = "10016";
	private Bind bind = null;
	private Socket socket = null;
	private OutputStream socketOutputStream = null;
	private InputStream socketInputStream = null;
	private Unbind unBind = null;
	private SGIP_Command sgipCommand = new SGIP_Command();

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
		this.unBind();
	}

	public boolean bind() {
		return bind(senderConfiguration.getServerIp(), senderConfiguration.getServerPort());
	}

	public boolean bind(String serverIp, int port) {
		SocketAddress serverSocketAddress = new InetSocketAddress(serverIp, port);
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
		try {
			Thread.sleep(configuration.getSleepInter());
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}

		// boolean isSuccess = prepareSend(prefix + userNumber, message, fee,
		// socketOutputStream);
		return prepareSend(configuration.getPrefix() + userNumber, message, configuration.getFee(), socketOutputStream);
	}

	private boolean prepareSend(String sMobileStr, String sMsg, String sFee, OutputStream outputStream) {
		if (configuration.isLongMessage()) {
			return sendSubsectionLong(sMobileStr, sMsg, sFee, outputStream);
		} else {
			return sendSubsection(sMobileStr, sMsg, sFee, outputStream);
		}
	}

	/**
	 * 消息长度<70
	 * 
	 * @param mobileNumber
	 * @param message
	 * @param fee
	 * @param out
	 * @return
	 */
	private boolean sendSubsection(String mobileNumber, String message, String fee, OutputStream out) {
		try {
			String subMessage = message;

			while (subMessage.length() > configuration.getGiMsgNum()) {
				String sTemp = subMessage.substring(0, configuration.getGiMsgNum());
				if (!sendMessage(mobileNumber, sTemp, fee))
					return false;

				subMessage = subMessage.substring(configuration.getGiMsgNum());

				Thread.sleep(configuration.getSleepInter());
			}

			if (subMessage.length() > 0) {
				if (!sendMessage(mobileNumber, subMessage, fee))
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
	private boolean sendSubsectionLong(String mobileNumber, String message, String fee, OutputStream out) {
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

				if (!sendMessage(mobileNumber, content, fee))
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
	private boolean sendMessage(String mobileNumber, String messageContent, String fee) {

		synchronized (Constant.SEND_LOCK) {
			/* 很多个线程发送时采用此方法 */
			/*
			 * if(Constant.countMessageNumber%20==0){ try { // 线程休眠不释放锁,全部线程都在等待
			 * Thread.sleep(1000); } catch (InterruptedException e) { // TODO Auto-generated
			 * catch block e.printStackTrace(); } }
			 */

			/* 单个线程采用此方法 */

			if (Constant.countMessageNumber % 20 == 0 && (1000 - 20 * configuration.getSleepInter()) > 0) {
				try {
					Thread.sleep(1000 - 20 * (configuration.getSleepInter()));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Constant.countMessageNumber = ++Constant.countMessageNumber % 20;
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

		// System.out.println("------------expireTime--------:"+expireTime);

		try {
			int userCount = 1;
			int messageLength = messageContent.length();

			Submit submit = new Submit(Long.parseLong(senderConfiguration.getNodeId()), // node id同上
					senderConfiguration.getCpPhone(), // cp_phone
					// chargeNumber, // 付费号码
					senderConfiguration.getChargeNumber(), userCount, // 接收短消息的手机数
					mobileNumber, // 手机号码前面加86
					senderConfiguration.getCorpId(), // cp_id QYDM
					configuration.getServiceType(), // 业务代码
					configuration.getFeeType(), // 计费类型
					fee, // 短消息收费值
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
	 * 通过短信消息长度修改参数
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