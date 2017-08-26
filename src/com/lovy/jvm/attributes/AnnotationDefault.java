package com.lovy.jvm.attributes;

/**
 * Created by dyard on 2017/7/5.
 */
public class AnnotationDefault extends BaseAttribute {
    private ElementValue defaultValue;

    public ElementValue getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(ElementValue defaultValue) {
        this.defaultValue = defaultValue;
    }
}
