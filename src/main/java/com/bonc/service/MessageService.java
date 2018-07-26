package com.bonc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.pojo.MessageTask;

@Service
@Transactional
public class MessageService {

	public void insertReplyMessage(String userNumber, String spNumber,
			String messageContent) {
		// TODO Auto-generated method stub
		
	}
	// 插入状态日志
	public void insertReportLog(String userNumber, Integer totalLength,
			Integer state, Integer errorCode, Integer reportType) {
		// TODO Auto-generated method stub
		
	}
	public List<MessageTask> scanMessageTask() {
		return null;
		// TODO Auto-generated method stub
		
	}

}
