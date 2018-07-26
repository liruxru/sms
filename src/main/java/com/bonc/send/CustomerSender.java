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

/**
 * 消息发送(消费者)
 * @author j
 *
 */
@Slf4j
public class CustomerSender extends Thread{

	@Override
	public void run() {
		try {
			synchronized (Constant.LOCK) {
				
				if(MessageQueue.messages.size()==0){
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
				    	if(messageTask.getNumberCount()>0
				    			&&messageTask.getUserNumber()!=null
				    			&&messageTask.getUserNumber().size()>0)
				    	smsSubList.add(messageTask);
					}
				    // 移除被分发的集合，加快下次的遍历速度
				    messages.remove(smsSubList);
				    if (smsSubList.isEmpty()) break;
				    
				    MessageSender messageSender = MessageSender.build();
				    Sender sender = new Sender(messageSender);
				    
				    
				  
				    sender.setSmsList(smsSubList); // 短信集合
				    
				    // 添加到线程集合
				    threads.add(new Thread(sender));
				} 
				
				// 启动全部线程
				for (Thread t : threads) {
				    t.start(); // 启动短信发送线程
				}
				
				// 线程join,结束任务后插入日志
				
				
		    }
		} catch (InterruptedException e) {
			log.error("sender error exception {}",e.getMessage());
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
