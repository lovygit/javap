package com.lovy.jvm.attributes;

/**
 * Created by dyard on 2017/7/5.
 */
public class TypeAnnotation {
    private int targetType;
    
    private int typeParameterTarget_typeParameterIndex=Integer.MIN_VALUE;
    
    private int superTypeTarget_superTypeIndex=Integer.MIN_VALUE;
    
    private int typeParameterBoundTarget_typeParameterIndex=Integer.MIN_VALUE;
    private int typeParameterBoundTarget_boundIndex=Integer.MIN_VALUE;
    
    private boolean emptyTarget=false;
    
    private int methodFormalParameterTarget_formalParameterIndex=Integer.MIN_VALUE;
    
    private int throwsTarget_throwsTypeIndex=Integer.MIN_VALUE;
    
    private LocalVarTarget localVarTarget;
    
    private int catchTarget_exceptionTableIndex=Integer.MIN_VALUE;
    
    private int offsetTarget_offset=Integer.MIN_VALUE;
    
    private int typeArgumentTarget_offset=Integer.MIN_VALUE;
    private int typeArgumentTarget_typeArgumentIndex=Integer.MIN_VALUE;
    
    private TypePath typePath;
    
    private int typeIndex= Integer.MIN_VALUE;
//    private int numElementValuePairs;
    
    private ElementValuePair[] elementValuePairs;
    
    
    public static class LocalVarTarget{
//        public int tableLength;
        public Table[] tables;
        
        public static class Table{
            public int startPc;
            public int length;
            public int index;
        }
    }
    
    
    public static class TypePath{
//        public int pathLength;
        public Path[] paths;
        
        public static class Path{
            public int typePathKink;
            public int typeArgumentIndex;
        }
    }


    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public int getTypeParameterTarget_typeParameterIndex() {
        return typeParameterTarget_typeParameterIndex;
    }

    public void setTypeParameterTarget_typeParameterIndex(int typeParameterTarget_typeParameterIndex) {
        this.typeParameterTarget_typeParameterIndex = typeParameterTarget_typeParameterIndex;
    }

    public int getSuperTypeTarget_superTypeIndex() {
        return superTypeTarget_superTypeIndex;
    }

    public void setSuperTypeTarget_superTypeIndex(int superTypeTarget_superTypeIndex) {
        this.superTypeTarget_superTypeIndex = superTypeTarget_superTypeIndex;
    }

    public int getTypeParameterBoundTarget_typeParameterIndex() {
        return typeParameterBoundTarget_typeParameterIndex;
    }

    public void setTypeParameterBoundTarget_typeParameterIndex(int typeParameterBoundTarget_typeParameterIndex) {
        this.typeParameterBoundTarget_typeParameterIndex = typeParameterBoundTarget_typeParameterIndex;
    }

    public int getTypeParameterBoundTarget_boundIndex() {
        return typeParameterBoundTarget_boundIndex;
    }

    public void setTypeParameterBoundTarget_boundIndex(int typeParameterBoundTarget_boundIndex) {
        this.typeParameterBoundTarget_boundIndex = typeParameterBoundTarget_boundIndex;
    }

    public boolean isEmptyTarget() {
        return emptyTarget;
    }

    public void setEmptyTarget(boolean emptyTarget) {
        this.emptyTarget = emptyTarget;
    }

    public int getMethodFormalParameterTarget_formalParameterIndex() {
        return methodFormalParameterTarget_formalParameterIndex;
    }

    public void setMethodFormalParameterTarget_formalParameterIndex(int methodFormalParameterTarget_formalParameterIndex) {
        this.methodFormalParameterTarget_formalParameterIndex = methodFormalParameterTarget_formalParameterIndex;
    }

    public int getThrowsTarget_throwsTypeIndex() {
        return throwsTarget_throwsTypeIndex;
    }

    public void setThrowsTarget_throwsTypeIndex(int throwsTarget_throwsTypeIndex) {
        this.throwsTarget_throwsTypeIndex = throwsTarget_throwsTypeIndex;
    }

    public LocalVarTarget getLocalVarTarget() {
        return localVarTarget;
    }

    public void setLocalVarTarget(LocalVarTarget localVarTarget) {
        this.localVarTarget = localVarTarget;
    }

    public int getCatchTarget_exceptionTableIndex() {
        return catchTarget_exceptionTableIndex;
    }

    public void setCatchTarget_exceptionTableIndex(int catchTarget_exceptionTableIndex) {
        this.catchTarget_exceptionTableIndex = catchTarget_exceptionTableIndex;
    }

    public int getOffsetTarget_offset() {
        return offsetTarget_offset;
    }

    public void setOffsetTarget_offset(int offsetTarget_offset) {
        this.offsetTarget_offset = offsetTarget_offset;
    }

    public int getTypeArgumentTarget_offset() {
        return typeArgumentTarget_offset;
    }

    public void setTypeArgumentTarget_offset(int typeArgumentTarget_offset) {
        this.typeArgumentTarget_offset = typeArgumentTarget_offset;
    }

    public int getTypeArgumentTarget_typeArgumentIndex() {
        return typeArgumentTarget_typeArgumentIndex;
    }

    public void setTypeArgumentTarget_typeArgumentIndex(int typeArgumentTarget_typeArgumentIndex) {
        this.typeArgumentTarget_typeArgumentIndex = typeArgumentTarget_typeArgumentIndex;
    }

    public TypePath getTypePath() {
        return typePath;
    }

    public void setTypePath(TypePath typePath) {
        this.typePath = typePath;
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(int typeIndex) {
        this.typeIndex = typeIndex;
    }

    public ElementValuePair[] getElementValuePairs() {
        return elementValuePairs;
    }

    public void setElementValuePairs(ElementValuePair[] elementValuePairs) {
        this.elementValuePairs = elementValuePairs;
    }
}
