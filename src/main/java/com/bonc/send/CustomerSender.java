package com.bonc.send;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bonc.pojo.MessageSenderConfiguration;
import com.bonc.pojo.MessageTask;
import com.bonc.service.MessageService;
import com.bonc.util.SpringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerSender implements Runnable {
	private MessageService messageService = (MessageService) SpringUtil.getBean("messageService");
	private MessageSender messageSender;

	public CustomerSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	/**
	 * 待发送短信
	 */
	private MessageTask messageTask;

	/**
	 * 设置短信集合
	 * 
	 * @param messageTask
	 */

	public void setMessageTask(MessageTask messageTask) {
		this.messageTask = messageTask;
	}

	@Override
	public void run() {
		log.info("发送线程启动");
		// 发送
		try {
			// 满足发送时间就发送
			if (isRealTime(messageTask) && messageTask.getUserNumbers() != null
					&& messageTask.getUserNumbers().size() > 0) {
				// 获取配置
				init(messageTask);
				messageSender.send(messageTask);
				updateAndLog(messageTask);
			} else {
				// 修改状态为2
				updateStatus(messageTask);
			}
		} catch (Exception e) {
			log.error("发送任务异常{}", e.getMessage());
			e.printStackTrace();
		}
		log.info(" thread normal end! send " + messageTask.getNumberCount() + " .");

	}

	/**
	 * 初始化sender配置
	 * 
	 * @param messageTask
	 */
	private void init(MessageTask messageTask) {
		
		try {
			Integer threadNumber = messageTask.getThreadNumber();
			// 数据库查询获取该线程发送器核心配置
			MessageSenderConfiguration senderConfiguration = messageService
					.getMessageSenderConfigurationByThreadNumber(threadNumber);
			if (senderConfiguration != null) {
				messageSender.setSenderConfiguration(senderConfiguration);
				log.debug(" sender core config-->{}" ,senderConfiguration);
			}else {
				throw new RuntimeException("senderConfiguration get error ,please check database SMS_SENDER_CONFIG");
			}
				
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	private void updateAndLog(MessageTask messageTask) {
		// 修改状态
		messageService.updateTaskStatuBySaleId(messageTask.getSaleId());
		// 日志记录
		messageService.insertTaskLog(messageTask);
	}

	private void updateStatus(MessageTask messageTask) {
		// 修改状态
		messageService.updateTaskStatuBySaleId(messageTask.getSaleId(), 2);
	}

	/**
	 * 
	 * @param messageTask
	 * @return
	 * @throws Exception 解析时间异常，数据库时间为空异常
	 */
	private boolean isRealTime(MessageTask messageTask) throws ParseException {
		Integer threadNumber = messageTask.getThreadNumber();
		// 判断是否在发送时间内
		Map<String, String> times = messageService.getTimesByThreadNum(threadNumber);
		String startTime = times.get("STARTTIME");
		String endTime = times.get("ENDTIME");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		Date now = sdf.parse(sdf.format(new Date()));
		if (sdf.parse(startTime).before(now) && sdf.parse(endTime).after(now))
			return true;
		return false;
	}

}
