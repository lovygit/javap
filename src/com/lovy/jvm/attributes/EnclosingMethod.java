package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class EnclosingMethod extends BaseAttribute {
    private int classIndex;
    private int methodIndex;

    public EnclosingMethod(){}
    public EnclosingMethod(int nameIndex,long length,int classIndex,int methodIndex){
        this.nameIndex=nameIndex;
        this.length=length;
        this.classIndex=classIndex;
        this.methodIndex=methodIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public long getLength() {
        return length;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public int getMethodIndex() {
        return methodIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }

    public void setMethodIndex(int methodIndex) {
        this.methodIndex = methodIndex;
    }
}
