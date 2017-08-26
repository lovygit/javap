package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class FloatInfo {
    public static final int tag= InfoTag.FLOAT.val;
    private float value;

    public static final int length=5;

    public FloatInfo(byte[] buf){
        int tag=buf[0]&0xff;
        if(tag!= FloatInfo.tag)
            System.err.println("FloatInfo Table Tag Exception");

        int floatIntBits=(buf[1]&0xff)<<24|
                (buf[2]&0xff)<<16|
                (buf[3]&0xff)<<8|
                (buf[4]&0xff);
        this.value=Float.intBitsToFloat(floatIntBits);
    }
    public float getValue(){
        return this.value;
    }

    @Override
    public String toString(){
        return String.valueOf(value)+"f";
    }
}
