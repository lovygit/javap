package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class Deprecated extends BaseAttribute {

    public int getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String toString(Object[] poolObjs){
        return poolObjs[nameIndex]+" [ "+length+" ]";
    }
}
