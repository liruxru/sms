package com.bonc.pojo;

import lombok.Data;
/**
 * 短信发送器配置
 * @author j
 *
 */
@Data
public class MessageSenderConfiguration {
	private String configId;
	private String serverIp;
	private Integer serverPort;
	private String loginUser;
	private String loginPass;
	private String nodeId;
	private String cpPhone;
	private String chargeNumber;
	private String corpId;
	private Long threadNumber;
	private Boolean status;
	private Long maxMessage;// 网关峰值
}
