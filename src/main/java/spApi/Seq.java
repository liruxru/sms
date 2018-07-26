// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   Seq.java

package spApi;

import java.util.Date;

public class Seq
{

    public static Class seqclass = (new Seq()).getClass();
    private static long globalSeq_1;
    private static int globalSeq_2;
    private static int globalSeq_3;
    private static int Min_Seq;
    private static int Max_Seq;
    private static Date original = new Date();

    public Seq(int min, int max)
    {
        Min_Seq = min;
        Max_Seq = max;
        globalSeq_3 = Min_Seq;
    }

    public Seq()
    {
        Min_Seq = 0;
        Max_Seq = 0x7fffffff;
    }

    public void setNodeId(long nodeId)
    {
        globalSeq_1 = nodeId;
    }

    public long getGlobalSeq_1()
    {
        return globalSeq_1;
    }

    public int getGlobalSeq_2()
    {
        return globalSeq_2;
    }

    public int getGlobalSeq_3()
    {
        return globalSeq_3;
    }

    public static synchronized void computeSequence()
    {
        Date now = new Date();
        if(globalSeq_3 == Max_Seq)
            globalSeq_3 = Min_Seq;
        else
            globalSeq_3++;
        globalSeq_2 = (now.getMonth() + 1) * 0x5f5e100 + now.getDate() * 0xf4240 + now.getHours() * 10000 + now.getMinutes() * 100 + now.getSeconds();
    }

}
