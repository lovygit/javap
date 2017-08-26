package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class Code extends BaseAttribute {
    private int maxStack;
    private int maxLocals;

    private byte[] code;

    private ExceptionTable[] exceptionTables;
//    private int attrCount;

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }

    private BaseAttribute[] attributes;

    public static class ExceptionTable{
        private int startPc;
        private int endPc;
        private int handlerPc;
        private int catchType;

        public int getStartPc() {
            return startPc;
        }

        public int getEndPc() {
            return endPc;
        }

        public int getHandlerPc() {
            return handlerPc;
        }

        public int getCatchType() {
            return catchType;
        }

        public void setStartPc(int startPc) {
            this.startPc = startPc;
        }

        public void setEndPc(int endPc) {
            this.endPc = endPc;
        }

        public void setHandlerPc(int handlerPc) {
            this.handlerPc = handlerPc;
        }

        public void setCatchType(int catchType) {
            this.catchType = catchType;
        }
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    public void setMaxLocals(int maxLocals) {
        this.maxLocals = maxLocals;
    }

    public void setExceptionTables(ExceptionTable[] exceptionTables) {
        this.exceptionTables = exceptionTables;
    }

    public void setAttributes(BaseAttribute[] attributes) {
        this.attributes = attributes;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public long getLength() {
        return length;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public ExceptionTable[] getExceptionTables() {
        return exceptionTables;
    }

    public BaseAttribute[] getAttributes() {
        return attributes;
    }

}
