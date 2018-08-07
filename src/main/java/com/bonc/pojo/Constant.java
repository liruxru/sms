package com.bonc.pojo;

public class Constant {
	public static final String LOCK = "lock" ;
	public static final String SEND_LOCK = "sendLock" ;
	/**
	 * 短信发送计数器
	 */
	public static volatile long countMessageNumber = 0L;

}
