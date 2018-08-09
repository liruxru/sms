package com.bonc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bonc.pojo.Configuration;
import com.bonc.pojo.MessageTask;
import com.bonc.service.MessageService;
import com.bonc.util.SpringUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.bonc.mapper") //mybatis映射扫描
public class SmsApplicationTests {
	@Autowired
	private Configuration configuration;
	@Test
	public void contextLoads() {
		System.out.println(configuration);
	}
	@Test
	public void test(){
		// 计算threadNum的个数
		Set<Integer> threadNum = new HashSet<Integer>();
		for (int i = 10; i >0; i--) {
			threadNum.add(i);
		}
		threadNum.size();
		
		List<Integer> threadNums = new ArrayList<Integer>();
		threadNums.addAll(threadNum);
		Collections.sort(threadNums);
		System.out.println(threadNums);
		
		List tempList = threadNums;
		tempList.remove(0);
		System.out.println(threadNums);
		
	}
	@Test
	public void name() {
		long a = Long.MAX_VALUE;
		System.out.println(a);
		System.out.println(a++);
		System.out.println(a++);
		System.out.println(Byte.MAX_VALUE);
		
	}
	@Test
	public void name1() {
		long aLong  =1000L;
		long bLong= 1000L;
		System.out.println(aLong==bLong);
		
	}
	@Autowired
	private MessageService messageService;
	@Test
	public void name2() {
		List<MessageTask> scanMessageTask = messageService.scanMessageTask();
		System.out.println(scanMessageTask);
	}
	
	@Test
	public void name3() {
//		 messageService.updateTaskStatuBySaleId("test");
		System.out.println(configuration);
	}
	/**
	 * 历史记录插入测试
	 */
	@Test
	public void name4() {
		try {
			List<MessageTask> scanMessageTask = messageService.scanMessageTask();
			System.out.println(scanMessageTask);
			for (MessageTask messageTask : scanMessageTask) {
				messageTask.getUserNumbers().add("18341893958");
				messageService.insertTaskLog(messageTask);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void name5() {
		String cron = messageService.getCron();
		System.out.println(cron);
	}
	@Test
	public void name6() throws ParseException {
		
		// 判断是否在发送时间内
    	MessageService messageService = (MessageService) SpringUtil.getBean("messageService");
    	Map<String, String> times = messageService.getTimesByThreadNum(1);
    	String startTime = times.get("STARTTIME");
		String endTime = times.get("ENDTIME");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		System.out.println(sdf.parse(startTime));
		System.out.println(sdf.parse(endTime));
		Date now = sdf.parse(sdf.format(new Date()));
		System.out.println(now);
		if(sdf.parse(startTime).before(now) && sdf.parse(endTime).after(now)) {
			System.out.println("ok");
		}
		
	}

}
