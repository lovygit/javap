package com.lovy.jvm.attributes;

/**
 * Created by dyard on 2017/7/5.
 */
public class MethodParameters extends BaseAttribute{
//    private int parametersCount;
    Parameter[] parameters;

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }

    public static class Parameter{
        public int nameIndex;
        public int accessFlags;
    }
}
