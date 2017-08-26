package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class Exceptions extends BaseAttribute {
    private int[] exceptionIndexes;

    public Exceptions(){}
    public Exceptions(int nameIndex,long length,int[] exceptionIndexes){
        this.nameIndex=nameIndex;
        this.length=length;
        this.exceptionIndexes=exceptionIndexes;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public long getLength() {
        return length;
    }

    public int[] getExceptionIndexes() {
        return exceptionIndexes;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setExceptionIndexes(int[] exceptionIndexes) {
        this.exceptionIndexes = exceptionIndexes;
    }
}
