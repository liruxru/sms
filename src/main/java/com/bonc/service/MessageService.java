package com.bonc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.mapper.MessageMapper;
import com.bonc.pojo.MessageTask;

@Service
@Transactional
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
	public List<MessageTask> scanMessageTask() {
		return messageMapper.scanTaskMessage();
	}
	public void updateTaskStatuBySaleId(String saleId) {
		 messageMapper.updateTaskStatuBySaleId(saleId);
	}
	public void insertTaskLog(MessageTask messageTask) {
		List<String> userNumbers = messageTask.getUserNumbers();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("saleId", messageTask.getSaleId());
		paramMap.put("monthId", messageTask.getMonthId());
		paramMap.put("dayId", messageTask.getDayId());
		paramMap.put("dataNo", messageTask.getDataNo());
		paramMap.put("deviceType", messageTask.getDeviceType()+"");
		for (String deviceNumber : userNumbers) {
			paramMap.put("deviceNumber",deviceNumber );
			messageMapper.insertTaskLog(paramMap);
		}
	}

}
