package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class SourceDebugExtension extends BaseAttribute {
//    private int nameIndex;
//    private int length;,the length shoule be long ,be the limited sizeof array Integer.MAX_VALUE;
//    private byte[] debugExtension;

    public SourceDebugExtension(int nameIndex,byte[] debugExtension){
        this.nameIndex=nameIndex;
        this.attribute=debugExtension;
    }
}
