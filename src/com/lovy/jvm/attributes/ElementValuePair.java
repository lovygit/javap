package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class ElementValuePair{
    private int nameIndex;
    private ElementValue elementValue;

    public int getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public ElementValue getElementValue() {
        return elementValue;
    }

    public void setElementValue(ElementValue elementValue) {
        this.elementValue = elementValue;
    }
}
