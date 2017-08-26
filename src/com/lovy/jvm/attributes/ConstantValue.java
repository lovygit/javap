package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class ConstantValue extends BaseAttribute {
    private int valueIndex;

    public ConstantValue(){}
    public ConstantValue(int nameIndex,long length,int valueIndex){
        this.nameIndex=nameIndex;
        this.length=length;
        this.valueIndex=valueIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public long getLength() {
        return length;
    }

    public int getValueIndex() {
        return valueIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setValueIndex(int valueIndex) {
        this.valueIndex = valueIndex;
    }
}
