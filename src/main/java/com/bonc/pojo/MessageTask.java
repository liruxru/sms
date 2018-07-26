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
	private long numberCount; // 目标用户数量
	private Integer threadNumber; //线程任务
	private String content;	// 短信内容
	private List<String> userNumber; // 目标用户集合
}


