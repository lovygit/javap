package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class MethodHandleInfo {
    public static final int tag= InfoTag.METHODHANDLE.val;
    private int referenceKind;
    private int referenceIndex;

    public static final int length=4;

    public MethodHandleInfo(byte[] buf){
        int tag=buf[0];
        if(tag!= MethodHandleInfo.tag)
            System.err.println("MethodHandleInfo Table Tag Exception");
        this.referenceKind=buf[1]&0xff;
        this.referenceIndex=(buf[2]&0xff)<<8|(buf[3]&0xff);
    }

    public int getReferenceKind(){
        return referenceKind;
    }

    public int getReferenceIndex(){
        return referenceIndex;
    }
}
