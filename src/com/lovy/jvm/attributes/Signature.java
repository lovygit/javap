package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class Signature extends BaseAttribute {
    private int signatureIndex;

    public Signature(){}
    public Signature(int nameIndex,long length,int signatureIndex){
        this.nameIndex=nameIndex;
        this.length=length;
        this.signatureIndex=signatureIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public long getLength() {
        return length;
    }

    public int getSignatureIndex() {
        return signatureIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setSignatureIndex(int signatureIndex) {
        this.signatureIndex = signatureIndex;
    }


    public String toString(Object[] poolObjs){
        String signatureStr=poolObjs[signatureIndex].toString();
        return "Signature [ "+length+" ] , "+signatureStr;
    }
}
