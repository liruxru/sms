// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   UserrptResp.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command

public class UserrptResp extends SGIP_Command
{

    private static final int CommandLength = 9;
    private static final int CommandID = 0x80000011;
    int Result;

    public UserrptResp(long NodeID, int Result)
    {
        super(NodeID, 9, 0x80000011);
        bodybytes[0] = SGIP_Command.IntToByte(Result);
        this.Result = Result;
    }

    public UserrptResp(int result)
    {
        super(9, 0x80000011);
        bodybytes[0] = SGIP_Command.IntToByte(result);
        Result = result;
    }

    public void SetResult(int result)
    {
        bodybytes[0] = SGIP_Command.IntToByte(result);
        Result = result;
    }
}
