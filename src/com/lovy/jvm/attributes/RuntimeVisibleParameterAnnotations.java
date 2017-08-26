package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class RuntimeVisibleParameterAnnotations extends BaseAttribute {
    private boolean visible=true;
//    private int numParameters;
    private ParameterAnnotation[] parameterAnnotations;

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

    public ParameterAnnotation[] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public void setParameterAnnotations(ParameterAnnotation[] parameterAnnotations) {
        this.parameterAnnotations = parameterAnnotations;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
