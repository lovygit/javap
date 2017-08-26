package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class StringInfo {
    public static final int tag= InfoTag.STRING.val;
    private int stringIndex;

    public static final int length=3;
    public static Object[] poolObjs;

    public StringInfo(byte[] buf){
        int tag=buf[0]&0xff;
        if(tag!= StringInfo.tag)
            System.err.println("StringInfo Table Tag Exception");
        this.stringIndex=(buf[1]&0xff)<<8|(buf[2]&0xff);
    }

    public int getStringIndex() {
        return stringIndex;
    }

    @Override
    public String toString(){
        StringBuilder builder=new StringBuilder();
        builder.append(String.format("%-15s","#"+stringIndex));
        builder.append("\t\t// ");
        builder.append(poolObjs[stringIndex]);
        return builder.toString();
    }
}
