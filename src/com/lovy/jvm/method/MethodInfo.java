package com.lovy.jvm.method;

import com.lovy.jvm.field.FieldInfo;

/**
 * Created by asus on 2017/7/1.
 */
public class MethodInfo extends FieldInfo {

    public MethodInfo(){}
    public MethodInfo(FieldInfo fieldInfo){
        this.setAccessFlag(fieldInfo.getAccessFlag());
        this.setAttributes(fieldInfo.getAttributes());
        this.setDescriptorIndex(fieldInfo.getDescriptorIndex());
        this.setNameIndex(fieldInfo.getNameIndex());
    }
}
