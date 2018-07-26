// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   Userrpt.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command, MsgHead

public class Userrpt extends SGIP_Command
{

    String SpNumber;
    String UserNumber;
    int UserCondition;

    public Userrpt(MsgHead head)
    {
        super(head);
    }

    public Userrpt(SGIP_Command command)
    {
        super(command);
    }

    public Userrpt()
    {
    }

    public int readbody()
    {
        SpNumber = new String(bodybytes, 0, 21);
        UserNumber = new String(bodybytes, 21, 21);
        UserCondition = bodybytes[42];
        return 0;
    }

    public String getSPNumber()
    {
        return SpNumber;
    }

    public String getUserNumber()
    {
        return UserNumber;
    }

    public int getUserCondition()
    {
        return UserCondition;
    }
}
