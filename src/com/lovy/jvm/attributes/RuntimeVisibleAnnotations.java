package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 * since <b>RuntimeVisibleAnnotation</b>'s components are same as
 * <b>RunTimeInvisibleAnnotations</b>'s components.
 * So There is only one class which represent these two classes;
 */
public class RuntimeVisibleAnnotations extends BaseAttribute {
    private boolean visible=true;
//    private int annotationsCount;
//    private
    private Annotation[] annotations;

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

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
