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
import com.bonc.pojo.MessageTask;
import com.bonc.util.SpringUtil;
import com.bonc.util.StringUtil;

/**
 * 短消息发送类，封装了短信发送方法
 **/
@Slf4j
@Data
public class MessageSender
{	
	// 饿汉单例
	private static MessageSender messageSender=null;
	private static Configuration configuration = null;
	public static final String DEFAULT_CP = "10016";
	private MessageSender(){
		// 构造器私有化
	}
	private MessageSender(Configuration configuration) {
		serverIp = configuration.getServerIp();
		port = configuration.getPort();
		loginUser = configuration.getLoginUser();
		loginPasswd = configuration.getLoginPasswd();
		nodeId = configuration.getNodeId();
		cpPhone =configuration.getCpPhone();
		chargeNumber = configuration.getChargeNumber();
		corpId = configuration.getCorpId();
		serviceType = configuration.getServiceType();
		feeType = configuration.getFeeType();
		fee = configuration.getFee();
		givenFee = configuration.getGivenFee();
		agentFlag = configuration.getAgentFlag();
		mot = configuration.getMot();
		priority = configuration.getPriority();
		expireTime = configuration.getExpireTime();
		scheduleTime = configuration.getScheduleTime();
		reportFlag = configuration.getReportFlag();
		tp_pid = configuration.getTp_pid();
		itp_udhi = configuration.getItp_udhi();
		messageCode = configuration.getMessageCode();
		messageType = configuration.getMessageType();
		defaultSleepInter = configuration.getDefaultSleepInter();
		otherShortSleepInter = configuration.getOtherShortSleepInter();
		otherLongSleepInter = configuration.getOtherLongSleepInter();
	}
	/**
	 * 单例  获取短信发送对象
	 * @return
	 */
	public static MessageSender getInstance(){
		if(null ==messageSender){
			synchronized (MessageSender.class) {
				if(null ==messageSender){
					// 获取配置信息 从数据库查询
//					SpringUtil.getBean("");
					// 暂时通过配置文件直接读取配置
					configuration = (Configuration) SpringUtil.getBean("configuration");
					messageSender = new MessageSender(configuration);
					return messageSender;
				}
			}
		}
		return messageSender;
		
	}
	/**
	 * 多例 本程序采用多例
	 * @return
	 */
	public static MessageSender build(){
		configuration = (Configuration) SpringUtil.getBean("configuration");
		return new MessageSender(configuration);
	}
	
	
	


	

	private Bind bind = null;
	private Socket socket = null;
	private OutputStream socketOutputStream = null;
	private InputStream socketInputStream = null;
	private Unbind unBind = null;
	private SGIP_Command sgipCommand = new SGIP_Command();

	public boolean bind() {
		return bind(this.serverIp, this.port);
	}

	public boolean bind(String serverIp, int port) {
		SocketAddress serverSocketAddress = new InetSocketAddress(serverIp,
				port);
		boolean isConnect = false;
		while (!isConnect) {
			try {
				socket = new Socket();
				socket.connect(serverSocketAddress, 5000);
				
				Thread.sleep(1000);
				isConnect = true;
				log.info("Connect server socket at address: "
						+ socket.toString());
			} catch (Exception e) {
				e.printStackTrace();
				isConnect = false;
			}
		}

		try {
			socketOutputStream = new DataOutputStream(socket.getOutputStream());
			socketInputStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			log.error("获取管道流失败"+e.getMessage());
			return false;
		}

		bind = new Bind(Long.parseLong(nodeId), // nodeID 3+CP_ID
				1, // login type
				loginUser, // login name
				loginPasswd); // login password
		bind.write(socketOutputStream);
		log.debug("bind.......{},{}",loginUser,loginPasswd);
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
			unBind = new Unbind(Long.parseLong(nodeId));
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
	 * @param userNumber 用户号码
	 * @param message	短信内容
	 * @return
	 */
	public boolean send(String userNumber, String message){
		try{
			Thread.sleep(sleepInter);
		}catch (InterruptedException e){
			log.error(e.getMessage());
		}

		
		// boolean isSuccess = prepareSend(prefix + userNumber, message, fee, socketOutputStream);
		return prepareSend(prefix + userNumber, message, fee, socketOutputStream);
	}

	private  boolean prepareSend(String sMobileStr, String sMsg, String sFee, OutputStream outputStream){
		if (isLongMessage()){
			return sendSubsectionLong(sMobileStr, sMsg, sFee, outputStream);
		}else{
			return sendSubsection(sMobileStr, sMsg, sFee, outputStream);
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
	private boolean sendSubsection(String mobileNumber, String message,
			String fee, OutputStream out) {
		try {
			String subMessage = message;

			while (subMessage.length() > giMsgNum) {
				String sTemp = subMessage.substring(0, giMsgNum);
				if (!sendMessage(mobileNumber, sTemp, fee))
					return false;

				subMessage = subMessage.substring(giMsgNum);

				Thread.sleep(sleepInter);
			}

			if (subMessage.length() > 0) {
				if (!sendMessage(mobileNumber, subMessage, fee))
					return false;
			}

			log.info(mobileNumber.substring(prefix.length()) + "[Short]: "
					+ message);
			return true;
		} catch (Exception e) {
			log.error("[sendSubsection] Exception: {}", e.getMessage());
			return false;
		}
	}
	/**
	 * 长短信发送(消息长度>70)
	 * @param mobileNumber
	 * @param message
	 * @param fee
	 * @param out
	 * @return
	 */
	@SuppressWarnings("static-access")
	private boolean sendSubsectionLong(String mobileNumber, String message,
			String fee, OutputStream out) {
		try {
			Thread thread = new Thread();
			thread.start();

			int totalLength = message.length();
			int everyLength = giMsgNum / 2;

			// 一条短信拆分为几个部分
			int part = (int) Math.ceil((double) totalLength / everyLength);

			for (int i = 0; i < part; i++) {
				int lastIndex = Math.min((i + 1) * everyLength, totalLength);

				String subString = message
						.substring(i * everyLength, lastIndex);
				byte[] messageUCS2 = subString.getBytes("iso-10646-ucs-2");
				int messageByteLength = messageUCS2.length;

				// 定义短信包头
				byte[] byteContent = new byte[messageByteLength + 6];
				byteContent[0] = 0x05;
				byteContent[1] = 0x00;
				byteContent[2] = 0x03;
				byteContent[3] = (byte) sequenceNumber;
				byteContent[4] = (byte) part;
				byteContent[5] = (byte) (i + 1);

				// 数组拷贝
				System.arraycopy(messageUCS2, 0, byteContent, 6,
						messageByteLength);

				String content = new String(byteContent, "ISO8859-1");
		
				
				if (!sendMessage(mobileNumber, content, fee))
					return false;
				

				thread.sleep(sleepInter);
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
	 * @param mobileNumber
	 * @param messageContent
	 * @param fee
	 * @return
	 */
	private boolean sendMessage(String mobileNumber, String messageContent,
			String fee) {
		
		
			synchronized (Constant.SEND_LOCK) {
		      /*很多个线程发送时采用此方法*/
				/*if(Constant.countMessageNumber%20==0){
					try {
						// 线程休眠不释放锁,全部线程都在等待
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				*/
				
				/*单个线程采用此方法*/
				
				if(Constant.countMessageNumber%20==0&&(1000-20*sleepInter)>0) {
					try {
						Thread.sleep(1000-20*sleepInter);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				Constant.countMessageNumber = ++Constant.countMessageNumber%20;
			}
		
	
		
		if (StringUtil.isNullOrEmpty(expireTime)) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(
					"yyMMddHHmmss032+");
			Calendar calendar = Calendar.getInstance();
			// 设置10分钟的短信过期时间
			// calendar.add(Calendar.MINUTE, 10);
			// 设置72小时的短信过期时间
			calendar.add(Calendar.DATE, 3);

			expireTime = dateFormatter.format(calendar.getTime());

		}

		// System.out.println("------------expireTime--------:"+expireTime);

		try {
			int userCount = 1;
			int messageLength = messageContent.length();

			Submit submit = new Submit(Long.parseLong(nodeId), // node id同上
					cpPhone, // cp_phone
					// chargeNumber, // 付费号码
					mobileNumber, userCount, // 接收短消息的手机数
					mobileNumber, // 手机号码前面加86
					corpId, // cp_id QYDM
					serviceType, // 业务代码
					feeType, // 计费类型
					fee, // 短消息收费值
					givenFee, // 赠送话费
					agentFlag, // 代收标志
					mot, // 引起MT的原因
					priority, // 优先级
					expireTime, // 短消息终止时间
					scheduleTime, // 011125120000032+短消息定时发送时间
					reportFlag, // 状态报告标志
					tp_pid, // GSM协议类型
					itp_udhi, // GSM协议类型
					messageCode, // 短消息编码格式
					messageType, // 信息类型
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
					log.error("号码：" + mobileNumber + "，网关接收失败，错误码： "
							+ submitresp.getResult());
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


	
	private int giMsgNum = 130; // 一次能发送的短信字
	private final String prefix = "86"; // 前缀

	private String serverIp = "10.157.39.14"; // *******************192.10.10.8
	private int port = 8801; // *******************8801
	private String loginUser = "bcs_01"; // *******************sh10655739
	private String loginPasswd = "bcs_123"; // *******************1234

	private int sleepInter = 34; // 发送消息休眠时间
	private int defaultSleepInter = 2;
	private int otherShortSleepInter = 34;
	private int otherLongSleepInter = 100;

	private String nodeId = "3085173703"; // *******************3021021854
	private String cpPhone = "10016"; // *******************10655739
	private String chargeNumber = "-1"; // 付费号码 如果有sp付费填
	private String corpId = "73403"; // *******************21854
	private String serviceType = ""; // 业务代码
	private int feeType = 0; // 收费类型

	/**
	 * 0 "短消息类型"为"发送"，对"计费用户号码"不计信息费， 此类话单仅用于核减SP对称的信道费<br />
	 * 1 对"计费用户号码"免费<br />
	 * 2 对"计费用户号码"按条计信息费<br />
	 * 3 对"计费用户号码"按包月收取信息费<br />
	 * 4 对"计费用户号码"的收费是由SP实现<br />
	 * 5 予留计费类型，如按流量计费
	 */
	private String fee = "0"; // 信息费
	private String givenFee = "0"; // 赠送话费
	private int agentFlag = 1; // 代收标志 0：应收； 1：实收

	/**
	 * 0：MO点播引起的第一条MT消息；<br />
	 * 1：MO点播引起的非第一条MT消息；<br />
	 * 2：非MO点播引起的MT消息；<br />
	 * 3：系统反馈引起的MT消息或标志本条话单为包月费话单 （当为包月费话单时，用户计费类别必须为3、信息费为0；为系统反馈时不参加计费结算）。
	 */
	private int mot = 2; // 引起MT的原因 标志一条MT话单是否由MO点播引起

	private int priority = 0; // 优先级
	private String expireTime = ""; // 短消息终止时间，格式为“yymmddhhmmsstnnp”，“tnnp”取固定值“032+”，即默认系统为北京时间
	private String scheduleTime = ""; // 011125120000032+短消息定时发送时间
	private int reportFlag = 1; // 状态报告标志
	private int tp_pid = 0; // GSM协议类型
	private int itp_udhi = 0; // GSM协议类型1
	private int messageCode = 15; // 短消息编码格式
	private int messageType = 0; // 信息类型

	public int sequenceNumber = -1; // 信息序列号
	private boolean isLongMessage = false; // 是否长短信
	
	public void setLongMessage(boolean isLongMessage) {
		this.isLongMessage = isLongMessage;

		if (this.cpPhone.equals(DEFAULT_CP)) {
			this.sleepInter = defaultSleepInter;
		} else {
			// 如果不是用默认的号码发送短信，则设置等待更长时间
			this.sleepInter = (isLongMessage ? otherLongSleepInter
					: otherShortSleepInter);
		}
	}
	/**
	 * 发送短信任务
	 * @param messageTask
	 */
	public void send(MessageTask messageTask) {
			
		// 绑定
		if (!this.bind()) {
			this.unBind();
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.bind();
		}  
		
		
		// 消息内容
		String smsContent =messageTask.getContent();
		// 消息长度
		Integer smsLength = smsContent.length();
		switchLong(smsLength);
		
		// 目标用户集合
		List<String> userNumber = messageTask.getUserNumbers();
		
		
		// 设置发信人的电话号码 SP_NUMBER
		String spNumber = configuration.getSpNumber();
		this.setCpPhone(spNumber); // 设置发信人的电话号码
		
		// 逐条逐个用户发送    deviceNumber //目标用户号码
		for (int i = 0; i < userNumber.size(); i++) {
			if (null!=userNumber.get(i)) {
				this.send(userNumber.get(i), smsContent);
			}
			
		}
		
		
		this.unBind();
	}
	/**
	 * 通过短信消息长度修改参数
	 * @param smsLength 短信内容长度
	 */
	private void switchLong(Integer smsLength) {
		if (smsLength >= 70) {
			// 长短信
			this.setTp_pid(0);
			this.setItp_udhi(1);
			this.setMessageCode(8);
			this.setGiMsgNum(130);
			this.setLongMessage(true);
		}else {
			// 短短信
			this.setTp_pid(1);
			this.setItp_udhi(0);
			this.setMessageCode(15);
			this.setGiMsgNum(140);
			this.setLongMessage(false);
		}
	}
	
	

}