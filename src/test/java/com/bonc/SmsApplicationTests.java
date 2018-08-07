package com.bonc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

}
