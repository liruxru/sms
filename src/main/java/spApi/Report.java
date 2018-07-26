// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   Report.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command, MsgHead

public class Report extends SGIP_Command
{

    String UserNumber;
    long Seq_1;
    int Seq_2;
    int Seq_3;
    int State;
    int ErrorCode;
    int ReportType;

    public Report(MsgHead head)
    {
        super(head);
    }

    public Report()
    {
    }

    public Report(SGIP_Command command)
    {
        super(command);
    }
    
    public Report(String UserNumber,long Seq_1,int Seq_2,int Seq_3){
    	
    }

    public int readbody()
    {
        byte tempbytes[] = new byte[4];
        SGIP_Command.BytesCopy(bodybytes, tempbytes, 0, 3, 0);
        Seq_1 = SGIP_Command.Bytes4ToLong(tempbytes);
        SGIP_Command.BytesCopy(bodybytes, tempbytes, 4, 7, 0);
        Seq_2 = SGIP_Command.Bytes4ToInt(tempbytes);
        SGIP_Command.BytesCopy(bodybytes, tempbytes, 8, 11, 0);
        Seq_3 = SGIP_Command.Bytes4ToInt(tempbytes);
        ReportType = bodybytes[12];
        UserNumber = new String(bodybytes, 13, 21);
        State = bodybytes[34];
        ErrorCode = bodybytes[35];
        return 0;
    }

    public String getUserNumber()
    {
        return UserNumber;
    }

    public int getReportType()
    {
        return ReportType;
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

    public int getState()
    {
        return State;
    }

    public int getErrorCode()
    {
        return ErrorCode;
    }
}
