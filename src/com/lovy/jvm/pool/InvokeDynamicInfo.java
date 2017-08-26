package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class InvokeDynamicInfo {
    public static final int tag= InfoTag.INVOKE_DYNAMIC.val;
    private int methodAttrnameAndTypeIndex;
    private int nameAndTypeIndex;

    public static final int length=5;

    public InvokeDynamicInfo(byte[] buf){
        int tag=buf[0]&0xff;
        if(tag!= InvokeDynamicInfo.tag)
            System.err.println("InvokeDynamicInfo Table Tag Exception");
        this.methodAttrnameAndTypeIndex=(buf[1]&0xff)<<8|(buf[2]&0xff);
        this.nameAndTypeIndex=(buf[3]&0xff)<<8|(buf[4]&0xff);
    }

    public int getMethodAttrnameAndTypeIndex(){
        return methodAttrnameAndTypeIndex;
    }

    public int getNameAndTypeIndex(){
        return nameAndTypeIndex;
    }
}
