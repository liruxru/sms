package com.bonc.receive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import spApi.Bind;
import spApi.BindResp;
import spApi.Deliver;
import spApi.DeliverResp;
import spApi.Report;
import spApi.ReportResp;
import spApi.SGIP_Command;
import spApi.UnbindResp;
import spApi.Userrpt;
import spApi.UserrptResp;

import com.bonc.service.MessageService;
import com.bonc.util.SpringUtil;
import com.bonc.util.StringUtil;

@Slf4j
public class MessageReceiverTask implements Runnable {
	@Autowired
	private MessageService messageService = SpringUtil.getBean(MessageService.class);
	private Socket socket;

	public MessageReceiverTask(Socket socket) {
		this.socket = socket;
	}

	// 沉淀用户回复短信

	@Override
	public void run() {
		try {
			receiveMessage();
		} catch (SocketException e) {
			log.error("MessageReceiverTask.run.SocketException: {}",
					e.getMessage());
		} catch (Exception e) {
			log.error("MessageReceiverTask.run.Exception:{} ", e.getMessage());
		} finally {
			if (this.socket != null) {
				try {
					this.socket.close();
					this.socket = null;
				} catch (Exception e) {
				}
			}
		}
	}

	private void receiveMessage() throws SocketException {
		SGIP_Command sgipCommand = new SGIP_Command();

		InputStream inputStream = null;
		OutputStream outputStream = null;

		SGIP_Command sgipTemp = null;

		Deliver deliver = null;
		DeliverResp deliverResp = null;

		Report report = null;
		ReportResp reportResp = null;

		Userrpt userrpt = null;
		UserrptResp userrptResp = null;

		Bind bind = null;
		BindResp bindResp = null;

		try {
			inputStream = this.socket.getInputStream();
			outputStream = this.socket.getOutputStream();
		} catch (IOException e) {
			log.error("MessageReceiverTask.receiveMessage.IOException");
			return;
		}

		boolean loop = true;
		boolean insertLog = true;
		while (loop) {
			try {
				sgipTemp = sgipCommand.read(inputStream);
			} catch (Exception e) {
				log.error("MessageReceiverTask.receiveMessage[sgipCommand.read].Exception: {} ", e.getMessage());
				return;
			}

			switch (sgipCommand.getCommandID()) {
			case SGIP_Command.ID_SGIP_DELIVER: {
				deliver = (Deliver) sgipTemp; // 强制转换
				deliver.readbody(); // 解包

				String userNumber = deliver.getUserNumber().replaceAll("^[+]*86", ""); // 用户号码
				userNumber = StringUtil.trimString(userNumber);
				String spNumber = StringUtil.trimString(deliver.getSPNumber()); // 服务商号码，10018
				int tp_pid = deliver.getTP_pid(); // GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9
				int tp_udhi = deliver.getTP_udhi(); // GSM协议类型。详细解释请参考GSM03.40中的9.2.3.23

				int messageCoding = deliver.getMessageCoding();

				String messageContent = deliver.getMessageContent();

				if (messageCoding == 8) {
					try {
						messageContent = new String(deliver.getMessageByte(), "iso-10646-ucs-2");
					} catch (UnsupportedEncodingException e) {
						messageContent = deliver.getMessageContent();
					}
				} else if (messageCoding == 15) {
					try {
						messageContent = new String(deliver.getMessageByte(), "GBK");
					} catch (UnsupportedEncodingException e) {
						messageContent = deliver.getMessageContent();
					}
				}

				deliverResp = new DeliverResp(399000, // node id 3＋CP_id
						0); // result
				deliverResp.SetResult(0);
				deliverResp.write(outputStream);

				if (messageContent == null || messageContent.length() == 0)
					break;

				if (messageContent.replaceAll("\\s", "").length() == 0)
					break;

				messageService.insertReplyMessage(userNumber, spNumber, messageContent);
				break;
			}
			case SGIP_Command.ID_SGIP_BIND: {
				bind = (Bind) sgipTemp; // 强制转换
				bind.readbody(); // 解包

				bindResp = new BindResp(399000, // node id 3＋CP_id
						0); // result
				bindResp.write(outputStream);
				break;
			}
			case SGIP_Command.ID_SGIP_UNBIND: {
				UnbindResp unBindResp = new UnbindResp(399000); // result
				unBindResp.write(outputStream);

				System.out.println("SGIP_Command.ID_SGIP_UNBIND");

				break;
			}
			case SGIP_Command.ID_SGIP_REPORT: {
					report = (Report) sgipTemp; // 强制转换
					report.readbody(); // 解包

					reportResp = new ReportResp(390999, // node id 3＋CP_id
							0); // result
					reportResp.SetResult(0);
					reportResp.write(outputStream);

					String userNumber = StringUtil.trimString(report.getUserNumber()).replaceAll("^[+]*86", "");
					Integer totalLength = report.getTotalLength();
					Integer state = report.getState();
					Integer errorCode = report.getErrorCode();
					Integer reportType = report.getReportType();
					
					messageService.insertReportLog(userNumber, totalLength, state, errorCode, reportType);
				break;
			}
			case SGIP_Command.ID_SGIP_USERRPT: {
				userrpt = (Userrpt) sgipTemp; // 强制转换
				userrpt.readbody(); // 解包

				System.out.println("SP Number: " + userrpt.getSPNumber());
				System.out.println("User Number: " + userrpt.getUserNumber());
				System.out.println("User Condition: " + userrpt.getUserCondition());

				userrptResp = new UserrptResp(390999, 0);
				userrptResp.SetResult(12);
				userrptResp.write(outputStream);
				loop = false;
				break;
			}

			default:
				break;
			}
		}

		if (outputStream != null) {
			try {
				outputStream.close();
				outputStream = null;
			} catch (IOException e) {
			}
		}

		if (inputStream != null) {
			try {
				inputStream.close();
				inputStream = null;
			} catch (IOException e) {
			}
		}
	}
}