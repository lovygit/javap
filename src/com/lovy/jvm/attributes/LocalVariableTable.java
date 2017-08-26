package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class LocalVariableTable extends BaseAttribute {
//    private int localVariableTableLength;
    private LocalVariableTableItem[] localVariableTableItems;


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

    public LocalVariableTableItem[] getLocalVariableTableItems() {
        return localVariableTableItems;
    }

    public void setLocalVariableTableItems(LocalVariableTableItem[] localVariableTableItems) {
        this.localVariableTableItems = localVariableTableItems;
    }

    public static class LocalVariableTableItem{
        private int startPc;
        private int length;
        private int nameIndex;
        private int descriptorIndex;
        private int index;

        public int getStartPc() {
            return startPc;
        }

        public void setStartPc(int startPc) {
            this.startPc = startPc;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getNameIndex() {
            return nameIndex;
        }

        public void setNameIndex(int nameIndex) {
            this.nameIndex = nameIndex;
        }

        public int getDescriptorIndex() {
            return descriptorIndex;
        }

        public void setDescriptorIndex(int descriptorIndex) {
            this.descriptorIndex = descriptorIndex;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
