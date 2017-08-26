package com.lovy.jvm.attributes;

/**
 * Created by dyard on 2017/7/5.
 */
public class BaseAttribute {
    public static Object[] poolObjects;
    protected int nameIndex;
    protected long length;
    protected byte[] attribute;

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

    public byte[] getAttribute() {
        return attribute;
    }

    public void setAttribute(byte[] attribute) {
        this.attribute = attribute;
    }
}
