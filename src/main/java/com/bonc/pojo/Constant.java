package com.bonc.pojo;

public class Constant {
	public static final String SEND_LOCK = "sendLock" ;
	/**
	 * 短信发送计数器
	 */
	// 最多支持的线程数目   1-9
	public static volatile  long countMessageNumbers[] = {0,0,0,0,0,0,0,0,0,0};

}
