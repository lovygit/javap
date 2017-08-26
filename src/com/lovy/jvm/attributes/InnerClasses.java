package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class InnerClasses extends BaseAttribute {
//    private int classesCount;



    private InnerClassItem[] innerClasses;

    public InnerClasses(){}

    public InnerClasses(int nameIndex,long length,InnerClassItem[] innerClasses){
        this.nameIndex=nameIndex;
        this.length=length;
        this.innerClasses=innerClasses;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public long getLength() {
        return length;
    }

    public InnerClassItem[] getInnerClasses() {
        return innerClasses;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setInnerClasses(InnerClassItem[] innerClasses) {
        this.innerClasses = innerClasses;
    }

    public static class InnerClassItem{
        private int innerClassInfoIndex;
        private int outerClassInfoIndex;
        private int innerNameIndex;
        private int innerClassAccessFlags;

        public int getInnerClassInfoIndex() {
            return innerClassInfoIndex;
        }

        public void setInnerClassInfoIndex(int innerClassInfoIndex) {
            this.innerClassInfoIndex = innerClassInfoIndex;
        }

        public int getOuterClassInfoIndex() {
            return outerClassInfoIndex;
        }

        public void setOuterClassInfoIndex(int outerClassInfoIndex) {
            this.outerClassInfoIndex = outerClassInfoIndex;
        }

        public int getInnerNameIndex() {
            return innerNameIndex;
        }

        public void setInnerNameIndex(int innerNameIndex) {
            this.innerNameIndex = innerNameIndex;
        }

        public int getInnerClassAccessFlag() {
            return innerClassAccessFlags;
        }

        public void setInnerClassAccessFlag(int innerClassAccessFlag) {
            this.innerClassAccessFlags = innerClassAccessFlag;
        }
    }
}
