package com.lovy.jvm.pool;

/**
 * Created by asus on 2017/7/1.
 */
public class NameAndTypeInfo {
    public static final int tag= InfoTag.NAME_AND_TYPE.val;
    private int descriptorIndex;
    private int nameIndex;

    public static final int length=5;
    public static Object[] poolObjs;

    public NameAndTypeInfo(byte[] buf){
        int tag=buf[0]&0xff;
        if(tag!= NameAndTypeInfo.tag)
            System.err.println("NameAndTypeInfo Table Tag Exception");
        this.nameIndex=(buf[1]&0xff)<<8|(buf[2]&0xff);
        this.descriptorIndex=(buf[3]&0xff)<<8|(buf[4]&0xff);
    }

    public int getDescriptorIndex(){
        return descriptorIndex;
    }

    public int getNameIndex(){
        return nameIndex;
    }


    @Override
    public String toString(){
        /*
        builder.append(String.format("%-15s","#"+((NameAndTypeInfo)poolObjs[i]).getIndex()+":#"+((NameAndTypeInfo)poolObjs[i]).getDescriptorIndex()));
                builder.append("\t\t// ");

                builder.append(poolObjs[((NameAndTypeInfo)poolObjs[i]).getIndex()]+":");
                builder.append(poolObjs[((NameAndTypeInfo)poolObjs[i]).getDescriptorIndex()]);
         */

        StringBuilder builder=new StringBuilder();
        builder.append(String.format("%-15s","#"+nameIndex+":#"+descriptorIndex));
        builder.append("\t\t// ");
        builder.append(poolObjs[nameIndex]);
        builder.append(":");
        builder.append(poolObjs[descriptorIndex]);

        return builder.toString();
    }


}
