package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class Synthetic extends BaseAttribute {
    public Synthetic(){}
    public Synthetic(int nameIndex,long length){
        this.nameIndex=nameIndex;
        this.length=length;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public long getLength() {
        return length;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
