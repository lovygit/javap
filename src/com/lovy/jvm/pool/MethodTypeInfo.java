package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class MethodTypeInfo {
    public static final int tag= InfoTag.METHOD_TYPE.val;
    private int descriptorIndex;

    public static final int length=3;

    public MethodTypeInfo(byte[] buf){
        int tag=buf[0]&0xff;

        if(tag!= MethodTypeInfo.tag)
            System.err.println("MethodTypeInfo Table Tag Exception");
        this.descriptorIndex=(buf[1]&0xff)<<8|(buf[2]&0xff);
    }

    public int getDescriptorIndex(){
        return descriptorIndex;
    }
}
