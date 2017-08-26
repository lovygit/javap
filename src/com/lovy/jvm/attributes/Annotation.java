package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class Annotation {
    private int typeIndex;
    //    private int pairNums;//num_element_value_pairs;
    private ElementValuePair[] pairs;

    public int getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(int typeIndex) {
        this.typeIndex = typeIndex;
    }

//    public int getPairNums() {
//        return pairNums;
//    }

//    public void setPairNums(int pairNums) {
//        this.pairNums = pairNums;
//    }

    public ElementValuePair[] getPairs() {
        return pairs;
    }

    public void setPairs(ElementValuePair[] pairs) {
        this.pairs = pairs;
    }

    public String toString(Object[] pollObjs){
        return pollObjs[typeIndex].toString();
    }
}
