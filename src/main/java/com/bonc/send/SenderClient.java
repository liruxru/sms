package com.bonc.send;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import com.bonc.pojo.Constant;
import com.bonc.pojo.MessageQueue;
import com.bonc.pojo.MessageTask;
import com.bonc.service.MessageService;
import com.bonc.util.SpringUtil;

/**
 * 消息发送客户端
 * @author j
 *
 */
@Slf4j
public class SenderClient extends Thread{

	@Override
	public void run() {
		try {
			while (true) {
				sendRun();
			}
		} catch (InterruptedException e) {
			log.error("sender error exception {}",e.getMessage());
		}
	}

	
	private void sendRun() throws InterruptedException {
		synchronized (Constant.LOCK) {
			
			if(MessageQueue.messages.size()==0){
				log.debug("任务集合没有任务，发送线程释放锁等待");
				Constant.LOCK.wait();
			}
			
			
			// 发送线程,分发任务  根据threadNum分发给线程
			List<MessageTask> messages = MessageQueue.messages;
			
			// 计算threadNum的个数
			/**
			 * 如何更好的拆分到各个线程？？？？才有益于程序效率(均匀？？)
			 */
			List<Integer> sortThreadTask = sortThreadTask(messages);
			
			// 将短信分发到各个线程
			List<Thread> threads = new ArrayList<Thread>(sortThreadTask.size());
			
			for (int i = 0; i < sortThreadTask.size(); i++) {
			    List<MessageTask> smsSubList = new ArrayList<MessageTask>();
			    for (MessageTask messageTask : messages) {
			    	// 满足条件加入集合
			    	Integer threadNumber = messageTask.getThreadNumber();
			    	
					if(threadNumber.equals(sortThreadTask.get(i)) 
			    			&&messageTask.getNumberCount()>0
			    			&&messageTask.getUserNumbers()!=null
			    			&&messageTask.getUserNumbers().size()>0) {
			    		
			    		smsSubList.add(messageTask);
			    	}
			    		
			    	
				}
			    // 移除被分发的集合，加快下次的遍历速度
			    messages.remove(smsSubList);
			    if (smsSubList.isEmpty()) break;
			    
			    MessageSender messageSender = MessageSender.build();
			    CustomerSender sender = new CustomerSender(messageSender);
			    // 短信集合
			    sender.setSmsList(smsSubList); 
			    
			    // 添加到线程集合
			    threads.add(new Thread(sender));
			} 
			
			MessageQueue.messages.clear();
			
			// 启动全部线程
			for (Thread t : threads) {
			    t.start(); // 启动短信发送线程
			}
			
			
			// 线程join,结束任务后插入日志
			for (Thread t : threads) {
			    t.join(); 
			}
			
			
			Constant.LOCK.notifyAll();
		
	    }
		
	}



	/**
	 * 获取全部任务的线程number 并排序
	 * @param messages  -- List<MessageTask>
	 * @return
	 */
	private List<Integer> sortThreadTask(List<MessageTask> messages) {
		Set<Integer> threadNum = new HashSet<Integer>();
		for (MessageTask messageTask : messages) {
			threadNum.add(messageTask.getThreadNumber());
		}
		threadNum.size();
		List<Integer> threadNums = new ArrayList<Integer>();
		threadNums.addAll(threadNum);
		Collections.sort(threadNums);
		return threadNums;
	}
	

}
