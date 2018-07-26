// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   Deliver.java

package spApi;

// Referenced classes of package spApi:
//            SGIP_Command, MsgHead

public class Deliver extends SGIP_Command
{

    String UserNumber;
    String SpNumber;
    int TP_pid;
    int TP_udhi;
    int MessageCoding;
    int MessageLength;
    byte MessageByte[];
    String MessageContent;

    public Deliver(MsgHead Head)
    {
        super(Head);
    }

    public Deliver(SGIP_Command command)
    {
        super(command);
    }

    public Deliver()
    {
    }

    public int readbody()
    {
        byte tempbytes[] = new byte[4];
        UserNumber = new String(bodybytes, 0, 21);
        SpNumber = new String(bodybytes, 21, 21);
        TP_pid = bodybytes[42];
        TP_udhi = bodybytes[43];
        MessageCoding = bodybytes[44];
        SGIP_Command.BytesCopy(bodybytes, tempbytes, 45, 48, 0);
        MessageLength = SGIP_Command.Bytes4ToInt(tempbytes);
        if(MessageLength > 160)
        {
            System.out.println("Message Length is too long!  " + MessageLength);
            MessageLength = 160;
        }
        MessageByte = new byte[MessageLength];
        SGIP_Command.BytesCopy(bodybytes, MessageByte, 49, (49 + MessageLength) - 1, 0);
        MessageContent = new String(MessageByte);
        return 0;
    }

    public String getUserNumber()
    {
        return UserNumber;
    }

    public String getSPNumber()
    {
        return SpNumber;
    }

    public int getTP_pid()
    {
        return TP_pid;
    }

    public int getTP_udhi()
    {
        return TP_udhi;
    }

    public int getMessageCoding()
    {
        return MessageCoding;
    }

    public int getMessageLength()
    {
        return MessageLength;
    }

    public byte[] getMessageByte()
    {
        return MessageByte;
    }

    public String getMessageContent()
    {
        return MessageContent;
    }
}
