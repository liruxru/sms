// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   ReportResp.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command

public class ReportResp extends SGIP_Command
{

    private static final int CommandLength = 9;
    private static final int CommandID = 0x80000005;
    private int Result;

    public ReportResp(long NodeID, int Result)
    {
        super(NodeID, 9, 0x80000005);
        this.Result = Result;
        bodybytes[0] = SGIP_Command.IntToByte(Result);
    }

    public void SetResult(int result)
    {
        Result = result;
        bodybytes[0] = SGIP_Command.IntToByte(result);
    }
}
