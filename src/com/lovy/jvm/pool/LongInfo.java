package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class LongInfo {
    public static final int tag=InfoTag.LONG.val;
    private long value;

    public static final int length=9;
    public LongInfo(byte[] buf){
        int tag=buf[0]&0xff;
        if(tag!= LongInfo.tag)
            System.err.println("LongInfo Table Tag Exception");
        this.value=(buf[1]&0xffL)<<56|
                (buf[2]&0xffL)<<48|
                (buf[3]&0xffL)<<40|
                (buf[4]&0xffL)<<32|
                (buf[5]&0xffL)<<24|
                (buf[6]&0xffL)<<16|
                (buf[7]&0xffL)<<8|
                (buf[8]&0xffL);

    }

    public long getValue(){
        return value;
    }

    @Override
    public String toString(){
        return String.valueOf(this.value)+"L";
    }
}
