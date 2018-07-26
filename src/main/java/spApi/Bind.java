// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   Bind.java

package spApi;


// Referenced classes of package spApi:
//            SGIP_Command

public class Bind extends SGIP_Command
{

    private static final int CommandLength = 41;
    private static final int CommandID = 1;
    private int LoginType;
    private int flag;
    private String LoginName;
    private String LoginPassword;

    public Bind(long NodeID)
    {
        super(NodeID, 41, 1);
        flag = 1;
    }

    public Bind()
    {
        super(41, 1);
        flag = 1;
    }

    public Bind(SGIP_Command command)
    {
        super(command);
        flag = 1;
    }

    public int GetFlag()
    {
        return flag;
    }

    public int GetLoginType()
    {
        return LoginType;
    }

    public void SetLoginType(int logintype)
    {
        LoginType = logintype;
        bodybytes[0] = SGIP_Command.IntToByte(logintype);
    }

    public String GetLoginName()
    {
        return LoginName;
    }

    public void SetLoginName(String loginname)
    {
        LoginName = loginname;
        byte tempbytes[] = new byte[16];
        loginname.getBytes(0, loginname.length(), tempbytes, 0);
        SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 15, 1);
    }

    public String GetLoginPassword()
    {
        return LoginPassword;
    }

    public void SetLoginPassword(String password)
    {
        LoginPassword = password;
        byte tempbytes[] = new byte[16];
        password.getBytes(0, password.length(), tempbytes, 0);
        SGIP_Command.BytesCopy(tempbytes, bodybytes, 0, 15, 17);
    }

    public Bind(long NodeID, int LoginType, String LoginName, String LoginPassword)
    {
        super(NodeID, 41, 1);
        flag = 1;
        this.LoginType = LoginType;
        this.LoginName = LoginName;
        this.LoginPassword = LoginPassword;
        bodybytes[0] = SGIP_Command.IntToByte(LoginType);
        LoginName.getBytes(0, LoginName.length(), bodybytes, 1);
        LoginPassword.getBytes(0, LoginPassword.length(), bodybytes, 17);
    }

    public int readbody()
    {
        byte tempbytes[] = new byte[16];
        LoginType = bodybytes[0];
        SGIP_Command.BytesCopy(bodybytes, tempbytes, 1, 16, 0);
        LoginName = new String(tempbytes);
        SGIP_Command.BytesCopy(bodybytes, tempbytes, 17, 32, 0);
        LoginPassword = new String(tempbytes);
        flag = 0;
        return 0;
    }
}
