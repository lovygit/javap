package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class ElementValue {
    private char tag;
    private int constValueIndex=-1;
    private EnumConstValue enumConstValue;
    private int classInfoIndex=-1;
    private Annotation annotationValue;
    private ArrayValue arrayValue;

    public char getTag() {
        return tag;
    }

    public void setTag(char tag) {
        this.tag = tag;
    }

    public int getConstValueIndex() {
        return constValueIndex;
    }

    public void setConstValueIndex(int constValueIndex) {
        this.constValueIndex = constValueIndex;
    }

    public EnumConstValue getEnumConstValue() {
        return enumConstValue;
    }

    public void setEnumConstValue(EnumConstValue enumConstValue) {
        this.enumConstValue = enumConstValue;
    }

    public int getClassInfoIndex() {
        return classInfoIndex;
    }

    public void setClassInfoIndex(int classInfoIndex) {
        this.classInfoIndex = classInfoIndex;
    }

    public ArrayValue getArrayValue() {
        return arrayValue;
    }

    public void setArrayValue(ArrayValue arrayValue) {
        this.arrayValue = arrayValue;
    }

    public static class EnumConstValue{
        public int typeNameIndex;
        public int constNameIndex;
    }

    public static class ArrayValue{
        public int numValues;
        public ElementValue[] elementValues;
    }

    public Annotation getAnnotationValue() {
        return annotationValue;
    }

    public void setAnnotationValue(Annotation annotationValue) {
        this.annotationValue = annotationValue;
    }
}
