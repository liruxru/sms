// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   Trace.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command, MsgHead

public class Trace extends SGIP_Command
{

    private static final int CommandLength = 41;
    private static final int CommandID = 4096;
    public int Seqno_1;
    public int Seqno_2;
    public int Seqno_3;
    public String UserNumber;
    public String Reserve;

    public Trace(MsgHead head)
    {
        super(head);
    }

    public Trace(long NodeID, int seqno_1, int seqno_2, int seqno_3, String usernumber)
    {
        super(NodeID, 41, 4096);
        Seqno_1 = seqno_1;
        Seqno_2 = seqno_2;
        Seqno_3 = seqno_3;
        UserNumber = usernumber;
        SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seqno_1), bodybytes, 0, 3, 0);
        SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seqno_2), bodybytes, 0, 3, 4);
        SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seqno_3), bodybytes, 0, 3, 8);
        UserNumber.getBytes(0, UserNumber.length(), bodybytes, 12);
    }

    public Trace(int seqno_1, int seqno_2, int seqno_3, String usernumber)
    {
        super(41, 4096);
        Seqno_1 = seqno_1;
        Seqno_2 = seqno_2;
        Seqno_3 = seqno_3;
        UserNumber = usernumber;
        SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seqno_1), bodybytes, 0, 3, 0);
        SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seqno_2), bodybytes, 0, 3, 4);
        SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seqno_3), bodybytes, 0, 3, 8);
        UserNumber.getBytes(0, UserNumber.length(), bodybytes, 12);
    }

    public void setSeqno_1(int seqno1)
    {
        Seqno_1 = seqno1;
        SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seqno_1), bodybytes, 0, 3, 0);
    }

    public int getSeqno1()
    {
        return Seqno_1;
    }

    public void setSeqno_2(int seqno2)
    {
        Seqno_2 = seqno2;
        SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seqno_2), bodybytes, 0, 3, 4);
    }

    @Override
	public int getSeqno_2()
    {
        return Seqno_2;
    }

    public void setSeqno_3(int seqno3)
    {
        Seqno_3 = seqno3;
        SGIP_Command.BytesCopy(SGIP_Command.IntToBytes4(Seqno_3), bodybytes, 0, 3, 8);
    }

    @Override
	public int getSeqno_3()
    {
        return Seqno_3;
    }

    public void setUserNumber(String UserNumber)
    {
        this.UserNumber = UserNumber;
        this.UserNumber.getBytes(0, this.UserNumber.length(), bodybytes, 12);
    }

    public String getUserNumber()
    {
        return UserNumber;
    }
}
