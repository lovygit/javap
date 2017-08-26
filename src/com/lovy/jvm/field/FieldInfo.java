package com.lovy.jvm.field;

import com.lovy.jvm.attributes.BaseAttribute;

/**
 * Created by asus on 2017/7/1.
 */
public class FieldInfo {
    private int accessFlag;
    private int nameIndex;
    private int descriptorIndex;
//    private int attrCount;
    private BaseAttribute[] attributes;

    public int getAccessFlag() {
        return accessFlag;
    }

    public void setAccessFlag(int accessFlag) {
        this.accessFlag = accessFlag;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public int getDescriptorIndex() {
        return descriptorIndex;
    }

    public void setDescriptorIndex(int descriptorIndex) {
        this.descriptorIndex = descriptorIndex;
    }

    public BaseAttribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(BaseAttribute[] attributes) {
        this.attributes = attributes;
    }
}
