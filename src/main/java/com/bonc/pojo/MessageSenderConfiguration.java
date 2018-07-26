package com.bonc.pojo;

import lombok.Data;
/**
 * 短信发送器配置
 * @author j
 *
 */
@Data
public class MessageSenderConfiguration {
	public static final String DEFAULT_CP = "10016";

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
	
	private int ErrorCode = 0;
	private int ReportType = 0;
	private int state = 0;
}
