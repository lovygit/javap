package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class LineNumberTable extends BaseAttribute {
    private LineNumberTableItem[] tableItems;
//    private int lineNumberTableCount;

    public LineNumberTable(){}
    public LineNumberTable(int nameIndex,long length,LineNumberTableItem[] tableItems){
        this.nameIndex=nameIndex;
        this.length=length;
        this.tableItems=tableItems;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public long getLength() {
        return length;
    }

    public LineNumberTableItem[] getTableItems() {
        return tableItems;
    }

    public static class LineNumberTableItem{
        private int startPC;
        private int lineNumber;

        public LineNumberTableItem(){}
        public LineNumberTableItem(int startPC,int lineNumber){
            this.startPC=startPC;
            this.lineNumber=lineNumber;
        }

        public int getStartPC() {
            return startPC;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public void setStartPC(int startPC) {
            this.startPC = startPC;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setTableItems(LineNumberTableItem[] tableItems) {
        this.tableItems = tableItems;
    }
}
