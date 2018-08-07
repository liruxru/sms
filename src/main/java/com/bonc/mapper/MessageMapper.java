package com.bonc.mapper;

import java.util.List;
import java.util.Map;

import com.bonc.pojo.MessageTask;

public interface MessageMapper {

	public void insertReplyMessage(String userNumber, String spNumber, String messageContent);
	

	/**
	 * 查询全部需要发送的短信任务
	 * @return List<MessageTask>
	 */
	public List<MessageTask> scanTaskMessage() ;
	/**
	 * 发送结束更新任务状态
	 * @param saleId
	 */
	public void updateTaskStatuBySaleId(String saleId);
	/**
	 * 发送结束后日志记录
	 * @param paramMap
	 */
	public void insertTaskLog(Map<String, String> paramMap);
	/**
	 * 插入网关返回状态信息
	 * @param userNumber
	 * @param totalLength
	 * @param state
	 * @param errorCode
	 * @param reportType
	 */
	public void insertReportLog(Map<String, Object> paramMap);

}
