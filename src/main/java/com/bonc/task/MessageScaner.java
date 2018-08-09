package com.bonc.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bonc.pojo.Constant;
import com.bonc.pojo.MessageQueue;
import com.bonc.pojo.MessageTask;
import com.bonc.service.MessageService;
/**
 * 定时任务扫描
 * 扫描需要发送的短信任务
 * @author j
 *
 */
@Component
public class MessageScaner {
	@Autowired
	private MessageService messageService;
	
	
	/**
	 * 设定时间扫描任务准备发送 每15分钟扫描一次任务
	 */
	@Scheduled(cron="0 0/15 * * * ?")
	public void scanMessageTask(){
		synchronized (Constant.LOCK) {
			List<MessageTask> messages = messageService.scanMessageTask();
			MessageQueue.messages.addAll(messages);
			
			// 有任务通知发送
			if(MessageQueue.messages.size()>0)
			Constant.LOCK.notifyAll();
		}
	}

}
