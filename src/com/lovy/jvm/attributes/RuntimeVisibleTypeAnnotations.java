package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class RuntimeVisibleTypeAnnotations extends BaseAttribute{
    private boolean visible=true;
//    private int numAnnotations;
    private TypeAnnotation[] annotations;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public TypeAnnotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(TypeAnnotation[] annotations) {
        this.annotations = annotations;
    }
}
