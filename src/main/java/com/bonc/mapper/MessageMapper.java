package com.bonc.mapper;

import java.util.List;

import com.bonc.pojo.MessageTask;

public interface MessageMapper {

	public void insertReplyMessage(String userNumber, String spNumber, String messageContent);

	public void insertReportLog(String userNumber, Integer totalLength, Integer state, Integer errorCode,
			Integer reportType);

	public List<MessageTask> scanTaskMessage() ;

	public int countMessageTableTask();

}
