// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name: SGIP_Command.java

package spApi;

import java.io.*;

// Referenced classes of package spApi:
// MsgHead, Seq, SubmitResp, Deliver,
// Bind, BindResp, Unbind, UnbindResp,
// Report, TraceResp, Userrpt

public class SGIP_Command
{
	protected byte bodybytes[];
	protected MsgHead head;
	protected int TotalLength;
	protected static Seq seq = null;
	public static final int LEN_SGIP_HEADER = 20;
	public static final int LEN_SGIP_BIND = 41;
	public static final int LEN_SGIP_BIND_RESP = 9;
	public static final int LEN_SGIP_UNBIND = 0;
	public static final int LEN_SGIP_UNBIND_RESP = 0;
	public static final int LEN_SGIP_SUBMIT = 123;
	public static final int LEN_SGIP_SUBMIT_RESP = 9;
	public static final int LEN_SGIP_DELIVER = 57;
	public static final int LEN_SGIP_DELIVER_RESP = 9;
	public static final int LEN_SGIP_REPORT = 44;
	public static final int LEN_SGIP_REPORT_RESP = 9;
	public static final int LEN_SGIP_USERRPT = 51;
	public static final int LEN_SGIP_USERRPT_RESP = 9;
	public static final int LEN_SGIP_TRACE = 41;
	public static final int LEN_SGIP_TRACE_RESP = 48;
	public static final int LEN_MAX_CONTENT = 160;
	public static final int TIMEOUT = 60000;
	public static final int SUBMIT_OK = 0;
	public static final int SUBMIT_ERROR_STRUCTURE = 1;
	public static final int SUBMIT_ERROR_COMMANDTYPE = 2;
	public static final int SUBMIT_ERROR_SEQ_DUPLICATE = 3;
	public static final int SUBMIT_ERROR_MSG_LENGTH = 4;
	public static final int SUBMIT_ERROR_FEECODE = 5;
	public static final int SUBMIT_ERROR_CONTENT_LENGTH_EXCEED = 6;
	public static final int SUBMIT_ERROR_SERVERID = 7;
	public static final int SUBMIT_ERROR_FLOW_CONTROL = 8;
	public static final int SUBMIT_ERROR_OTHERS = 9;
	public static final int ID_SGIP_BIND = 1;
	public static final int ID_SGIP_BIND_RESP = 0x80000001;
	public static final int ID_SGIP_UNBIND = 2;
	public static final int ID_SGIP_UNBIND_RESP = 0x80000002;
	public static final int ID_SGIP_SUBMIT = 3;
	public static final int ID_SGIP_SUBMIT_RESP = 0x80000003;
	public static final int ID_SGIP_DELIVER = 4;
	public static final int ID_SGIP_DELIVER_RESP = 0x80000004;
	public static final int ID_SGIP_REPORT = 5;
	public static final int ID_SGIP_REPORT_RESP = 0x80000005;
	public static final int ID_SGIP_USERRPT = 17;
	public static final int ID_SGIP_USERRPT_RESP = 0x80000011;
	public static final int ID_SGIP_TRACE = 4096;
	public static final int ID_SGIP_TRACE_RESP = 0x80001000;
	public static final int MSG_TYPE_ASCII = 0;
	public static final int MSG_TYPE_WRITECARD = 3;
	public static final int MSG_TYPE_BINARY = 4;
	public static final int MSG_TYPE_UCS2 = 8;
	public static final int MSG_TYPE_CHINESE = 15;
	public static final int READ_SGIP_INVALID = 0;
	public static final int READ_SGIP_DELIVER = 1;
	public static final int READ_SGIP_REPORT = 2;
	public static final int READ_SGIP_USERRPT = 3;
	public static final int READ_SGIP_UNBIND = 4;

	public SGIP_Command()
	{
		TotalLength = 0;
		head = new MsgHead();
		if (seq == null)
			seq = new Seq();
	}

	public SGIP_Command(long NodeID)
	{
		TotalLength = 0;
		head = new MsgHead();
		if (seq == null)
			seq = new Seq();
		seq.setNodeId(NodeID);
	}

	public SGIP_Command(MsgHead head)
	{
		TotalLength = 0;
		this.head = head;
		if (seq == null)
			seq = new Seq();
	}

	public SGIP_Command(SGIP_Command command)
	{
		TotalLength = 0;
		head = command.head;
		bodybytes = command.bodybytes;
		TotalLength = command.TotalLength;
	}

	public SGIP_Command(long NodeID, int MinSeq, int MaxSeq, int CommandLength, int CommandID)
	{
		TotalLength = 0;
		if (seq == null)
			seq = new Seq(MinSeq, MaxSeq);
		seq.setNodeId(NodeID);
		TotalLength = 20 + CommandLength;
		head = new MsgHead();
		head.setTotalLength(TotalLength);
		head.setCommandID(CommandID);
		head.setSeq_1(seq.getGlobalSeq_1());
		head.setSeq_2(seq.getGlobalSeq_2());
		head.setSeq_3(seq.getGlobalSeq_3());
		bodybytes = new byte[CommandLength];
	}

	public SGIP_Command(long NodeID, int CommandLength, int CommandID)
	{
		TotalLength = 0;
		if (seq == null)
			seq = new Seq();
		seq.setNodeId(NodeID);
		TotalLength = 20 + CommandLength;
		head = new MsgHead();
		head.setTotalLength(TotalLength);
		head.setCommandID(CommandID);
		head.setSeq_1(seq.getGlobalSeq_1());
		head.setSeq_2(seq.getGlobalSeq_2());
		head.setSeq_3(seq.getGlobalSeq_3());
		bodybytes = new byte[CommandLength];
	}

	public SGIP_Command(int CommandLength, int CommandID)
	{
		TotalLength = 0;
		if (seq == null)
			seq = new Seq();
		TotalLength = 20 + CommandLength;
		head = new MsgHead();
		head.setTotalLength(TotalLength);
		head.setCommandID(CommandID);
		head.setSeq_1(seq.getGlobalSeq_1());
		head.setSeq_2(seq.getGlobalSeq_2());
		head.setSeq_3(seq.getGlobalSeq_3());
		bodybytes = new byte[CommandLength];
	}

	public int write(OutputStream out)
	{
		try
		{
			byte Head_Body[] = new byte[TotalLength];
			synchronized (Seq.seqclass)
			{
				Seq.computeSequence();
				head.setSeq_2(seq.getGlobalSeq_2());
				head.setSeq_3(seq.getGlobalSeq_3());
			}

			BytesCopy(head.Head, Head_Body, 0, 19, 0);
			BytesCopy(bodybytes, Head_Body, 0, TotalLength - 20 - 1, 20);
			out.write(Head_Body);

			return 0;
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}

		return -1;
	}

	public SGIP_Command read(InputStream in) throws Exception, IOException
	{
		head.read(in);
		readdataintobody(in);
		switch (head.getCommandID())
		{
			case -2147483645:
				return new SubmitResp(this);

			case 4: // '\004'
				return new Deliver(this);

			case 1: // '\001'
				return new Bind(this);

			case -2147483647:
				return new BindResp(this);

			case 2: // '\002'
				return new Unbind(this);

			case -2147483646:
				return new UnbindResp(this);

			case 5: // '\005'
				return new Report(this);

			case -2147479552:
				return new TraceResp(this);

			case 17: // '\021'
				return new Userrpt(this);
		}
		return null;
	}

	private void readdataintobody(InputStream in) throws Exception, IOException
	{
		TotalLength = head.getTotalLength();

		if (TotalLength > 1024 * 100)
			return;

		bodybytes = new byte[Math.max(TotalLength - 20, 4)];
		in.read(bodybytes);
	}

	public int getTotalLength()
	{
		return head.getTotalLength();
	}

	public long getSeqno_1()
	{
		return head.getSeq_1();
	}

	public int getSeqno_2()
	{
		return head.getSeq_2();
	}

	public int getSeqno_3()
	{
		return head.getSeq_3();
	}

	public void setSeqno_1(long Seq_id)
	{
		head.setSeq_1(Seq_id);
	}

	public int getCommandID()
	{
		return head.getCommandID();
	}

	protected static int ByteToInt(byte mybyte)
	{
		return mybyte;
	}

	protected static byte IntToByte(int i)
	{
		return (byte)i;
	}

	protected static int BytesToInt(byte mybytes[])
	{
		return ((0xff & mybytes[0]) << 8) + mybytes[1];
	}

	protected static byte[] IntToBytes(int i)
	{
		byte mybytes[] = new byte[2];
		mybytes[1] = (byte)(0xff & i);
		mybytes[0] = (byte)((0xff00 & i) >> 8);
		return mybytes;
	}

	protected static byte[] IntToBytes4(int i)
	{
		byte mybytes[] = new byte[4];
		mybytes[3] = (byte)(0xff & i);
		mybytes[2] = (byte)((0xff00 & i) >> 8);
		mybytes[1] = (byte)((0xff0000 & i) >> 16);
		mybytes[0] = (byte)((0xff000000 & i) >> 24);
		return mybytes;
	}

	protected static byte[] LongToBytes4(long i)
	{
		byte mybytes[] = new byte[4];
		mybytes[3] = (byte)(int)(255 & i);
		mybytes[2] = (byte)(int)((65280 & i) >> 8);
		mybytes[1] = (byte)(int)((0xff0000 & i) >> 16);
		mybytes[0] = (byte)(int)((0xff000000 & i) >> 24);
		return mybytes;
	}

	protected static void LongToBytes4(long i, byte mybytes[])
	{
		mybytes[3] = (byte)(int)(255 & i);
		mybytes[2] = (byte)(int)((65280 & i) >> 8);
		mybytes[1] = (byte)(int)((0xff0000 & i) >> 16);
		mybytes[0] = (byte)(int)((0xff000000 & i) >> 24);
	}

	protected static void IntToBytes(int i, byte mybytes[])
	{
		mybytes[1] = (byte)(0xff & i);
		mybytes[0] = (byte)((0xff00 & i) >> 8);
	}

	protected static void IntToBytes4(int i, byte mybytes[])
	{
		mybytes[3] = (byte)(0xff & i);
		mybytes[2] = (byte)((0xff00 & i) >> 8);
		mybytes[1] = (byte)((0xff0000 & i) >> 16);
		mybytes[0] = (byte)(int)(((long)0xff000000 & (long)i) >> 24);
	}

	protected static int Bytes4ToInt(byte mybytes[])
	{
		return (0xff & mybytes[0]) << 24 | (0xff & mybytes[1]) << 16 | (0xff & mybytes[2]) << 8 | 0xff & mybytes[3];
	}

	protected static long Bytes4ToLong(byte mybytes[])
	{
		return ((long)255 & (long)mybytes[0]) << 24 | ((long)255 & (long)mybytes[1]) << 16 | ((long)255 & (long)mybytes[2]) << 8 | (long)255 & (long)mybytes[3];
	}

	protected static void BytesCopy(byte source[], byte dest[], int sourcebegin, int sourceend, int destbegin)
	{
		int j = 0;
		for (int i = sourcebegin; i <= sourceend; i++)
		{
			dest[destbegin + j] = source[i];
			j++;
		}

	}

}
