package com.bonc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.mapper.MessageMapper;
import com.bonc.pojo.MessageSenderConfiguration;
import com.bonc.pojo.MessageTask;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class MessageService {
	@Autowired
	private MessageMapper messageMapper;
	
	public void insertReplyMessage(String userNumber, String spNumber,
			String messageContent) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 网关返回状态日志
	 * @param userNumber
	 * @param totalLength
	 * @param state
	 * @param errorCode
	 * @param reportType
	 */
	public void insertReportLog(String userNumber, Integer totalLength,
			Integer state, Integer errorCode, Integer reportType) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userNumber", userNumber);
		paramMap.put("totalLength", totalLength);
		paramMap.put("state", state);
		paramMap.put("errorCode", errorCode);
		paramMap.put("reportType", reportType);
		messageMapper.insertReportLog(paramMap);
		
	}
	/**
	 * 获取任务，修改状态为发送中状态 --> 10
	 * @return
	 */
	public List<MessageTask> scanMessageTask() {
		List<MessageTask> scanTaskMessage = messageMapper.scanTaskMessage();
		for (MessageTask messageTask : scanTaskMessage) {
			// 修改即将发送的任务状态为10
			messageMapper.updateTaskStatuBySaleIdAndStatus(messageTask.getSaleId(), 10);
		}
		return scanTaskMessage;
	}
	/**
	 * 更新任务状态 从 到1 从未发送 转换为发送成功
	 * @param saleId
	 */
	public void updateTaskStatuBySaleId(String saleId) {
		 messageMapper.updateTaskStatuBySaleId(saleId);
	}
	
	/**
	 * 插入任务历史记录
	 * @param messageTask
	 */
	public void insertTaskLog(MessageTask messageTask) {
		List<Map<String,String>> paramMaps = new ArrayList<>();
		List<String> userNumbers = messageTask.getUserNumbers();
		
		for (String deviceNumber : userNumbers) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("saleId", messageTask.getSaleId());
			paramMap.put("monthId", messageTask.getMonthId());
			paramMap.put("dayId", messageTask.getDayId());
			paramMap.put("dataNo", messageTask.getDataNo());
			paramMap.put("deviceType", messageTask.getDeviceType()+"");
			paramMap.put("deviceNumber",deviceNumber );
			paramMaps.add(paramMap);
		}
		try {
			messageMapper.insertTaskLogs(paramMaps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("插入任务日志报错-->{}",e);
		}
	}
	/**
	  * 获取管理员设置的任务调度时间
	 * @return
	 */
	public String getCron() {
		Map<String,String> times = messageMapper.getTimes();
		System.out.println(times);
		String startTime = times.get("STARTTIME").substring(0, 2);
		String endTime = times.get("ENDTIME").substring(0, 2);
		return "0 0/15 "+startTime+"-"+endTime+" * * ?";
	}
	
	
	public Map<String,String>  getTimesByThreadNum(Integer threadNumber) {
		return messageMapper.getTimesByThreadNum(threadNumber);
		
	}
	
	public void updateTaskStatuBySaleId(String saleId, int i) {
		messageMapper.updateTaskStatuBySaleIdAndStatus(saleId,i);
	}
	/**
	 * 通过threadNumber获取配置
	 * @param threadNumber
	 * @return
	 */
	public MessageSenderConfiguration getMessageSenderConfigurationByThreadNumber(Integer threadNumber) {
		return messageMapper.getMessageSenderConfigurationByThreadNumber(threadNumber);
	}

}
