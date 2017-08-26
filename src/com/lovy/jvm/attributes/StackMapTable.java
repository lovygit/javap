package com.lovy.jvm.attributes;

/**
 * Created by asus on 2017/7/1.
 */
public class StackMapTable extends BaseAttribute {
//    public int numEntries;
    private StackMapFrame[] entries;

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

    public StackMapFrame[] getEntries() {
        return entries;
    }

    public void setEntries(StackMapFrame[] entries) {
        this.entries = entries;
    }

    public static class StackMapFrame{
        public SameFrame sameFrame;
        public SameLocals1StackItemFrame sameLocals1StackItemFrame;
        public SameLocals1StackItemFrameExtended sameLocals1StackItemFrameExtended;
        public ChopFrame chopFrame;
        public SameFrameExtended sameFrameExtended;
        public AppendFrame appendFrame;
        public FullFrame fullFrame;
        
        
        public static class SameFrame{
            public char frameType;
        }
        
        public static class SameLocals1StackItemFrame{
            public char frameType;
            public VerificationTypeInfo[] stack;
        }

        public static class SameLocals1StackItemFrameExtended{
            public char frameType;
            public int offsetDelta;
            public VerificationTypeInfo[] stack;
        }

        public static class ChopFrame{
            public char frameType;
            public int offsetDelta;
        }

        public static class SameFrameExtended{
            public char frameType;
            public int offsetDelta;
        }

        public static class AppendFrame{
            public char frameType;
            public int offsetDelta;
            public VerificationTypeInfo[] locals;
        }

        public static class FullFrame{
            public char frameType;
            public int offsetDelta;
//            public int numLocals;
//            public int numStackItems;
            public VerificationTypeInfo[] typeInfoLocals;
            public VerificationTypeInfo[] typeInfoStack;
        }
    }

    public static class VerificationTypeInfo{
        public char topVariableInfoTag=Character.MAX_VALUE;
        public char integerVariableInfoTag=Character.MAX_VALUE;
        public char floatVariableInfoTag=Character.MAX_VALUE;
        public char longVariableInfoTag=Character.MAX_VALUE;
        public char doubleVariableInfoTag=Character.MAX_VALUE;
        public char nullVariableInfoTag=Character.MAX_VALUE;
        public char uninitializedThisVariableInfoTag=Character.MAX_VALUE;

        public char objectVariableInfoTag=Character.MAX_VALUE;
        public int objectVariableInfoCPoolIndex=Integer.MIN_VALUE;

        public char uninitializedVariableInfoTag=Character.MAX_VALUE;
        public int uninitializedVariableInfoOffset=Integer.MIN_VALUE;
    }
}
