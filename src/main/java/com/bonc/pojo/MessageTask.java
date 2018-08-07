package com.bonc.pojo;

import java.util.List;

import lombok.Data;

/**
 * 任务实体   
 * @author j
 *
 */
@Data
public class MessageTask {
	private String saleId;
	private String monthId;
	private String dayId;
	private String dataNo;
	private int numberCount; // 目标用户数量
	private Integer threadNumber; //线程任务
	private String content;	// 短信内容
	private byte deviceType;//DEVICE_TYPE       NUMBER(6)     -----0为单点发送  1为任务一发送任务  
	private List<String> userNumbers; // 目标用户集合
}


