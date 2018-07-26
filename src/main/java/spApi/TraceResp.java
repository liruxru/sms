// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   TraceResp.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command

public class TraceResp extends SGIP_Command
{

    public int Count;
    public int Result;
    public String NodeId;
    public String ReceiveTime;
    public String SendTime;
    public String Reserve;

    public TraceResp()
    {
    }

    public TraceResp(SGIP_Command command)
    {
        super(command);
    }

    public int getCount()
    {
        return Count;
    }

    public int getResult()
    {
        return Result;
    }

    public String getNodeId()
    {
        return NodeId;
    }

    public String getReceiveTime()
    {
        return ReceiveTime;
    }

    public String SendTime()
    {
        return SendTime;
    }

    public int readbody()
    {
        Count = bodybytes[0];
        Result = bodybytes[1];
        NodeId = new String(bodybytes, 2, 6);
        ReceiveTime = new String(bodybytes, 8, 16);
        SendTime = new String(bodybytes, 24, 16);
        return 0;
    }
}
