package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class LocalVariableTypeTable extends BaseAttribute {
//    private int localVariableTypeTableLength;
    private LocalVariableTypeTable.LocalVariableTypeTableItem[] localVariableTypeTableItems;


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

    public LocalVariableTypeTableItem[] getLocalVariableTableItems() {
        return localVariableTypeTableItems;
    }

    public void setLocalVariableTableItems(LocalVariableTypeTableItem[] localVariableTableItems) {
        this.localVariableTypeTableItems = localVariableTableItems;
    }

    public static class LocalVariableTypeTableItem{
        private int startPc;
        private int length;
        private int nameIndex;
        private int signatureIndex;
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

        public int getSignatureIndex() {
            return signatureIndex;
        }

        public void setSignatureIndex(int signatureIndex) {
            this.signatureIndex = signatureIndex;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
