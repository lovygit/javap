package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class Utf8Info {
    public static final int tag=InfoTag.UTF8.val;

//    private int length;
    private String value;

    public Utf8Info(byte[] buf){
        int tag=buf[0]&0xff;
        if(tag!= Utf8Info.tag)
            System.err.println("Utf8Info Table Tag Exception");
//        this.length=length;
        int length=(buf[1]&0xff)<<8|(buf[2]&0xff);
        this.value=new String(buf,3,length);
    }

    public int getLength(){
        return value.length();
    }

    @Override
    public String toString(){
        return value;
    }
}
