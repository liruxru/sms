package com.bonc.pool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class SendThreadPool {
	// 线程number所拥有的线程池及初始化数量 (threadNumber 键值保存为String)
	public static Map<String,ThreadPoolExecutor> threadPools = new ConcurrentHashMap<String,ThreadPoolExecutor>();

}
