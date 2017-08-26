package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class IntegerInfo {
    public static final int tag= InfoTag.INTEGER.val;
    private int value;

    public static final int length=5;

    public IntegerInfo(byte[] buf){
        int tag=buf[0]&0xff;
        if(tag!= IntegerInfo.tag)
            System.err.println("IntegerInfo Table Tag Exception");
        this.value=(buf[1]&0xff)<<24|
                (buf[2]&0xff)<<16|
                (buf[3]&0xff)<<8|
                (buf[4]&0xff);
    }

    public int getValue(){
        return value;
    }

    @Override
    public String toString(){
        return Integer.valueOf(value).toString();
    }
}
