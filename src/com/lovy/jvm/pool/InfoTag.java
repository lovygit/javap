package com.lovy.jvm.pool;

/**
 * Created by dyard on 2017/7/4.
 */
public enum InfoTag {
    UTF8(0X01),
    INTEGER(0X03),
    FLOAT(0X04),
    LONG(0X05),
    DOUBLE(0X06),
    CLASS(0X07),
    STRING(0X08),
    FIELDREF(0X09),
    METHODREF(0X0A),
    INTERFACE_METHODREF(0X0B),
    NAME_AND_TYPE(0X0C),
    METHODHANDLE(0X0F),
    METHOD_TYPE(0X10),
    INVOKE_DYNAMIC(0X12);

    public int val;
    InfoTag(int val){
        this.val=val;
    }
}
