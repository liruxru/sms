package com.bonc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.bonc.receive.ReceiverClient;
import com.bonc.send.SenderClient;

@SpringBootApplication
@MapperScan("com.bonc.mapper") //mybatis映射扫描
@EnableScheduling //定时任务开关
public class SmsApplication {

	public static void main(String[] args) {
		// 程序启动
		SpringApplication.run(SmsApplication.class, args);
		
		// 网关信息接收
		ReceiverClient receiverClient = new ReceiverClient();
		Thread receiver = new Thread(receiverClient);
		receiver.start();
		
		// 发送线程
		SenderClient senderClient = new SenderClient();
		Thread sender = new Thread(senderClient);
		sender.start();
	}
}
