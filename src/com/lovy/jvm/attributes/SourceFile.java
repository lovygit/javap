package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class SourceFile extends BaseAttribute {
    private int sourceFileIndex;

    public SourceFile(){}
    public SourceFile(int nameIndex,long length,int sourceFileIndex){
        this.nameIndex=nameIndex;
        this.length=length;
        this.sourceFileIndex=sourceFileIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public long getLength() {
        return length;
    }

    public int getSourceFileIndex() {
        return sourceFileIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setSourceFileIndex(int sourceFileIndex) {
        this.sourceFileIndex = sourceFileIndex;
    }
}
