package com.bonc.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bonc.pojo.MessageSenderConfiguration;
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
	/**
	 *  批量插入网关返回状态信息
	 * @param paramMaps
	 */

	public void insertTaskLogs(List<Map<String, String>> paramMaps);

	/**
	  * 查询起始时间和终止时间
	 * @return
	 */
	public Map<String, String> getTimes();


	public Map<String, String> getTimesByThreadNum(Integer threadNumber);


	public void updateTaskStatuBySaleIdAndStatus(String saleId, int i);


	public MessageSenderConfiguration getMessageSenderConfigurationByThreadNumber(Integer threadNumber);


	public int getThreadPoolSizeByThreadNumber(Integer threadNumber);


	public List<Integer> selectAllThreadNumberByCpHone(String cpPhone);


	public boolean getSystemOnOff();


	public Date selectReportMaxDate(String userNumber);

}
