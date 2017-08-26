package com.lovy.jvm.attributes;

/**
 * Created by dyard on 2017/7/5.
 */
public class BootstrapMethods extends BaseAttribute {
//    private int numBootstrapMethods;
    private BootstrapMethod[] bootstrapMethods;

    public BootstrapMethod[] getBootstrapMethods() {
        return bootstrapMethods;
    }

    public void setBootstrapMethods(BootstrapMethod[] bootstrapMethods) {
        this.bootstrapMethods = bootstrapMethods;
    }

    public static class BootstrapMethod{
        public int bootstrapMethodref;
//        public int numBootstrapArguments;
        public int[] bootstrapArguments;
    }
}
