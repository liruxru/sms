package com.bonc.receive;

import java.net.ServerSocket;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;

/**
 * 短信接收客户端
 * 
 * @author j
 *
 */
@Slf4j
public class ReceiverClient implements Runnable {

	private int port = 8881; // 8882
	// 默认端口8881

	/**
	 * 默认端口8881
	 */
	public ReceiverClient() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 指定端口
	 * 
	 * @param port
	 */
	public ReceiverClient(int port) {
		super();
		this.port = port;
	}

	@Override
	public void run() {
		receiveMessage();
	}

	public void receiveMessage() {

		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			log.info("Start to waiting for client...");
			while (true) {
				// 阻塞,接收消息
				Socket socket = serverSocket.accept();
				log.info("Client socket connected at address:{} ", socket.toString());
				try {
					MessageReceiverTask task = new MessageReceiverTask(socket);
					Thread thread = new Thread(task);
					thread.start();
				} catch (Exception e) {
					log.error("MessageReceiver.receiveMessage.Exception: {}", e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
					serverSocket = null;
				} catch (Exception e) {
					log.error("Receiver.receiveMessage.Exception: {}", e.getMessage());
				}
			}
		}
	}

}
