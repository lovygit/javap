package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class DoubleInfo {
    public static final int tag= InfoTag.DOUBLE.val;
    private double value;

    public static final int length=9;
    public DoubleInfo(byte[] buf){
        int tag=buf[0]&0xff;
        if(tag!= DoubleInfo.tag)
            System.err.println("DoubleInfo Table Tag Exception");

        long doubleLongBits=(long)(buf[1]&0xff)<<56|
                (long)(buf[2]&0xff)<<48|
                (long)(buf[3]&0xff)<<40|
                (long)(buf[4]&0xff)<<32|
                (buf[5]&0xff)<<24|
                (buf[6]&0xff)<<16|
                (buf[7]&0xff)<<8|
                (buf[8]&0xff);

        this.value=Double.longBitsToDouble(doubleLongBits);
    }

    public double getValue(){
        return value;
    }

    @Override
    public String toString(){
        return String.valueOf(this.value)+"d";
    }
}
