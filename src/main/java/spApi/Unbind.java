// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   Unbind.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command

public class Unbind extends SGIP_Command
{

    private static final int CommandLength = 0;
    private static final int CommandID = 2;
    private int flag;

    public Unbind(long NodeID)
    {
        super(NodeID, 0, 2);
        flag = 1;
    }

    public Unbind(SGIP_Command command)
    {
        super(command);
        flag = 1;
    }

    public int GetFlag()
    {
        return flag;
    }

    public Unbind()
    {
        super(0, 2);
        flag = 1;
    }
}
