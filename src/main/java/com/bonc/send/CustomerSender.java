package com.bonc.send;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.bonc.pojo.MessageTask;

@Slf4j
public class CustomerSender implements Runnable{
	
	MessageSender messageSender ;
	public CustomerSender(MessageSender messageSender) {
		this.messageSender=messageSender;
	}


	/**
	 * 待发送短信
	 */
	private List<MessageTask> smsList;
	/**
	 * 设置短信集合
	 * @param smsList
	 */
	public void setSmsList(List<MessageTask> smsList) {
		this.smsList = smsList;
	}

	@Override
	public void run() {
		log.info(" thread normal start......");
		// 发送
		for (MessageTask messageTask : smsList) {
			try {
				messageSender.send(messageTask);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info(" thread normal end! sned " + smsList.size() + " .");
		
	}


}
