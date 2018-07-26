// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   SubmitResp.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command

public class SubmitResp extends SGIP_Command
{

    private int Result;

    public SubmitResp()
    {
    }

    public SubmitResp(SGIP_Command command)
    {
        super(command);
    }

    public int getResult()
    {
        return Result;
    }

    public int readbody()
    {
        Result = bodybytes[0];
        return 0;
    }
}
