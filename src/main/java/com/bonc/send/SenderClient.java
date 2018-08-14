package com.bonc.send;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.extern.slf4j.Slf4j;

import com.bonc.pojo.MessageTask;
import com.bonc.pool.SendThreadPool;
import com.bonc.service.MessageService;
import com.bonc.util.SpringUtil;

/**
 * 消息发送客户端
 * 
 * @author j
 *
 */
@Slf4j
public class SenderClient extends Thread {
	private List<MessageTask> messagesTask = null;

	public SenderClient(List<MessageTask> messagesTask) {
		super();
		this.messagesTask = messagesTask;
	}

	@Override
	public void run() {
		try {
			sendRun();
		} catch (InterruptedException e) {
			log.error("sender error exception {}", e.getMessage());
		}
	}

	private void sendRun() throws InterruptedException {
		// 遍历任务，分发线程
		for (MessageTask messageTask : messagesTask) {
			MessageSender messageSender = new MessageSender();
			CustomerSender sender = new CustomerSender(messageSender);
			// 短信集合
			sender.setMessageTask(messageTask);
			// 获取线程number 查找线程池 ，及其配置
			Integer threadNumber = messageTask.getThreadNumber();
			// 获取线程执行器
			ThreadPoolExecutor threadPoolExecutor = getThreadExecutor(threadNumber);
			// 启动线程
			threadPoolExecutor.execute(sender);
		}
	}

	private ThreadPoolExecutor getThreadExecutor(Integer threadNumber) {
		MessageService messageService = (MessageService) SpringUtil.getBean("messageService");
		ThreadPoolExecutor threadPoolExecutor = SendThreadPool.threadPools.get(threadNumber+"_thread");
		if(null != threadPoolExecutor)return threadPoolExecutor;
		synchronized (SendThreadPool.class) {
			threadPoolExecutor = SendThreadPool.threadPools.get(threadNumber+"_thread");
			if(null != threadPoolExecutor)return threadPoolExecutor;
			if(null==threadPoolExecutor) {
				log.info("获取线程池为空，从数据库读取配置--->新建池");
				// 初始化 , 通过threadNum 数据库查询threadNumber
				int nThreads= messageService.getThreadPoolSizeByThreadNumber(threadNumber);
				threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
				SendThreadPool.threadPools.put(threadNumber+"_thread", threadPoolExecutor);
			}
			return threadPoolExecutor;
		}
		
	}

}
