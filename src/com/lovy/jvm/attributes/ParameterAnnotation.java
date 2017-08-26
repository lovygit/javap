package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class ParameterAnnotation {
//    private int numAnnotations;
    private Annotation[] annotations;

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }
}
