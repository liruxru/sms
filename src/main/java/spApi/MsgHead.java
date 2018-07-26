// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name: MsgHead.java

package spApi;

import java.io.*;

// Referenced classes of package spApi:
// SGIP_Command

public class MsgHead
{

	public byte Head[];
	private int TotalLength;
	private int CommandID;
	private long Seq_1;
	private int Seq_2;
	private int Seq_3;
	private byte tempbytes[];

	public MsgHead(byte InBytes[])
	{
		int i = 0;
		Head = new byte[20];
		for (i = 0; i < 20; i++)
			Head[i] = InBytes[i];

		TotalLength = SGIP_Command.Bytes4ToInt(Head);
		tempbytes = (new byte[] { Head[4], Head[5], Head[6], Head[7] });
		CommandID = SGIP_Command.Bytes4ToInt(tempbytes);
		tempbytes = (new byte[] { Head[8], Head[9], Head[10], Head[11] });
		Seq_1 = SGIP_Command.Bytes4ToLong(tempbytes);
		tempbytes = (new byte[] { Head[12], Head[13], Head[14], Head[15] });
		Seq_2 = SGIP_Command.Bytes4ToInt(tempbytes);
		tempbytes = (new byte[] { Head[16], Head[17], Head[18], Head[19] });
		Seq_3 = SGIP_Command.Bytes4ToInt(tempbytes);
	}

	public MsgHead()
	{
		Head = new byte[20];
	}

	public MsgHead(int TotalLength, int CommandID, long Seq_1, int Seq_2, int Seq_3)
	{
		Head = new byte[20];
		this.TotalLength = TotalLength;
		this.CommandID = CommandID;
		this.Seq_1 = Seq_1;
		this.Seq_2 = Seq_2;
		this.Seq_3 = Seq_3;
		SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(TotalLength), Head, 0, 3, 0);
		SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(CommandID), Head, 0, 3, 4);
		SGIP_Command.BytesCopy(SGIP_Command.LongToBytes4(Seq_1), Head, 0, 3, 8);
		SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seq_2), Head, 0, 3, 12);
		SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seq_3), Head, 0, 3, 16);
	}

	public void setTotalLength(int TotalLength)
	{
		this.TotalLength = TotalLength;
		SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(TotalLength), Head, 0, 3, 0);
	}

	public void setCommandID(int CommandID)
	{
		this.CommandID = CommandID;
		SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(CommandID), Head, 0, 3, 4);
	}

	public void setSeq_1(long Seq_1)
	{
		this.Seq_1 = Seq_1;
		SGIP_Command.BytesCopy(SGIP_Command.LongToBytes4(Seq_1), Head, 0, 3, 8);
	}

	public void setSeq_2(int Seq_2)
	{
		this.Seq_2 = Seq_2;
		SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seq_2), Head, 0, 3, 12);
	}

	public void setSeq_3(int Seq_3)
	{
		this.Seq_3 = Seq_3;
		SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seq_3), Head, 0, 3, 16);
	}

	public int getTotalLength()
	{
		return TotalLength;
	}

	public int getCommandID()
	{
		return CommandID;
	}

	public long getSeq_1()
	{
		return Seq_1;
	}

	public int getSeq_2()
	{
		return Seq_2;
	}

	public int getSeq_3()
	{
		return Seq_3;
	}

	public int write(OutputStream out)
	{
		try
		{
			out.write(Head);
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
			byte byte0 = -1;
			return byte0;
		}
		return 0;
	}

	public int read(InputStream in)
	{
		byte tempbytes[] = new byte[4];
		try
		{
			in.read(tempbytes);
			SGIP_Command.BytesCopy(tempbytes, Head, 0, 3, 0);
			TotalLength = SGIP_Command.Bytes4ToInt(tempbytes);
			in.read(tempbytes);
			SGIP_Command.BytesCopy(tempbytes, Head, 0, 3, 4);
			CommandID = SGIP_Command.Bytes4ToInt(tempbytes);
			in.read(tempbytes);
			SGIP_Command.BytesCopy(tempbytes, Head, 0, 3, 8);
			Seq_1 = SGIP_Command.Bytes4ToLong(tempbytes);
			in.read(tempbytes);
			SGIP_Command.BytesCopy(tempbytes, Head, 0, 3, 12);
			Seq_2 = SGIP_Command.Bytes4ToInt(tempbytes);
			in.read(tempbytes);
			SGIP_Command.BytesCopy(tempbytes, Head, 0, 3, 16);
			Seq_3 = SGIP_Command.Bytes4ToInt(tempbytes);
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
			byte byte0 = -1;
			return byte0;
		}
		return 0;
	}
}
