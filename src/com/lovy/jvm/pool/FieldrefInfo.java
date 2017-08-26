package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class FieldrefInfo {
    public static final int tag= InfoTag.FIELDREF.val;
    private int classIndex;
    private int nameAndTypeIndex;

    public static final int length=5;
    public static Object[] poolObjs;

    public FieldrefInfo(byte[] buf){
        int tag=buf[0]&0xff;
        if(tag!= FieldrefInfo.tag)
            System.err.println("FieldrefInfo Table Tag Exception");

        this.classIndex=(buf[1]&0xff)<<8|(buf[2]&0xff);
        this.nameAndTypeIndex=(buf[3]&0xff)<<8|(buf[4]&0xff);
    }

    public int getClassIndex() {
        return classIndex;
    }

    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }

    @Override
    public String toString(){
        StringBuilder builder=new StringBuilder();
        builder.append(String.format("%-15s","#"+classIndex+".#"+nameAndTypeIndex));

        builder.append("\t\t// ");
        builder.append(poolObjs[((ClassInfo)poolObjs[classIndex]).getNameIndex()]+".");
        builder.append(poolObjs[((NameAndTypeInfo)poolObjs[nameAndTypeIndex]).getNameIndex()]);
        builder.append(":");
        builder.append(poolObjs[((NameAndTypeInfo)poolObjs[nameAndTypeIndex]).getDescriptorIndex()]);

        return builder.toString();
    }
}
