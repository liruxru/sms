// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   UnbindResp.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command

public class UnbindResp extends SGIP_Command
{

    private static final int CommandLength = 0;
    private static final int CommandID = 0x80000002;
    private int flag;

    public UnbindResp(SGIP_Command command)
    {
        super(command);
        flag = 1;
    }

    public UnbindResp(long NodeID)
    {
        super(NodeID, 0, 0x80000002);
        flag = 1;
    }

    public UnbindResp()
    {
        super(0, 0x80000002);
        flag = 1;
    }

    public int GetFlag()
    {
        return flag;
    }
}
