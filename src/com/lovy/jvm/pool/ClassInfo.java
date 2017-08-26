package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class ClassInfo {
    public static final int tag= InfoTag.CLASS.val;
    private int nameIndex;

    public static final int length=3;
    public static Object[] poolObjs;

    public ClassInfo(byte[] buf){
        int tag=buf[0]&0xff;
        if(tag!= ClassInfo.tag)
            System.err.print("ClassInfo Table Tag Exception");

        this.nameIndex=(buf[1]&0xff)<<8|(buf[2]&0xff);
    }

    public static int getTag() {
        return tag;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    @Override
    public String toString(){
        StringBuilder builder=new StringBuilder();
        builder.append(String.format("%-15s","#"+this.getNameIndex()));
        builder.append("\t\t// ");
        builder.append(poolObjs[nameIndex]);
        return builder.toString();
    }
}
