// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   BindResp.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command

public class BindResp extends SGIP_Command
{

    private static final int CommandLength = 9;
    private static final int CommandID = 0x80000001;
    private int Result;
    private int flag;

    public BindResp(long NodeID)
    {
        super(NodeID, 9, 0x80000001);
        flag = 1;
    }

    public BindResp(SGIP_Command command)
    {
        super(command);
        flag = 1;
    }

    public BindResp()
    {
        super(9, 0x80000001);
        flag = 1;
    }

    public int GetFlag()
    {
        return flag;
    }

    public BindResp(long NodeID, int Result)
    {
        super(NodeID, 9, 0x80000001);
        flag = 1;
        this.Result = Result;
        bodybytes[0] = SGIP_Command.IntToByte(Result);
    }

    public int readbody()
    {
        Result = bodybytes[0];
        flag = 0;
        return 0;
    }

    public int GetResult()
    {
        return Result;
    }

    public void SetResult(int result)
    {
        Result = result;
        bodybytes[0] = SGIP_Command.IntToByte(result);
    }
}
