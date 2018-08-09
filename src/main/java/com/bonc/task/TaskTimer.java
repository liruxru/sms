/*package com.bonc.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.bonc.pojo.Constant;
import com.bonc.pojo.MessageQueue;
import com.bonc.pojo.MessageTask;
import com.bonc.service.MessageService;

import lombok.extern.slf4j.Slf4j;
/**
 * 第一种：动态修改任务调度
 * 第二种：任务调度不变，发送时验证是否属于发送时间
 */
/*
@Slf4j
@Component
public class TaskTimer implements SchedulingConfigurer  {
	@Autowired
	private MessageService messageService;
	
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            //定时任务的执行逻辑
            @Override
            public void run() {
                log.info("时间调度开始执行------");
                try {
                	
                	synchronized (Constant.LOCK) {
            			List<MessageTask> messages = messageService.scanMessageTask();
            			MessageQueue.messages.addAll(messages);
            			
            			// 有任务通知发送
            			if(MessageQueue.messages.size()>0)
            			Constant.LOCK.notifyAll();
            		}
                    

                } catch (Exception e) {
                    log.error("出错信息-->{}",e.getMessage());
                }


            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 定时任务触发，可修改定时任务的执行周期
                // 这里的corn从数据库去查
                String cron="0 0/15 8-22 * * ?";
                log.info("默认cron表达式为每天8点到22点每15分钟扫描一次任务-->{}",cron);
                try {
                    cron = messageService.getCron();
                    log.info("下次执行任务的corn表达式为{}",cron);
                } catch (Exception e) {
                    log.error("获取cron表达式出错-->{}",e.getMessage());
                    throw new RuntimeException(e);
                }

                CronTrigger trigger = new CronTrigger(cron);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });
    }
}
*/