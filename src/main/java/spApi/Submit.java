// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name: Submit.java

package spApi;

import java.io.OutputStream;

// Referenced classes of package spApi:
// SGIP_Command, SGIP_Exception, Seq, MsgHead

public class Submit extends SGIP_Command
{
	long NodeID;
	String SPNumber;
	String ChargeNumber;
	int UserCount;
	String usernumber[];
	String CorpId;
	String ServiceType;
	int FeeType;
	String FeeValue;
	String GivenValue;
	int AgentFlag;
	int MOrelatetoMTFlag;
	int Priority;
	String ExpireTime;
	String ScheduleTime;
	int ReportFlag;
	int TP_pid;
	int TP_udhi;
	int MessageCoding;
	int MessageType;
	int MessageLength;
	String MessageContent;
	byte BinContent[];
	int ContentLength;

	public Submit(long NodeID)
	{
		super(NodeID);
		this.NodeID = 0L;
		ContentLength = 0;
		this.NodeID = NodeID;
	}

	public Submit(SGIP_Command command)
	{
		super(command);
		NodeID = 0L;
		ContentLength = 0;
	}

	public void setSPNumber(String SPNumber)
	{
		this.SPNumber = SPNumber;
	}

	public String getSPNumber()
	{
		return SPNumber;
	}

	public void setChargeNumber(String ChargeNumber)
	{
		this.ChargeNumber = ChargeNumber;
	}

	public String getChargeNumber()
	{
		return ChargeNumber;
	}

	public int setUserNumber(String dest_Mobile) throws SGIP_Exception
	{
		if (dest_Mobile.length() == 0)
			return 1;
		UserCount = 0;
		for (int i = 0; i < dest_Mobile.length(); i++)
		{
			char tempchar = dest_Mobile.charAt(i);
			if (tempchar != ',' && (tempchar < '0' || tempchar > '9'))
				throw new SGIP_Exception("Invalid Mobile Number");
			if (tempchar == ',')
				UserCount++;
		}

		UserCount++;
		usernumber = new String[UserCount];
		int from_point = 0;
		int cur_point = 0;
		for (int i = 0; i < UserCount - 1; i++)
		{
			from_point = cur_point;
			cur_point = dest_Mobile.indexOf(44, cur_point + 1);
			usernumber[i] = dest_Mobile.substring(from_point, cur_point);
			cur_point++;
		}

		usernumber[UserCount - 1] = dest_Mobile.substring(cur_point, dest_Mobile.length());
		return 0;
	}

	public String[] getUserNumber()
	{
		return usernumber;
	}

	public int getUserCount()
	{
		return UserCount;
	}

	public void setCorpId(String CorpId)
	{
		this.CorpId = CorpId;
	}

	public String getCorpId()
	{
		return CorpId;
	}

	public void setServiceType(String ServiceType)
	{
		this.ServiceType = ServiceType;
	}

	public String getServiceType()
	{
		return ServiceType;
	}

	public void setFeeType(int FeeType)
	{
		this.FeeType = FeeType;
	}

	public int getFeeType()
	{
		return FeeType;
	}

	public void setFeeValue(String FeeValue)
	{
		this.FeeValue = FeeValue;
	}

	public String getFeeValue()
	{
		return FeeValue;
	}

	public void setGivenValue(String GivenValue)
	{
		this.GivenValue = GivenValue;
	}

	public String getGivenValue()
	{
		return GivenValue;
	}

	public void setAgentFlag(int AgentFlag)
	{
		this.AgentFlag = AgentFlag;
	}

	public int getAgentFlag()
	{
		return AgentFlag;
	}

	public void setMOrelatetoMTFlag(int MOrelatetoMTFlag)
	{
		this.MOrelatetoMTFlag = MOrelatetoMTFlag;
	}

	public int getMOrelatetoMTFlag()
	{
		return MOrelatetoMTFlag;
	}

	public void setPriority(int Priority)
	{
		this.Priority = Priority;
	}

	public int getPriority()
	{
		return Priority;
	}

	public void setExpireTime(String ExpireTime)
	{
		this.ExpireTime = ExpireTime;
	}

	public String getExpireTime()
	{
		return ExpireTime;
	}

	public void setScheduleTime(String ScheduleTime)
	{
		this.ScheduleTime = ScheduleTime;
	}

	public String getScheduleTime()
	{
		return ScheduleTime;
	}

	public void setReportFlag(int ReportFlag)
	{
		this.ReportFlag = ReportFlag;
	}

	public int getReportFlag()
	{
		return ReportFlag;
	}

	public void setTP_pid(int TP_pid)
	{
		this.TP_pid = TP_pid;
	}

	public int getTP_pid()
	{
		return TP_pid;
	}

	public void setTP_udhi(int TP_udhi)
	{
		this.TP_udhi = TP_udhi;
	}

	public int getTP_udhi()
	{
		return TP_udhi;
	}

	public void setMessageType(int MessageType)
	{
		this.MessageType = MessageType;
	}

	public int getMessageType()
	{
		return MessageType;
	}

	public int setContent(int MessageCoding, String MessageContent) throws SGIP_Exception
	{
		if (MessageCoding == 4)
		{
			throw new SGIP_Exception("MessageCoding Error! Use setBinContent() to set Binary Message!");
		}
		else
		{
			this.MessageCoding = MessageCoding;
			this.MessageContent = MessageContent;
			return 0;
		}
	}

	public int setBinContent(int ContentLength, byte Content[])
	{
		MessageCoding = 4;
		this.ContentLength = ContentLength;
		BinContent = new byte[ContentLength];
		SGIP_Command.BytesCopy(Content, BinContent, 0, ContentLength - 1, 0);
		return 0;
	}

	public int getMessageCoding()
	{
		return MessageCoding;
	}

	public int getMessageLength()
	{
		return ContentLength;
	}

	public Submit(long NodeID, String SPNumber, String ChargeNumber, int UserCount, String UserNumber, String CorpId, String ServiceType, int FeeType, String FeeValue, String GivenValue,
			int AgentFlag, int MOrelatetoMTFlag, int Priority, String ExpireTime, String ScheduleTime, int ReportFlag, int TP_pid, int TP_udhi, int MessageCoding, int MessageType, int MessageLength,
			String MessageContent) throws SGIP_Exception
	{
		super(NodeID);
		this.NodeID = 0L;
		ContentLength = 0;
		this.NodeID = NodeID;
		this.SPNumber = SPNumber;
		this.ChargeNumber = ChargeNumber;
		this.UserCount = 0;
		for (int i = 0; i < UserNumber.length(); i++)
		{
			char tempchar = UserNumber.charAt(i);
			if (tempchar != ',' && (tempchar < '0' || tempchar > '9'))
				throw new SGIP_Exception("Invalid Mobile Number");
			if (tempchar == ',')
				this.UserCount++;
		}

		this.UserCount++;
		if (this.UserCount > 100)
			throw new SGIP_Exception("Too many users!");
		usernumber = new String[this.UserCount];
		int from_point = 0;
		int cur_point = 0;
		for (int i = 0; i < this.UserCount - 1; i++)
		{
			from_point = cur_point;
			cur_point = UserNumber.indexOf(44, cur_point + 1);
			usernumber[i] = UserNumber.substring(from_point, cur_point);
			cur_point++;
		}

		usernumber[this.UserCount - 1] = UserNumber.substring(cur_point, UserNumber.length());
		this.CorpId = CorpId;
		this.ServiceType = ServiceType;
		this.FeeType = FeeType;
		this.FeeValue = FeeValue;
		this.GivenValue = GivenValue;
		this.AgentFlag = AgentFlag;
		this.MOrelatetoMTFlag = MOrelatetoMTFlag;
		this.Priority = Priority;
		this.ExpireTime = ExpireTime;
		this.ScheduleTime = ScheduleTime;
		this.ReportFlag = ReportFlag;
		this.TP_pid = TP_pid;
		this.TP_udhi = TP_udhi;
		this.MessageCoding = MessageCoding;
		this.MessageType = MessageType;
		this.MessageContent = MessageContent;
	}

	public Submit(long NodeID, String SPNumber, String ChargeNumber, int UserCount, String UserNumber, String CorpId, String ServiceType, int FeeType, String FeeValue, String GivenValue,
			int AgentFlag, int MOrelatetoMTFlag, int Priority, String ExpireTime, String ScheduleTime, int ReportFlag, int TP_pid, int TP_udhi, int MessageCoding, int MessageType, int ContentLength,
			byte MessageContent[]) throws SGIP_Exception
	{
		super(NodeID);
		this.NodeID = 0L;
		this.ContentLength = 0;
		this.NodeID = NodeID;
		this.SPNumber = SPNumber;
		this.ChargeNumber = ChargeNumber;
		this.UserCount = 0;
		for (int i = 0; i < UserNumber.length(); i++)
		{
			char tempchar = UserNumber.charAt(i);
			if (tempchar != ',' && (tempchar < '0' || tempchar > '9'))
				throw new SGIP_Exception("Invalid Mobile Number");
			if (tempchar == ',')
				this.UserCount++;
		}

		this.UserCount++;
		if (this.UserCount > 100)
			throw new SGIP_Exception("Too many users!");
		usernumber = new String[this.UserCount];
		int from_point = 0;
		int cur_point = 0;
		for (int i = 0; i < this.UserCount - 1; i++)
		{
			from_point = cur_point;
			cur_point = UserNumber.indexOf(44, cur_point + 1);
			usernumber[i] = UserNumber.substring(from_point, cur_point);
			cur_point++;
		}

		usernumber[this.UserCount - 1] = UserNumber.substring(cur_point, UserNumber.length());
		this.CorpId = CorpId;
		this.ServiceType = ServiceType;
		this.FeeType = FeeType;
		this.FeeValue = FeeValue;
		this.GivenValue = GivenValue;
		this.AgentFlag = AgentFlag;
		this.MOrelatetoMTFlag = MOrelatetoMTFlag;
		this.Priority = Priority;
		this.ExpireTime = ExpireTime;
		this.ScheduleTime = ScheduleTime;
		this.ReportFlag = ReportFlag;
		this.TP_pid = TP_pid;
		this.TP_udhi = TP_udhi;
		this.MessageCoding = MessageCoding;
		this.MessageType = MessageType;
		this.ContentLength = ContentLength;
		BinContent = new byte[ContentLength];
		SGIP_Command.BytesCopy(MessageContent, BinContent, 0, this.ContentLength - 1, 0);
	}

	private void SetMsgBody() throws SGIP_Exception
	{
		byte temp_chn[] = null;
		try
		{
			if (MessageCoding == 15)
			{
				temp_chn = MessageContent.getBytes("GBK");
				ContentLength = temp_chn.length;
			}
			else
			{
				ContentLength = MessageContent.length();
			}
		}
		catch (Exception e)
		{
			System.out.println("chinese code error:" + e.toString());
		}
		TotalLength = 143 + 21 * UserCount + ContentLength;
		head.setTotalLength(TotalLength);
		head.setCommandID(3);
		head.setSeq_1(SGIP_Command.seq.getGlobalSeq_1());
		head.setSeq_2(SGIP_Command.seq.getGlobalSeq_2());
		head.setSeq_3(SGIP_Command.seq.getGlobalSeq_3());
		bodybytes = new byte[TotalLength - 20];
		byte tempbytes[] = new byte[21];
		if (SPNumber.length() > 21)
			throw new SGIP_Exception("SPNumber Longer than 21 bytes:" + SPNumber);
		SPNumber.getBytes(0, SPNumber.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 20, 0);
		tempbytes = new byte[21];
		if (ChargeNumber.length() > 21)
			throw new SGIP_Exception("ChargeNumber longer than 21 bytes:" + ChargeNumber);
		ChargeNumber.getBytes(0, ChargeNumber.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 20, 21);
		bodybytes[42] = (byte)UserCount;
		UserCount = UserCount;
		int cur_pos = 43;
		int i;
		for (i = 0; i < UserCount; i++)
		{
			tempbytes = new byte[21];
			String mobile = new String(usernumber[i]);
			if (mobile.length() > 21)
				throw new SGIP_Exception("UserNumber longer than 21 bytes:" + mobile);
			mobile.getBytes(0, mobile.length(), tempbytes, 0);
			SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 20, cur_pos);
			cur_pos += 21;
		}

		tempbytes = new byte[5];
		if (CorpId.length() > 5)
			throw new SGIP_Exception("CorpId longer than 5 bytes:" + CorpId);
		CorpId.getBytes(0, CorpId.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 4, cur_pos);
		cur_pos += 5;
		tempbytes = new byte[10];
		if (ServiceType.length() > 10)
			throw new SGIP_Exception("ServiceType longer than 10 bytes:" + ServiceType);
		ServiceType.getBytes(0, ServiceType.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 9, cur_pos);
		cur_pos += 10;
		bodybytes[cur_pos] = (byte)FeeType;
		cur_pos++;
		tempbytes = new byte[6];
		if (FeeValue.length() > 6)
			throw new SGIP_Exception("FeeValue longer than 6 bytes:" + FeeValue);
		FeeValue.getBytes(0, FeeValue.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 5, cur_pos);
		cur_pos += 6;
		tempbytes = new byte[6];
		if (GivenValue.length() > 6)
			throw new SGIP_Exception("GivenValue longer than 6 bytes:" + GivenValue);
		GivenValue.getBytes(0, GivenValue.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 5, cur_pos);
		cur_pos += 6;
		bodybytes[cur_pos] = (byte)AgentFlag;
		cur_pos++;
		bodybytes[cur_pos] = (byte)MOrelatetoMTFlag;
		cur_pos++;
		bodybytes[cur_pos] = (byte)Priority;
		cur_pos++;
		tempbytes = new byte[16];
		if (ExpireTime.length() > 16)
			throw new SGIP_Exception("EXpireTime longer than 16 bytes:" + ExpireTime);
		ExpireTime.getBytes(0, ExpireTime.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 15, cur_pos);
		cur_pos += 16;
		tempbytes = new byte[16];
		if (ScheduleTime.length() > 16)
			throw new SGIP_Exception("ScheduleTime longer than 16 bytes:" + ScheduleTime);
		ScheduleTime.getBytes(0, ScheduleTime.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 15, cur_pos);
		cur_pos += 16;
		bodybytes[cur_pos] = (byte)ReportFlag;
		cur_pos++;
		bodybytes[cur_pos] = (byte)TP_pid;
		cur_pos++;
		bodybytes[cur_pos] = (byte)TP_udhi;
		cur_pos++;
		bodybytes[cur_pos] = (byte)MessageCoding;
		cur_pos++;
		bodybytes[cur_pos] = (byte)MessageType;
		cur_pos++;
		SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(ContentLength), bodybytes, 0, 3, cur_pos);
		cur_pos += 4;
		i = 0;
		if (MessageCoding == 15)
			SGIP_Command.BytesCopy(temp_chn, bodybytes, 0, ContentLength - 1, cur_pos);
		else
			MessageContent.getBytes(0, ContentLength, bodybytes, cur_pos);
	}

	private void SetBinBody() throws SGIP_Exception
	{
		TotalLength = 143 + 21 * UserCount + ContentLength;
		if (SGIP_Command.seq == null)
		{
			SGIP_Command.seq = new Seq();
			SGIP_Command.seq.setNodeId(NodeID);
			Seq.computeSequence();
		}
		head.setTotalLength(TotalLength);
		head.setCommandID(3);
		head.setSeq_1(SGIP_Command.seq.getGlobalSeq_1());
		head.setSeq_2(SGIP_Command.seq.getGlobalSeq_2());
		head.setSeq_3(SGIP_Command.seq.getGlobalSeq_3());
		bodybytes = new byte[TotalLength - 20];
		byte tempbytes[] = new byte[21];
		if (SPNumber.length() > 21)
			throw new SGIP_Exception("SPNumber Longer than 21 bytes:" + SPNumber);
		SPNumber.getBytes(0, SPNumber.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 20, 0);
		tempbytes = new byte[21];
		if (ChargeNumber.length() > 21)
			throw new SGIP_Exception("ChargeNumber longer than 21 bytes:" + ChargeNumber);
		ChargeNumber.getBytes(0, ChargeNumber.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 20, 21);
		bodybytes[42] = (byte)UserCount;
		int cur_pos = 43;
		for (int i = 0; i < UserCount; i++)
		{
			tempbytes = new byte[21];
			String mobile = new String(usernumber[i]);
			if (mobile.length() > 21)
				throw new SGIP_Exception("UserNumber longer than 21 bytes:" + mobile);
			mobile.getBytes(0, mobile.length(), tempbytes, 0);
			SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 20, cur_pos);
			cur_pos += 21;
		}

		tempbytes = new byte[5];
		if (CorpId.length() > 5)
			throw new SGIP_Exception("CorpId longer than 5 bytes:" + CorpId);
		CorpId.getBytes(0, CorpId.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 4, cur_pos);
		cur_pos += 5;
		tempbytes = new byte[10];
		if (ServiceType.length() > 10)
			throw new SGIP_Exception("ServiceType longer than 10 bytes:" + ServiceType);
		ServiceType.getBytes(0, ServiceType.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 9, cur_pos);
		cur_pos += 10;
		bodybytes[cur_pos] = (byte)FeeType;
		cur_pos++;
		tempbytes = new byte[6];
		if (FeeValue.length() > 6)
			throw new SGIP_Exception("FeeValue longer than 6 bytes:" + FeeValue);
		FeeValue.getBytes(0, FeeValue.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 5, cur_pos);
		cur_pos += 6;
		tempbytes = new byte[6];
		if (GivenValue.length() > 6)
			throw new SGIP_Exception("GivenValue longer than 6 bytes:" + GivenValue);
		GivenValue.getBytes(0, GivenValue.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 5, cur_pos);
		cur_pos += 6;
		bodybytes[cur_pos] = (byte)AgentFlag;
		cur_pos++;
		bodybytes[cur_pos] = (byte)MOrelatetoMTFlag;
		cur_pos++;
		bodybytes[cur_pos] = (byte)Priority;
		cur_pos++;
		tempbytes = new byte[16];
		if (ExpireTime.length() > 16)
			throw new SGIP_Exception("EXpireTime longer than 16 bytes:" + ExpireTime);
		ExpireTime.getBytes(0, ExpireTime.length(), tempbytes, 0);
		SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 15, cur_pos);
		cur_pos += 16;
		tempbytes = new byte[16];
		if (ScheduleTime.length() > 16)
		{
			throw new SGIP_Exception("ScheduleTime longer than 16 bytes:" + ScheduleTime);
		}
		else
		{
			ScheduleTime.getBytes(0, ScheduleTime.length(), tempbytes, 0);
			SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 15, cur_pos);
			cur_pos += 16;
			bodybytes[cur_pos] = (byte)ReportFlag;
			cur_pos++;
			bodybytes[cur_pos] = (byte)TP_pid;
			cur_pos++;
			bodybytes[cur_pos] = (byte)TP_udhi;
			cur_pos++;
			bodybytes[cur_pos] = (byte)MessageCoding;
			cur_pos++;
			bodybytes[cur_pos] = (byte)MessageType;
			cur_pos++;
			SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(ContentLength), bodybytes, 0, 3, cur_pos);
			cur_pos += 4;
			SGIP_Command.BytesCopy(BinContent, bodybytes, 0, ContentLength - 1, cur_pos);
			return;
		}
	}

	@Override
	public int write(OutputStream out)
	{
		try
		{
			if (MessageCoding == 4)
				SetBinBody();
			else
				SetMsgBody();
			super.write(out);
			int i = 0;
			return i;
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		byte byte0 = -1;
		return byte0;
	}
}
