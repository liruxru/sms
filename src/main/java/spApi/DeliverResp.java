// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   DeliverResp.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command

public class DeliverResp extends SGIP_Command
{

    private int Result;
    private static final int CommandLength = 9;
    private static final int CommandID = 0x80000004;

    public DeliverResp(long NodeID, int Result)
    {
        super(NodeID, 9, 0x80000004);
        bodybytes[0] = SGIP_Command.IntToByte(Result);
    }

    public DeliverResp(SGIP_Command command)
    {
        super(command);
    }

    public void SetResult(int result)
    {
        Result = result;
        bodybytes[0] = SGIP_Command.IntToByte(Result);
    }
}
