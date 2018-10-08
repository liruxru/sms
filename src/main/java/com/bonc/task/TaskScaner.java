package com.bonc.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bonc.pojo.Constant;
import com.bonc.pojo.MessageTask;
import com.bonc.send.SenderClient;
import com.bonc.service.MessageService;

import lombok.extern.slf4j.Slf4j;
/**
 * 定时任务扫描
 * 扫描需要发送的短信任务
 * @author j
 *
 */
@Component
@Slf4j
public class TaskScaner {
	@Autowired
	private MessageService messageService;
	
	
	/**
	 * 设定时间扫描任务准备发送 每10s扫描一次任务
	 */
	@Scheduled(cron="0/10 * * * * ?")
	public void scanMessageTask(){
		List<MessageTask> messages = messageService.scanMessageTask();
		// 判断系统是否开启
		boolean bln = messageService.getSystemOnOff();
		if(bln) {
			SenderClient senderClient = new SenderClient(messages);
			senderClient.start();
		}else {
			for (MessageTask messageTask : messages) {
				// 修改状态为3 表明系统关闭
				messageService.updateTaskStatuBySaleId(messageTask.getSaleId(), 3);
				log.info("发送程序处于关闭状态！！！！！！！OFF,如想打开，修改数据库配置SMS_SYSTEM_SWITCH_ON_OFF");
			}
			
		}
	
	}

}
