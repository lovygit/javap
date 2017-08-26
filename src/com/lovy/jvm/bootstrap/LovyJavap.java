package com.lovy.jvm.bootstrap;

import com.lovy.jvm.attributes.*;
import com.lovy.jvm.attributes.Deprecated;
import com.lovy.jvm.field.FieldInfo;
import com.lovy.jvm.method.MethodInfo;
import com.lovy.jvm.pool.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by asus on 2017/7/1.
 */
public class LovyJavap {

    private String classFileName;
    private int readLen=0;
    private DataInputStream dis;
    private byte[] buf=new byte[512];

    private String version;

    private Object[] poolObjs;

    private int accessVal;

    public String getClassFileName() {
        return classFileName;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public String getVersion() {
        return version;
    }

    public int getAccessVal() {
        return accessVal;
    }

    public String getAccessFlag() {
        return accessFlag;
    }

    public String getClassName() {
        return className;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public String getInterfaces() {
        return interfaces;
    }

    public FieldInfo[] getFieldInfos() {
        return fieldInfos;
    }

    public MethodInfo[] getMethodInfos() {
        return methodInfos;
    }

    public BaseAttribute[] getAttributes() {
        return attributes;
    }

    private String accessFlag;

    private String className;

    private String superClassName;

    private String interfaces;

    private FieldInfo[] fieldInfos;

    private MethodInfo[] methodInfos;

    private BaseAttribute[] attributes;

//    public LovyJavap(){}

    public LovyJavap(String classFileName){
        try {
            this.classFileName=classFileName;
            this.dis=new DataInputStream(new FileInputStream(classFileName));
            retrieveInfo();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void retrieveInfo() throws IOException {
        readMagicNumber();

        version=readVersion();

        poolObjs=readConstantPool();
        initPoolObjects();

        accessVal=readAccessFlag();
        accessFlag=classFlagString(accessVal);

        className=poolObjs[((ClassInfo)poolObjs[readClassIndex()]).getNameIndex()].toString();

        int superClassIndex=readClassIndex();
        superClassName=superClassIndex==0? null:poolObjs[((ClassInfo)poolObjs[superClassIndex]).getNameIndex()].toString();

        interfaces=readInterfaces();

        fieldInfos=readField();

//            System.out.println("After read field,pos [ "+readLen+" ]");
        methodInfos=readMethod();

        attributes=readClassAttributes();
    }
    public LovyJavap(DataInputStream dis){
        this.dis=dis;
        try {
            retrieveInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initPoolObjects(){
        ClassInfo.poolObjs=poolObjs;
        FieldrefInfo.poolObjs=poolObjs;
        MethodrefInfo.poolObjs=poolObjs;
        InterfaceMethodrefInfo.poolObjs=poolObjs;
        StringInfo.poolObjs=poolObjs;
        NameAndTypeInfo.poolObjs=poolObjs;
    }

    public boolean readMagicNumber() throws IOException {
            int len=dis.read(buf,0,4);
//            if(len!=4)
//                return false;
            this.readLen+=len;

            String byteA=Integer.toHexString(Byte.toUnsignedInt(buf[0])).toUpperCase();
            String byteB=Integer.toHexString(Byte.toUnsignedInt(buf[1])).toUpperCase();
            String byteC=Integer.toHexString(Byte.toUnsignedInt(buf[2])).toUpperCase();
            String byteD=Integer.toHexString(Byte.toUnsignedInt(buf[3])).toUpperCase();

            if(byteA.equals("CA")&&byteB.equals("FE")&&byteC.equals("BA")&&byteD.equals("BE"))
                return true;
            return false;
    }

    public String readVersion() throws IOException {
        int len=dis.read(buf,0,4);
//        if(len!=4)
//            throw new IOException();
        this.readLen+=len;

        int numA=Byte.toUnsignedInt(buf[0]);
        int numB=Byte.toUnsignedInt(buf[1]);
        int numC=Byte.toUnsignedInt(buf[2]);
        int numD=Byte.toUnsignedInt(buf[3]);

        int minorVersion=numA*256+numB;
        int majorVersion=numC*256+numD;

        return majorVersion+":"+minorVersion;
    }


    private Object[] readConstantPool() throws IOException {
        int len=dis.read(buf,0,2);
//        if(len!=2)
//            throw new IOException();
        readLen+=len;

        int constantPoolCount=(buf[0]&0xff)<<8|(buf[1]&0xff);
//        System.out.println("There are "+constantPoolCount+" variables");

        Object[] poolObjs=new Object[constantPoolCount];
        int index=1;
        while(constantPoolCount>1){
            len=dis.read(buf,0,1);
            readLen+=len;

            char tag= (char) buf[0];

            switch (tag){
                case 1:
                    len=dis.read(buf,1,2);
                    readLen+=len;

                    int length=(buf[1]&0xff)<<8|(buf[2]&0xff);

                    len=dis.read(buf,3,length);
                    readLen+=len;

                    Utf8Info utf8Info=new Utf8Info(buf);
                    poolObjs[index++]=utf8Info;
                    break;
                case 3:
                    len=dis.read(buf,1,4);
                    readLen+=len;

                    IntegerInfo integerInfo=new IntegerInfo(buf);
                    poolObjs[index++]=integerInfo;
                    break;
                case 4:
                    len=dis.read(buf,1,4);
                    readLen+=len;

                    FloatInfo floatInfo=new FloatInfo(buf);
                    poolObjs[index++]=floatInfo;
                    break;
                case 5:
                    len=dis.read(buf,1,8);
                    readLen+=len;

                    LongInfo longInfo=new LongInfo(buf);
                    poolObjs[index++]=longInfo;

                    index++;//long occupys two positions
                    constantPoolCount--;//
                    break;
                case 6:
                    len=dis.read(buf,1,8);
                    readLen+=len;

                    DoubleInfo doubleInfo=new DoubleInfo(buf);
                    poolObjs[index++]=doubleInfo;

                    index++;//double occupys two positions;
                    constantPoolCount--;

                    break;
                case 7:
                    len=dis.read(buf,1,2);
                    readLen+=len;

                    ClassInfo classInfo=new ClassInfo(buf);
                    poolObjs[index++]=classInfo;
                    break;
                case 8:
                    len=dis.read(buf,1,2);
                    readLen+=len;

                    StringInfo stringInfo=new StringInfo(buf);
                    poolObjs[index++]=stringInfo;
                    break;
                case 9:
                    len=dis.read(buf,1,4);
                    readLen+=len;

                    FieldrefInfo fieldrefInfo =new FieldrefInfo(buf);
                    poolObjs[index++]= fieldrefInfo;
                    break;
                case 10:
                    len=dis.read(buf,1,4);
                    readLen+=len;

                    MethodrefInfo methodrefInfo =new MethodrefInfo(buf);
                    poolObjs[index++]= methodrefInfo;
                    break;
                case 11:
                    len=dis.read(buf,1,4);
                    readLen+=len;

                    InterfaceMethodrefInfo interfaceMethodrefInfo =new InterfaceMethodrefInfo(buf);
                    poolObjs[index++]= interfaceMethodrefInfo;
                    break;
                case 12:
                    len=dis.read(buf,1,4);
                    readLen+=len;

                    NameAndTypeInfo nameAndTypeInfo=new NameAndTypeInfo(buf);
                    poolObjs[index++]=nameAndTypeInfo;
                    break;
                case 15:
                    len=dis.read(buf,1,3);
                    readLen+=len;

                    MethodHandleInfo methodHandleInfo=new MethodHandleInfo(buf);
                    poolObjs[index++]=methodHandleInfo;
                    break;
                case 16:
                    len=dis.read(buf,1,2);
                    readLen+=len;

                    MethodTypeInfo methodTypeInfo=new MethodTypeInfo(buf);
                    poolObjs[index++]=methodTypeInfo;
                    break;
                case 18:
                    len=dis.read(buf,1,4);
                    readLen+=len;

                    InvokeDynamicInfo invokeDynamicInfo=new InvokeDynamicInfo(buf);
                    poolObjs[index++]=invokeDynamicInfo;
                    break;
                default:
                    System.err.println("Unexpected tag [ "+(int)tag+" ]"+", index "+index);
                    poolObjs[index++]=null;
            }
            constantPoolCount--;
        }
        return poolObjs;
    }

    private String poolString(int indent,Object[] poolObjs){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+='\t';

        StringBuilder builder=new StringBuilder();
        for(int i=1;i<poolObjs.length;i++) {
            if (poolObjs[i] == null)
                continue;
            String className = poolObjs[i].getClass().getSimpleName();
            className = className.substring(0, className.indexOf("Info"));

            int preIndent = String.valueOf(poolObjs.length).length() - String.valueOf(i).length();
            builder.append(indentTabs);
            for (int k = 0; k < preIndent; k++)
                builder.append(" ");
            builder.append("#");
            builder.append(i);
            builder.append(" = ");
            builder.append(String.format("%-10s", className));
            builder.append("\t\t");

            builder.append(poolObjs[i]+"\n");
        }
//            if(className.equals("Fieldref")){
//                builder.append(String.format("%-15s","#"+((FieldrefInfo)poolObjs[i]).getClassIndex()+".#"+((FieldrefInfo)poolObjs[i]).getNameAndTypeIndex()));
//                builder.append("\t\t// ");
//
//                builder.append(poolObjs[((ClassInfo)poolObjs[((FieldrefInfo)poolObjs[i]).getClassIndex()]).getNameIndex()]+".");
//                builder.append(poolObjs[((NameAndTypeInfo)(poolObjs[((FieldrefInfo)poolObjs[i]).getNameAndTypeIndex()])).getNameIndex()]+":");
//                builder.append(poolObjs[((NameAndTypeInfo)(poolObjs[((FieldrefInfo)poolObjs[i]).getNameAndTypeIndex()])).getDescriptorIndex()]);
//            }else if(className.equals("Methodref")){
//                builder.append(String.format("%-15s","#"+((MethodrefInfo)poolObjs[i]).getClassIndex()+".#"+((MethodrefInfo)poolObjs[i]).getNameAndTypeIndex()));
//                builder.append("\t\t// ");
//
//                builder.append(poolObjs[((ClassInfo)poolObjs[((MethodrefInfo)poolObjs[i]).getClassIndex()]).getNameIndex()]+".");
//                builder.append(poolObjs[((NameAndTypeInfo)(poolObjs[((MethodrefInfo)poolObjs[i]).getNameAndTypeIndex()])).getNameIndex()]+":");
//                builder.append(poolObjs[((NameAndTypeInfo)(poolObjs[((MethodrefInfo)poolObjs[i]).getNameAndTypeIndex()])).getDescriptorIndex()]);
//            }else if(className.equals("InterfaceMethodref")){
//                builder.append(String.format("%-15s","#"+((InterfaceMethodrefInfo)poolObjs[i]).getClassIndex()+".#"+((InterfaceMethodrefInfo)poolObjs[i]).getNameAndTypeIndex()));
//                builder.append("\t\t// ");
//
//                builder.append(poolObjs[((ClassInfo)poolObjs[((InterfaceMethodrefInfo)poolObjs[i]).getClassIndex()]).getNameIndex()]+".");
//                builder.append(poolObjs[((NameAndTypeInfo)(poolObjs[((InterfaceMethodrefInfo)poolObjs[i]).getNameAndTypeIndex()])).getNameIndex()]+".");
//                builder.append(poolObjs[((NameAndTypeInfo)(poolObjs[((InterfaceMethodrefInfo)poolObjs[i]).getNameAndTypeIndex()])).getDescriptorIndex()]);
//            }else if(className.equals("InvokeDynamic")){
//                builder.append(String.format("%-15s","#"+((InvokeDynamicInfo)poolObjs[i]).getNameAndTypeIndex()));
//                builder.append("\t\t// ");
//
//                builder.append(poolObjs[((NameAndTypeInfo)(poolObjs[((InvokeDynamicInfo)poolObjs[i]).getNameAndTypeIndex()])).getNameIndex()]+".");
//                builder.append(poolObjs[((NameAndTypeInfo)(poolObjs[((InvokeDynamicInfo)poolObjs[i]).getNameAndTypeIndex()])).getDescriptorIndex()]);
//            }
//            else if(className.equals("NameAndType")){
//                builder.append(String.format("%-15s","#"+((NameAndTypeInfo)poolObjs[i]).getNameIndex()+":#"+((NameAndTypeInfo)poolObjs[i]).getDescriptorIndex()));
//                builder.append("\t\t// ");
//
//                builder.append(poolObjs[((NameAndTypeInfo)poolObjs[i]).getNameIndex()]+":");
//                builder.append(poolObjs[((NameAndTypeInfo)poolObjs[i]).getDescriptorIndex()]);
//            }else if(className.equals("MethodHandle")){
//                builder.append(String.format("%-15s","#"+((MethodHandleInfo)poolObjs[i]).getReferenceIndex()));
//                builder.append("\t\t// ");
//
//                builder.append(poolObjs[((MethodHandleInfo)poolObjs[i]).getReferenceIndex()]);
//            }else if(className.equals("MethodType")){
//                builder.append(String.format("%-15s","#"+((MethodTypeInfo)poolObjs[i]).getDescriptorIndex()));
//                builder.append("\t\t// ");
//
//                builder.append(poolObjs[((MethodTypeInfo)poolObjs[i]).getDescriptorIndex()]);
//            }else if(className.equals("Utf8")||"Long,Double,Integer,Float".contains(className)){
//                builder.append(String.format("%-15s",poolObjs[i].toString()));
//            }else if(className.equals("String")){
//                builder.append(String.format("%-15s","#"+((StringInfo)poolObjs[i]).getStringIndex()));
//                builder.append("\t\t// ");
//
//                builder.append(poolObjs[((StringInfo)poolObjs[i]).getStringIndex()]);
//            }else if(className.equals("Class")){
//                builder.append(String.format("%-15s","#"+((ClassInfo)poolObjs[i]).getNameIndex()));
//                builder.append("\t\t// ");
//
//                builder.append(poolObjs[((ClassInfo)poolObjs[i]).getNameIndex()]);
//            }
//            builder.append("\n");
//        }
        return builder.toString();
    }

    public int readAccessFlag() throws IOException {
        int len=dis.read(buf,0,2);
//        if(len!=2)
//            throw new IOException("Wrong Access Flag");
        readLen+=len;
        return (buf[0]&0xff)<<8|(buf[1]&0xff);
    }

    public int readClassIndex() throws IOException {
        int len=dis.read(buf,0,2);
        readLen+=len;

        return (buf[0]&0xff)<<8|(buf[1]&0xff);
    }


    public String readInterfaces()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;

        int interfaceCount=(buf[0]&0xff)<<8|(buf[1]&0xff);
        if(interfaceCount==0)
            return "";

        StringBuilder builder=new StringBuilder();
        while(interfaceCount>0){
            builder.append(poolObjs[((ClassInfo)poolObjs[readClassIndex()]).getNameIndex()].toString());
            if(interfaceCount>1)
                builder.append(",");
            interfaceCount--;
        }
        return builder.toString();
    }


    //-------------read field-----------------------------

    public FieldInfo[] readField()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;

        int fieldCount=(buf[0]&0xff)<<8|(buf[1]&0xff);

//        System.out.println("fieldcount "+fieldCount);

        FieldInfo[] fieldInfos=new FieldInfo[fieldCount];
        for(int i=0;i<fieldCount;i++)
            fieldInfos[i]=readFieldItem();

        return fieldInfos;
    }

    public FieldInfo readFieldItem()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;
        int accessVal=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int nameIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int descriptorIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len= dis.read(buf,0,2);
        readLen+=len;

        int attrCount=(buf[0]&0xff)<<8|(buf[1]&0xff);

//        System.err.println(fieldFlagString(accessVal)+" "+typeMapping(poolObjs[descriptorIndex].toString())+" "+poolObjs[nameIndex]+", attrCount "+attrCount);

        BaseAttribute[] attrs=new BaseAttribute[attrCount];
        for(int i=0;i<attrCount;i++)
            attrs[i]=readAttribute();

        FieldInfo fieldInfo=new FieldInfo();
        fieldInfo.setAccessFlag(accessVal);
        fieldInfo.setNameIndex(nameIndex);
        fieldInfo.setDescriptorIndex(descriptorIndex);
        fieldInfo.setAttributes(attrs);

        return fieldInfo;
    }


    //-------------read attribute-------------------------
    /**
     * All attributes' nameIndex will be pre-readed in method <b>readFieldItemAttributes</b>
     * @return
     * @throws IOException
     */
    public BaseAttribute readAttribute()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;

        int nameIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);


        String attrName=poolObjs[nameIndex].toString();

//        System.err.println("AttrName:[ "+attrName+" ]");

        if(attrName.equals("Signature")){
            Signature signature=readSignature();
            signature.setNameIndex(nameIndex);

//            System.out.println(signature.toString(poolObjs));

            return signature;
        }else if(attrName.equals("Deprecated")){
            Deprecated deprecated=readDeprecated();
            deprecated.setNameIndex(nameIndex);

//            System.out.println(deprecated.toString(poolObjs));
            return deprecated;
        }else if(attrName.equals("RuntimeVisibleAnnotations")||attrName.equals("RuntimeInvisibleAnnotations")){
//            System.out.print("Before read RuntimeVisibleAnnotation. (nameIndex has already been read)");
//            System.out.println("readLen "+readLen);

            RuntimeVisibleAnnotations runtimeVisibleAnnotations=readRuntimeVisibleAnnotations();
            runtimeVisibleAnnotations.setNameIndex(nameIndex);
//            System.out.println(runtimeVisibleAnnotations.getLength());
//            for(Annotation annotation:runtimeVisibleAnnotations.getAnnotations())
//                System.out.println(annotation.toString(poolObjs));
            if(attrName.equals("RuntimeInvisibleAnnotations"))
                runtimeVisibleAnnotations.setVisible(false);

            return runtimeVisibleAnnotations;
        }else if(attrName.equals("Synthetic")){
            Synthetic synthetic=readSynthetic();
            synthetic.setNameIndex(nameIndex);

            return synthetic;
        }else if(attrName.equals("ConstantValue")){
            ConstantValue constantValue=readConstantValue();
            constantValue.setNameIndex(nameIndex);

            return constantValue;
        }else if(attrName.equals("Code")){
//            System.err.println("Now pos > "+readLen);
            Code code=readCode();
            code.setNameIndex(nameIndex);
//            System.out.println("Attr [code] length "+code.getCode().length);
//            System.out.println(Arrays.toString(code.getCode()));
//            System.out.println(code.getAttributes()[0]);
            return code;
        }else if(attrName.equals("LineNumberTable")){
            LineNumberTable lineNumberTable=readLineNumberTable();
            lineNumberTable.setNameIndex(nameIndex);

            return lineNumberTable;
        }else if(attrName.equals("SourceFile")){
            SourceFile sourceFile=readSourceFile();
            sourceFile.setNameIndex(nameIndex);

            return sourceFile;
        }else if(attrName.equals("Exceptions")){
            Exceptions exceptions=readExceptions();
            exceptions.setNameIndex(nameIndex);

            return exceptions;
        }else if(attrName.equals("InnerClasses")){
            InnerClasses innerClasses=readInnerClasses();
            innerClasses.setNameIndex(nameIndex);

            return innerClasses;
        }else if(attrName.equals("EnclosingMethod")){
            EnclosingMethod enclosingMethod=readEnclosingMethod();
            enclosingMethod.setNameIndex(nameIndex);

            return enclosingMethod;
        }else if(attrName.equals("LocalVariableTable")){
            LocalVariableTable localVariableTable=readLocalVariableTable();
            localVariableTable.setNameIndex(nameIndex);

            return localVariableTable;
        }else if(attrName.equals("LocalVariableTypeTable")){
            LocalVariableTypeTable localVariableTypeTable=readLocalVariableTypeTable();
            localVariableTypeTable.setNameIndex(nameIndex);

            return localVariableTypeTable;
        }else if(attrName.equals("RuntimeVisibleParameterAnnotations")||attrName.equals("RuntimeInvisibleParameterAnnotations")){
            RuntimeVisibleParameterAnnotations runtimeVisibleParameterAnnotations=readRuntimeVisibleParameterAnnotations();
            runtimeVisibleParameterAnnotations.setNameIndex(nameIndex);

            if(attrName.equals("RuntimeInvisibleParameterAnnotations"))
                runtimeVisibleParameterAnnotations.setVisible(false);

            return runtimeVisibleParameterAnnotations;
        }else if(attrName.equals("StackMapTable")){
            StackMapTable stackMapTable=readStackMapTable();
            stackMapTable.setNameIndex(nameIndex);

            return stackMapTable;
        }else if(attrName.equals("RuntimeVisibleTypeAnnotations")||attrName.equals("RuntimeInvisibleTypeAnnotations")){
            RuntimeVisibleTypeAnnotations runtimeVisibleTypeAnnotations=readRuntimeVisibleTypeAnnotations();
            if(attrName.equals("RuntimeVisibleTypeAnnotations"))
                runtimeVisibleTypeAnnotations.setVisible(false);

            runtimeVisibleTypeAnnotations.setNameIndex(nameIndex);

            return runtimeVisibleTypeAnnotations;
        }else if(attrName.equals("AnnotationDefault")){
            AnnotationDefault annotationDefault=readAnnotationDefault();
            annotationDefault.setNameIndex(nameIndex);
            return annotationDefault;
        }else if(attrName.equals("BootstrapMethods")){
            BootstrapMethods bootstrapMethods=readBootstrapMethods();
            bootstrapMethods.setNameIndex(nameIndex);
            return bootstrapMethods;
        }else if(attrName.equals("MethodParameters")){
            MethodParameters methodParameters=readMethodParameters();
            methodParameters.setNameIndex(nameIndex);
            return methodParameters;
        }
        return null;
    }


    //-----------this part includes the functions used to read attribute tables



    //the Attribute-table's nameIndex will be initialled at the method <b>readAttribute()</b>
    public Signature readSignature()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;

        int length=buf[3]&0xff;
        if(length!=2){
            System.err.println("Signature Length Warning...");
        }

        len=dis.read(buf,0,2);
        readLen+=len;

        int signatureIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        Signature signature=new Signature();
        signature.setLength(length);
        signature.setSignatureIndex(signatureIndex);

        return signature;
    }

    public Deprecated readDeprecated()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;

        int length=(buf[3]&0xff);
        if(length!=0)
            System.err.println("Deprecated Length Warning...");

        Deprecated deprecated=new Deprecated();
        deprecated.setLength(0);
        return deprecated;
    }

    public RuntimeVisibleAnnotations readRuntimeVisibleAnnotations()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;

        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;

        int annotationNum=(buf[0]&0xff)<<8|(buf[1]&0xff);

        Annotation[] annotations=new Annotation[annotationNum];
        for(int i=0;i<annotationNum;i++)
            annotations[i]=readAnnotation();

        RuntimeVisibleAnnotations runtimeVisibleAnnotations=new RuntimeVisibleAnnotations();
        runtimeVisibleAnnotations.setLength(length);
//        runtimeVisibleAnnotations.setNameIndex(na);
        runtimeVisibleAnnotations.setAnnotations(annotations);

        return runtimeVisibleAnnotations;
    }

    public Annotation readAnnotation()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;

        int typeIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int pairsCount=(buf[0]&0xff)<<8|(buf[1]&0xff);

        ElementValuePair[] elementValuePairs=new ElementValuePair[pairsCount];
        for(int i=0;i<pairsCount;i++)
            elementValuePairs[i]=readElementValuePair();

        Annotation annotation=new Annotation();
        annotation.setTypeIndex(typeIndex);
        annotation.setPairs(elementValuePairs);

        return annotation;
    }

    public ElementValuePair readElementValuePair()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;

        int nameIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);
        ElementValue elementValue=readElementValue();

        ElementValuePair elementValuePair=new ElementValuePair();
        elementValuePair.setNameIndex(nameIndex);
        elementValuePair.setElementValue(elementValue);

        return elementValuePair;
    }

    public ElementValue readElementValue()throws IOException{
        int len=dis.read(buf,0,1);
        readLen+=len;

        ElementValue elementValue=new ElementValue();
        char tag= (char) buf[0];
        if("BCDFIJSZs".contains(String.valueOf(tag))){
            len=dis.read(buf,0,2);
            readLen+=len;

            int constValueIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

            elementValue.setConstValueIndex(constValueIndex);
            return elementValue;
        }else if(tag=='e'){
            len=dis.read(buf,0,2);
            readLen+=len;

            int typeNameIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

            len=dis.read(buf,0,2);
            readLen+=len;
            int constNameValue=(buf[0]&0xff)<<8|(buf[1]&0xff);

            ElementValue.EnumConstValue enumConstValue=new ElementValue.EnumConstValue();
            enumConstValue.constNameIndex=constNameValue;
            enumConstValue.typeNameIndex=typeNameIndex;

            elementValue.setEnumConstValue(enumConstValue);
            return elementValue;
        }else if(tag=='c'){
            len=dis.read(buf,0,2);
            readLen+=len;

            int classInfoIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);
            elementValue.setClassInfoIndex(classInfoIndex);
            return elementValue;
        }else if(tag=='@'){
            elementValue.setAnnotationValue(readAnnotation());
            return elementValue;
        }else if(tag=='['){
            len=dis.read(buf,0,2);
            readLen+=len;

            int numValues=(buf[0]&0xff)<<8|(buf[1]&0xff);
            ElementValue.ArrayValue values=new ElementValue.ArrayValue();
            values.numValues=numValues;
            values.elementValues=new ElementValue[numValues];

            for(int i=0;i<numValues;i++)
                values.elementValues[i]=readElementValue();

            elementValue.setArrayValue(values);

            return elementValue;
        }
        return null;
    }

    public RuntimeVisibleAnnotations readRuntimeInvisibleAnnotations()throws IOException{
        return readRuntimeVisibleAnnotations();
    }

    public ConstantValue readConstantValue()throws IOException{
//        int len=dis.read(buf,0,2);
//        readLen+=len;
//
//        int nameIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        int len=dis.read(buf,0,4);
        readLen+=len;

        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;

        int constanValueIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        ConstantValue constantValue=new ConstantValue();

        constantValue.setLength(length);
        constantValue.setValueIndex(constanValueIndex);

        return constantValue;
    }

    public Synthetic readSynthetic()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;

        int length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        Synthetic synthetic=new Synthetic();
        synthetic.setLength(length);

        return synthetic;
    }

    public Code readCode()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xffL)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int maxStack=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int maxLocals=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,4);//I have to use an integer to represent codeLength
        readLen+=len;
        int codeLength=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

//        System.out.println("Now pos > "+readLen);
        byte[] codes=new byte[codeLength];
        len=dis.read(codes,0,codeLength);
        readLen+=len;

        len=dis.read(buf,0,2);
        readLen+=len;
        int exceptionCount=(buf[0]&0xff)<<8|(buf[1]&0xff);

        Code.ExceptionTable[] exceptionTables=new Code.ExceptionTable[exceptionCount];
        for(int i=0;i<exceptionCount;i++)
            exceptionTables[i]=readExceptionTable();

        len=dis.read(buf,0,2);
        readLen+=len;
        int attrCount=(buf[0]&0xff)<<8|(buf[1]&0xff);

        BaseAttribute[] attributes=new BaseAttribute[attrCount];
        for(int i=0;i<attrCount;i++)
            attributes[i]=readAttribute();

        Code code=new Code();
        code.setLength(length);
        code.setMaxStack(maxStack);
        code.setMaxLocals(maxLocals);
//        code.setCodeLength(codeLength);
        code.setCode(codes);
        code.setAttributes(attributes);

        return code;
    }

    public Code.ExceptionTable readExceptionTable()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;
        int startPc=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int endPc=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int handlerPc=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int catchType=(buf[0]&0xff)<<8|(buf[1]&0xff);

        Code.ExceptionTable exceptionTable=new Code.ExceptionTable();
        exceptionTable.setStartPc(startPc);
        exceptionTable.setEndPc(endPc);
        exceptionTable.setHandlerPc(handlerPc);
        exceptionTable.setCatchType(catchType);

        return exceptionTable;
    }

    public LineNumberTable readLineNumberTable()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int tableLength=(buf[0]&0xff)<<8|(buf[1]&0xff);

        LineNumberTable.LineNumberTableItem[] tableItems=new LineNumberTable.LineNumberTableItem[tableLength];
        for(int i=0;i<tableLength;i++)
            tableItems[i]=readLineNumberTableItem();

        LineNumberTable lineNumberTable=new LineNumberTable();
        lineNumberTable.setLength(length);
        lineNumberTable.setTableItems(tableItems);

        return lineNumberTable;
    }

    public LineNumberTable.LineNumberTableItem readLineNumberTableItem()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;
        int startPc=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int lineNumber=(buf[0]&0xff)<<8|(buf[1]&0xff);

        LineNumberTable.LineNumberTableItem item=new LineNumberTable.LineNumberTableItem();
        item.setStartPC(startPc);
        item.setLineNumber(lineNumber);

        return item;
    }

    public SourceFile readSourceFile()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int sourceFileIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        SourceFile sourceFile=new SourceFile();
        sourceFile.setLength(length);
        sourceFile.setSourceFileIndex(sourceFileIndex);

        return sourceFile;
    }

    public Exceptions readExceptions()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int exceptionsCount=(buf[0]&0xff)<<8|
                (buf[1]&0xff);

        int[] exceptionIndexes=new int[exceptionsCount];
        for(int i=0;i<exceptionsCount;i++){
            len=dis.read(buf,0,2);
            readLen+=len;
            exceptionIndexes[i]=(buf[0]&0xff)<<8|(buf[1]&0xff);
        }

        Exceptions exceptions=new Exceptions();
        exceptions.setLength(length);
        exceptions.setExceptionIndexes(exceptionIndexes);

        return exceptions;
    }

    public InnerClasses readInnerClasses()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int classesCount=(buf[0]&0xff)<<8|(buf[1]&0xff);

        InnerClasses.InnerClassItem[] classItems=new InnerClasses.InnerClassItem[classesCount];
        for(int i=0;i<classesCount;i++)
            classItems[i]=readInnerClassItem();

        InnerClasses innerClasses=new InnerClasses();
        innerClasses.setLength(length);
        innerClasses.setInnerClasses(classItems);

        return innerClasses;
    }

    public InnerClasses.InnerClassItem readInnerClassItem()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;
        int innerClassInfoIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int outerClassInfoIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int innerNameIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int innerClassAccessFlags=(buf[0]&0xff)<<8|(buf[1]&0xff);

        InnerClasses.InnerClassItem innerClassItem=new InnerClasses.InnerClassItem();
        innerClassItem.setInnerClassInfoIndex(innerClassInfoIndex);
        innerClassItem.setOuterClassInfoIndex(outerClassInfoIndex);
        innerClassItem.setInnerNameIndex(innerNameIndex);
        innerClassItem.setInnerClassAccessFlag(innerClassAccessFlags);

        return innerClassItem;
    }

    public EnclosingMethod readEnclosingMethod()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int classIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int methodIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        EnclosingMethod enclosingMethod=new EnclosingMethod();
        enclosingMethod.setLength(length);
        enclosingMethod.setClassIndex(classIndex);
        enclosingMethod.setMethodIndex(methodIndex);

        return enclosingMethod;
    }

    public LocalVariableTable readLocalVariableTable()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int tableLength=(buf[0]&0xff)<<8|(buf[1]&0xff);

        LocalVariableTable.LocalVariableTableItem[] tableItems=new LocalVariableTable.LocalVariableTableItem[tableLength];
        for(int i=0;i<tableLength;i++)
            tableItems[i]=readLocalVariableTableItem();

        LocalVariableTable localVariableTable=new LocalVariableTable();
        localVariableTable.setLength(length);
        localVariableTable.setLocalVariableTableItems(tableItems);

        return localVariableTable;
    }

    public LocalVariableTable.LocalVariableTableItem readLocalVariableTableItem()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;
        int startPc=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int length=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int nameIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int descriptoIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int index=(buf[0]&0xff)<<8|(buf[1]&0xff);

        LocalVariableTable.LocalVariableTableItem localVariableTableItem=new LocalVariableTable.LocalVariableTableItem();
        localVariableTableItem.setStartPc(startPc);
        localVariableTableItem.setLength(length);
        localVariableTableItem.setNameIndex(nameIndex);
        localVariableTableItem.setDescriptorIndex(descriptoIndex);
        localVariableTableItem.setIndex(index);

        return localVariableTableItem;
    }

    public LocalVariableTypeTable.LocalVariableTypeTableItem readLocalVariableTypeTableItem()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;
        int startPc=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int length=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int nameIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int signatureIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int index=(buf[0]&0xff)<<8|(buf[1]&0xff);

        LocalVariableTypeTable.LocalVariableTypeTableItem localVariableTypeTableItem=new LocalVariableTypeTable.LocalVariableTypeTableItem();
        localVariableTypeTableItem.setStartPc(startPc);
        localVariableTypeTableItem.setLength(length);
        localVariableTypeTableItem.setNameIndex(nameIndex);
        localVariableTypeTableItem.setSignatureIndex(signatureIndex);
        localVariableTypeTableItem.setIndex(index);

        return localVariableTypeTableItem;
    }

    public LocalVariableTypeTable readLocalVariableTypeTable()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int tableLength=(buf[0]&0xff)<<8|(buf[1]&0xff);

        LocalVariableTypeTable.LocalVariableTypeTableItem[] tableItems=new LocalVariableTypeTable.LocalVariableTypeTableItem[tableLength];
        for(int i=0;i<tableLength;i++)
            tableItems[i]=readLocalVariableTypeTableItem();

        LocalVariableTypeTable localVariableTypeTable=new LocalVariableTypeTable();
        localVariableTypeTable.setLength(length);
        localVariableTypeTable.setLocalVariableTableItems(tableItems);

        return localVariableTypeTable;
    }

    public RuntimeVisibleParameterAnnotations readRuntimeVisibleParameterAnnotations()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,1);
        readLen+=len;
        int numParameters=(buf[0]&0xff);

        ParameterAnnotation[] parameterAnnotations=new ParameterAnnotation[numParameters];
        for(int i=0;i<numParameters;i++)
            parameterAnnotations[i]=readParameterAnnotation();

        RuntimeVisibleParameterAnnotations runtimeVisibleParameterAnnotations=new RuntimeVisibleParameterAnnotations();
        runtimeVisibleParameterAnnotations.setLength(length);
        runtimeVisibleParameterAnnotations.setParameterAnnotations(parameterAnnotations);

        return runtimeVisibleParameterAnnotations;
    }

    public ParameterAnnotation readParameterAnnotation()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;
        int numAnnotataions=(buf[0]&0xff)<<8|(buf[1]&0xff);

        Annotation[] annotations=new Annotation[numAnnotataions];
        for(int i=0;i<numAnnotataions;i++)
            annotations[i]=readAnnotation();

        ParameterAnnotation parameterAnnotation=new ParameterAnnotation();
        parameterAnnotation.setAnnotations(annotations);

        return parameterAnnotation;
    }

    public StackMapTable readStackMapTable()throws IOException{
//        System.out.println("Before reading stackMapTable,pos "+readLen);
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);
        len=dis.read(buf,0,2);
        readLen+=len;
        int numEntries=(buf[0]&0xff)<<8|(buf[1]&0xff);
        StackMapTable.StackMapFrame[] entries=new StackMapTable.StackMapFrame[numEntries];
//        System.out.println("numEntries "+numEntries);
        for(int i=0;i<numEntries;i++)
            entries[i]=readStackMapFrame();

        StackMapTable stackMapTable=new StackMapTable();
        stackMapTable.setLength(length);
        stackMapTable.setEntries(entries);

//        System.out.println("After reading StackMapTable ,pos "+readLen);
        return stackMapTable;
    }

    public StackMapTable.StackMapFrame readStackMapFrame()throws IOException{
        int len=dis.read(buf,0,1);
        readLen+=len;
        char frameType= (char) (buf[0]&0xff);
        StackMapTable.StackMapFrame stackMapFrame=new StackMapTable.StackMapFrame();
        if(frameType<64){//set sameframe
            StackMapTable.StackMapFrame.SameFrame sameFrame=new StackMapTable.StackMapFrame.SameFrame();
            sameFrame.frameType=frameType;

            stackMapFrame.sameFrame=sameFrame;
        }else if(frameType<128){//set sameLocals1StackItemFrame
            StackMapTable.StackMapFrame.SameLocals1StackItemFrame stackItemFrame=new StackMapTable.StackMapFrame.SameLocals1StackItemFrame();
            StackMapTable.VerificationTypeInfo typeInfo=readVerificationTypeInfo();
            stackItemFrame.stack=new StackMapTable.VerificationTypeInfo[]{typeInfo};
            stackItemFrame.frameType=frameType;

            stackMapFrame.sameLocals1StackItemFrame=stackItemFrame;
        }else if(frameType==247){
            len=dis.read(buf,0,2);
            readLen+=len;
            int offsetDelta=(buf[0]&0xff)<<8|(buf[1]&0xff);

            StackMapTable.VerificationTypeInfo typeInfo=readVerificationTypeInfo();
            StackMapTable.StackMapFrame.SameLocals1StackItemFrameExtended itemFrameExtended=new StackMapTable.StackMapFrame.SameLocals1StackItemFrameExtended();
            itemFrameExtended.frameType=frameType;
            itemFrameExtended.offsetDelta=offsetDelta;
            itemFrameExtended.stack=new StackMapTable.VerificationTypeInfo[]{typeInfo};

            stackMapFrame.sameLocals1StackItemFrameExtended=itemFrameExtended;
        }else if(frameType>=248&&frameType<=250){
            len=dis.read(buf,0,2);
            readLen+=len;
            int offsetDelta=(buf[0]&0xff)<<8|(buf[1]&0xff);
            StackMapTable.StackMapFrame.ChopFrame chopFrame=new StackMapTable.StackMapFrame.ChopFrame();
            chopFrame.frameType=frameType;
            chopFrame.offsetDelta=offsetDelta;

            stackMapFrame.chopFrame=chopFrame;
        }else if(frameType==251){
            len=dis.read(buf,0,2);
            readLen+=len;
            int offsetDelta=(buf[0]&0xff)<<8|(buf[1]&0xff);
            StackMapTable.StackMapFrame.SameFrameExtended extended=new StackMapTable.StackMapFrame.SameFrameExtended();
            extended.frameType=frameType;
            extended.offsetDelta=offsetDelta;

            stackMapFrame.sameFrameExtended=extended;
        }else if(frameType>=252&&frameType<=254){
            len=dis.read(buf,0,2);
            readLen+=len;
            int offsetDelta=(buf[0]&0xff)<<8|(buf[1]&0xff);

            int numLocal=frameType-251;
            StackMapTable.VerificationTypeInfo[] typeInfos=new StackMapTable.VerificationTypeInfo[numLocal];
            for(int i=0;i<numLocal;i++)
                typeInfos[i]=readVerificationTypeInfo();

            StackMapTable.StackMapFrame.AppendFrame appendFrame=new StackMapTable.StackMapFrame.AppendFrame();
            appendFrame.frameType=frameType;
            appendFrame.offsetDelta=offsetDelta;
            appendFrame.locals=typeInfos;

            stackMapFrame.appendFrame=appendFrame;
        }else if(frameType==255){
            len=dis.read(buf,0,2);
            readLen+=len;
            int offsetDelta=(buf[0]&0xff)<<8|(buf[1]&0xff);

            len=dis.read(buf,0,2);
            readLen+=len;
            int numLocals=(buf[0]&0xff)<<8|(buf[1]&0xff);

            StackMapTable.VerificationTypeInfo[] typeInfoLocals=new StackMapTable.VerificationTypeInfo[numLocals];
            for(int i=0;i<numLocals;i++)
                typeInfoLocals[i]=readVerificationTypeInfo();

            len=dis.read(buf,0,2);
            readLen+=len;
            int numStackItems=(buf[0]&0xff)<<8|(buf[1]&0xff);
            StackMapTable.VerificationTypeInfo[] typeInfoStack=new StackMapTable.VerificationTypeInfo[numStackItems];
            for(int i=0;i<numStackItems;i++)
                typeInfoStack[i]=readVerificationTypeInfo();

            StackMapTable.StackMapFrame.FullFrame fullFrame=new StackMapTable.StackMapFrame.FullFrame();
            fullFrame.frameType=frameType;
            fullFrame.typeInfoLocals=typeInfoLocals;
            fullFrame.typeInfoStack=typeInfoStack;

            stackMapFrame.fullFrame=fullFrame;
        }
        return stackMapFrame;
    }

    public StackMapTable.VerificationTypeInfo readVerificationTypeInfo()throws IOException{
        int len=dis.read(buf,0,1);
        readLen+=len;
        char tag= (char) (buf[0]&0xff);
        StackMapTable.VerificationTypeInfo typeInfo=new StackMapTable.VerificationTypeInfo();

//        System.err.println("current tag [ "+(int)tag+" ]");
        switch (tag){
            case 0:
                typeInfo.topVariableInfoTag=tag;
                break;
            case 1:
                typeInfo.integerVariableInfoTag=tag;
                break;
            case 2:
                typeInfo.floatVariableInfoTag=tag;
                break;
            case 3:
                typeInfo.doubleVariableInfoTag=tag;
                break;
            case 4:
                typeInfo.longVariableInfoTag=tag;
                break;
            case 5:
                typeInfo.nullVariableInfoTag=tag;
                break;
            case 6:
                typeInfo.uninitializedThisVariableInfoTag=tag;
                break;
            case 7:
                typeInfo.objectVariableInfoTag=tag;
                len=dis.read(buf,0,2);
                readLen+=len;
                int cPoolIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);
                typeInfo.objectVariableInfoCPoolIndex=cPoolIndex;
                break;
            case 8:
                typeInfo.uninitializedVariableInfoTag=tag;
                len=dis.read(buf,0,2);
                readLen+=len;
                int offset=(buf[0]&0xff)<<8|(buf[1]&0xff);
                typeInfo.uninitializedVariableInfoOffset=offset;
                break;
            default:
                System.err.println("Unexpected tag");
                break;
        }

        return typeInfo;
    }

    public RuntimeVisibleTypeAnnotations readRuntimeVisibleTypeAnnotations()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int numAnnotations=(buf[0]&0xff)<<8|(buf[1]&0xff);

        TypeAnnotation[] annotations=new TypeAnnotation[numAnnotations];
        for(int i=0;i<numAnnotations;i++)
            annotations[i]=readTypeAnnotations();

        RuntimeVisibleTypeAnnotations runtimeVisibleTypeAnnotations=new RuntimeVisibleTypeAnnotations();
        runtimeVisibleTypeAnnotations.setAnnotations(annotations);
        runtimeVisibleTypeAnnotations.setLength(length);
        return runtimeVisibleTypeAnnotations;
    }
    public TypeAnnotation readTypeAnnotations()throws IOException{
        int len=dis.read(buf,0,1);
        readLen+=len;

        int targetType=buf[0]&0xff;
        TypeAnnotation typeAnnotation=new TypeAnnotation();
        switch (targetType){
            case 0x00:
            case 0x01:
                len=dis.read(buf,0,1);
                readLen+=len;

                int typeParameterIndex=buf[0]&0xff;
                typeAnnotation.setTypeParameterTarget_typeParameterIndex(typeParameterIndex);
                break;
            case 0x10:
                len=dis.read(buf,0,2);
                readLen+=len;

                int superTypeIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);
                typeAnnotation.setSuperTypeTarget_superTypeIndex(superTypeIndex);
                break;
            case 0x11:
            case 0x12:

                len=dis.read(buf,0,1);
                readLen+=len;
                typeParameterIndex=buf[0]&0xff;

                len=dis.read(buf,0,1);
                readLen+=len;
                int boundIndex=buf[0]&0xff;
                typeAnnotation.setTypeParameterBoundTarget_typeParameterIndex(typeParameterIndex);
                typeAnnotation.setTypeParameterBoundTarget_boundIndex(boundIndex);
                break;
            case 0x13:
            case 0x14:
            case 0x15:
                typeAnnotation.setEmptyTarget(true);
            case 0x16:
                len=dis.read(buf,0,1);
                readLen+=len;

                int formalParameterIndex=buf[0]&0xff;
                typeAnnotation.setMethodFormalParameterTarget_formalParameterIndex(formalParameterIndex);
                break;
            case 0x17:
                len=dis.read(buf,0,2);
                readLen+=len;

                int throwTypeIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);
                typeAnnotation.setThrowsTarget_throwsTypeIndex(throwTypeIndex);
                break;
            case 0x40:
            case 0x41:
                typeAnnotation.setLocalVarTarget(readLocalVarTarget());
                break;
            case 0x42:
                len=dis.read(buf,0,2);
                readLen+=len;
                int exceptionTableIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

                typeAnnotation.setCatchTarget_exceptionTableIndex(exceptionTableIndex);
                break;
            case 0x43:
            case 0x44:
            case 0x45:
            case 0x46:
                len=dis.read(buf,0,2);
                readLen+=len;
                int offset=(buf[0]&0xff)<<8|(buf[1]&0xff);
                typeAnnotation.setOffsetTarget_offset(offset);
                break;
            case 0x47:
            case 0x48:
            case 0x49:
            case 0x4A:
            case 0x4B:
                len=dis.read(buf,0,2);
                readLen+=len;
                offset=(buf[0]&0xff)<<8|(buf[1]&0xff);

                len=dis.read(buf,0,1);
                readLen+=len;
                int typeArgumentIndex=buf[0]&0xff;

                typeAnnotation.setTypeArgumentTarget_offset(offset);
                typeAnnotation.setTypeArgumentTarget_typeArgumentIndex(typeArgumentIndex);
                break;
            default:
                System.err.println("Unexpected target_type value");
        }

        return typeAnnotation;
    }


    private TypeAnnotation.LocalVarTarget readLocalVarTarget()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;
        int tableLength=(buf[0]&0xff)<<8|(buf[1]&0xff);

        TypeAnnotation.LocalVarTarget.Table[] tables=new TypeAnnotation.LocalVarTarget.Table[tableLength];
        for(int i=0;i<tableLength;i++)
            tables[i]=readTable();

        TypeAnnotation.LocalVarTarget localVarTarget=new TypeAnnotation.LocalVarTarget();
        localVarTarget.tables=tables;

        return localVarTarget;
    }

    private TypeAnnotation.LocalVarTarget.Table readTable()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;
        int startPc=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int length=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int index=(buf[0]&0xff)<<8|(buf[1]&0xff);

        TypeAnnotation.LocalVarTarget.Table table=new TypeAnnotation.LocalVarTarget.Table();
        table.startPc=startPc;
        table.length=length;
        table.index=index;

        return table;
    }

    public AnnotationDefault readAnnotationDefault()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        ElementValue defaultValue=readElementValue();

        AnnotationDefault annotationDefault=new AnnotationDefault();
        annotationDefault.setLength(length);

        return annotationDefault;
    }

    public BootstrapMethods readBootstrapMethods()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int numBootstrapMethods=(buf[0]&0xff)<<8|(buf[1]&0xff);

        BootstrapMethods.BootstrapMethod[] bootstrapMethodsItem=new BootstrapMethods.BootstrapMethod[numBootstrapMethods];
        for(int i=0;i<numBootstrapMethods;i++)
            bootstrapMethodsItem[i]=readBootstrapMethod();

        BootstrapMethods bootstrapMethods=new BootstrapMethods();
        bootstrapMethods.setLength(length);
        bootstrapMethods.setBootstrapMethods(bootstrapMethodsItem);

        return bootstrapMethods;
    }

    private BootstrapMethods.BootstrapMethod readBootstrapMethod()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;
        int bootstrapMethodref=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int numBootstrapArguments=(buf[0]&0xff)<<8|(buf[1]&0xff);

        int[] bootstrapArguments=new int[numBootstrapArguments];
        for(int i=0;i<numBootstrapArguments;i++){
            len=dis.read(buf,0,2);
            readLen+=len;

            bootstrapArguments[i]=(buf[0]&0xff)<<8|(buf[1]&0xff);
        }

        BootstrapMethods.BootstrapMethod bootstrapMethod=new BootstrapMethods.BootstrapMethod();
        bootstrapMethod.bootstrapMethodref=bootstrapMethodref;
        bootstrapMethod.bootstrapArguments=bootstrapArguments;

        return bootstrapMethod;
    }

    public MethodParameters readMethodParameters()throws IOException{
        int len=dis.read(buf,0,4);
        readLen+=len;
        long length=(buf[0]&0xff)<<24|
                (buf[1]&0xff)<<16|
                (buf[2]&0xff)<<8|
                (buf[3]&0xff);

        len=dis.read(buf,0,1);
        readLen+=len;
        int parametersCount=buf[0]&0xff;

        MethodParameters.Parameter[] parameters=new MethodParameters.Parameter[parametersCount];
        for(int i=0;i<parametersCount;i++)
            parameters[i]=readParameter();

        MethodParameters methodParameters=new MethodParameters();
        methodParameters.setLength(length);
        methodParameters.setParameters(parameters);

        return methodParameters;
    }

    private MethodParameters.Parameter readParameter()throws IOException{
        int len=dis.read(buf,0,2);
        readLen+=len;
        int nameIndex=(buf[0]&0xff)<<8|(buf[1]&0xff);

        len=dis.read(buf,0,2);
        readLen+=len;
        int accessFlags=(buf[0]&0xff)<<8|(buf[1]&0xff);

        MethodParameters.Parameter parameter=new MethodParameters.Parameter();
        parameter.nameIndex=nameIndex;
        parameter.accessFlags=accessFlags;

        return parameter;
    }

    private static final String[] codeHelpSymbols={
            "nop",          "aconst_null",  "iconst_ml",    "iconst_0",     "iconst_1",
            "iconst_2",     "iconst_3",     "iconst_4",     "iconst_5",     "lconst_0",
            "lconst_1",     "fconst_0",     "fconst_1",     "fconst_2",     "dconst_0",
            "dconst_1",     "bipush",       "sipush",       "ldc",          "ldc_w",
            "ldc2_w",       "iload",        "lload",        "fload",        "dload",
            "aload",        "iload_0",      "iload_1",      "iload_2",      "iload_3",
            "lload_0",      "lload_1",      "lload_2",      "lload_3",      "fload_0",
            "fload_1",      "fload_2",      "fload_3",      "dload_0",      "dload_1",
            "dload_2",      "dload_3",      "aload_0",      "aload_1",      "aload_2",
            "aload_3",      "iaload",       "laload",       "faload",       "daload",
            "aaload",       "baload",       "caload",       "saload",       "istore",
            "lstore",       "fstore",       "dstore",       "astore",       "istore_0",
            "istore_1",     "istore_2",     "istore_3",     "lstore_0",     "lstore_1",
            "lstore_2",     "lstore_3",     "fstore_0",     "fstore_1",     "fstore_2",
            "fstore_3",     "dstore_0",     "dstore_1",     "dstore_2",     "dstore_3",
            "astore_0",     "astore_1",     "astore_2",     "astore_3",     "iastore",
            "lastore",      "fastore",      "dastore",      "aastore",      "bastore",
            "castore",      "sastore",      "pop",          "pop2",         "dup",
            "dup_x1",       "dup_x2",       "dup2",         "dup2_x1",      "dup2_x2",
            "swap",         "iadd",         "ladd",         "fadd",         "dadd",
            "isub",         "lsub",         "fsub",         "dsub",         "imul",
            "lmul",         "fmul",         "dmul",         "idiv",         "ldiv",
            "fdiv",         "ddiv",         "irem",         "lrem",         "frem",
            "drem",         "ineg",         "lneg",         "fneg",         "dneg",
            "ishl",         "lshl",         "ishr",         "lshr",         "iushr",
            "lushr",        "iand",         "land",         "ior",          "lor",
            "ixor",         "lxor",         "iinc",         "i2l",          "i2f",
            "i2d",          "l2i",          "l2f",          "l2d",          "f2i",
            "f2l",          "f2d",          "d2i",          "d2l",          "d2f",
            "i2b",          "i2c",          "i2s",          "lcmp",         "fcmpl",
            "fcmpg",        "dcmpl",        "dcmpg",        "ifeq",         "ifne",
            "iflt",         "ifge",         "ifgt",         "ifle",         "if_icmpeq",
            "if_icmpne",    "if_icmplt",    "if_icmpge",    "if_icmpgt",    "if_icmple",
            "if_acmpeq",    "if_acmpne",    "goto",         "jsr",          "ret",
            "tableswitch",  "lookupswitch", "ireturn",      "lreturn",      "freturn",
            "dreturn",      "areturn",      "return",       "getstatic",    "putstatic",
            "getfield",     "putfield",     "invokevirtual","invokespecial","invokestatic",
            "invokeinterface","invokedynamic","new",        "newarray",     "anewarray",
            "arraylength",  "athrow",       "checkcast",    "instanceof",   "monitorenter",
            "monitorexit",  "wide",         "multianewarray","ifnull",      "ifnonnull",
            "goto_w",       "jsr_w",        "breakpoint",   null,           null,
            null,           null,           null,           null,           null,
            null,           null,           null,           null,           null,
            null,           null,           null,           null,           null,
            null,           null,           null,           null,           null,
            null,           null,           null,           null,           null,
            null,           null,           null,           null,           null,
            null,           null,           null,           null,           null,
            null,           null,           null,           null,           null,
            null,           null,           null,           null,           null,
            null,           null,           null,           null,           "impdep1",
            "impdep2"
    };

    //---------------------read method------------------------------
    public MethodInfo[] readMethod()throws IOException{
//        readMethodFlag=true;

        int len=dis.read(buf,0,2);
        readLen+=len;

        int methodCount=(buf[0]&0xff)<<8|(buf[1]&0xff);

//        System.out.println("methodCount "+methodCount);

        MethodInfo[] methodInfos=new MethodInfo[methodCount];
        for(int i=0;i<methodCount;i++){
//            System.out.println("Reading method "+(i+1));
            methodInfos[i]=readMethodItem();
//            System.out.println("methodName "+poolObjs[methodInfos[i].getNameIndex()]+",current pos "+readLen);
        }

        return methodInfos;
    }

    public MethodInfo readMethodItem()throws IOException{
        return new MethodInfo(readFieldItem());
    }



    //---------------------read class attribute---------------------

    public BaseAttribute[] readClassAttributes()throws IOException{
//        System.out.println("current pos "+readLen);


        int len=dis.read(buf,0,2);
        readLen+=len;

        int attrCount=(buf[0]&0xff)<<8|(buf[1]&0xff);
//        System.out.println("class attribute count "+attrCount);
        BaseAttribute[] classAttributes=new BaseAttribute[attrCount];
        for(int i=0;i<attrCount;i++)
            classAttributes[i]=readAttribute();

        return classAttributes;
    }

    private String classFlagString(int accessFlag){
        String flagString="";
        if((accessFlag&0x0001)==1)
            flagString+="public ";

        if((accessFlag&0x0010)==0x0010)
            flagString+="final ";

        if((accessFlag&0x0400)==0x0400)
            flagString+="abstract ";

        if((accessFlag&0x0200)==0x0200)
            flagString+="interface ";
        else if((accessFlag&0x4000)==0x4000)
            flagString+="annotation";
        else
            flagString+="class ";

        return flagString.trim();
    }

    private String fieldFlagString(int accessFlag){
        String flagString="";
        if((accessFlag&0x0001)==0x0001)
            flagString+="public ";
        else if((accessFlag&0x0002)==0x0002)
            flagString+="private ";
        else if((accessFlag&0x0004)==0x0004)
            flagString+="protected ";

        if((accessFlag&0x0010)==0x0010)
            flagString+="final ";
        else if((accessFlag&0x0080)==0x0080)
            flagString+="transient ";

        if((accessFlag&0x0008)==0x0008)
            flagString+="static ";
        if((accessFlag&0x4000)==0x4000)
            flagString+="enum ";

        return flagString.trim();
    }

    private String methodFlagString(int accessFlag){
        StringBuilder builder=new StringBuilder();
        if((accessFlag&0x0001)==0x0001)
            builder.append("public ");
        if((accessFlag&0x0002)==0x0002)
            builder.append("private ");
        if((accessFlag&0x0004)==0x0004)
            builder.append("protected ");
        if((accessFlag&0x0400)==0x0400)
            builder.append("abstract ");
        if((accessFlag&0x0008)==0x0008)
            builder.append("static ");
        if((accessFlag&0x0010)==0x0010)
            builder.append("final ");
        if((accessFlag&0x0020)==0x0020)
            builder.append("synchronized ");
        if((accessFlag&0x0040)==0x0040)
            builder.append("bridge ");
        if((accessFlag&0x0080)==0x0080)
            builder.append("varargs ");
        if((accessFlag&0x0100)==0x0100)
            builder.append("native ");
        if((accessFlag&0x0400)==0x0400)
            builder.append("abstract ");
        if((accessFlag&0x0800)==0x0800)
            builder.append("strict ");
        if((accessFlag&0x1000)==0x1000)
            builder.append("synthetic ");

        String flag=builder.toString().trim();
        return flag.length()==0? "default":flag;
    }

    private String typeMapping(String original){
        switch (original){
            case "B":
                return "byte";
            case "C":
                return "char";
            case "D":
                return "double";
            case "F":
                return "float";
            case "I":
                return "int";
            case "J":
                return "long";
            case "S":
                return "short";
            case "Z":
                return "boolean";
            case "V":
                return "void";
            default:
                if(original.startsWith("[")){
                    String tmp="";
                    int i=0;
                    while(original.charAt(i)=='['){
                        tmp+="[]";
                        i++;
                    }
                    return typeMapping(original.substring(i))+tmp;
                }else{
                    original=original.replace("/",".");
                    return original.substring(1,original.length()-1);
                }
        }
    }

    public String getConstantPool(int indent){
        return poolString(indent,poolObjs);
    }


    //----------------------------tackle with method------------------------

    private String fieldStereotypeName(FieldInfo fieldInfo){
        String accessFlagStr=fieldFlagString(fieldInfo.getAccessFlag());
        String name=poolObjs[fieldInfo.getNameIndex()].toString();
        String descriptor=poolObjs[fieldInfo.getDescriptorIndex()].toString();

        String signature="";
        for(BaseAttribute attribute:fieldInfo.getAttributes()){
            if(attribute instanceof Signature){
                signature=poolObjs[((Signature)attribute).getSignatureIndex()].toString();
                break;
            }
        }
        if(signature.length()>0) {
            StringBuilder builder = new StringBuilder();
            int k = 0;
            while (k < signature.length()) {
                if (signature.charAt(k) == 'L') {
                    k++;
                    while (signature.charAt(k) != ';'&&signature.charAt(k)!='<') {
                        if (signature.charAt(k) == '/')
                            builder.append(".");
                        else
                            builder.append(signature.charAt(k));
                        k++;
                    }
                    if(signature.charAt(k)==';')
                        k++;
                } else {
                    if(signature.charAt(k)!=';')
                        builder.append(signature.charAt(k));
                    k++;
                }
            }
            return accessFlagStr + " " + builder + " " + name;
        }
        return accessFlagStr+" "+typeMapping(descriptor)+" "+name;
    }
    public Map<Integer,String> fields(int indent,boolean privateIncluded){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+='\t';

        Map<Integer,String> fieldMessage=new HashMap<>();
        for(int i=0;i<fieldInfos.length;i++){
            FieldInfo fieldInfo=fieldInfos[i];
            String accessFlagStr=fieldFlagString(fieldInfo.getAccessFlag());
            if(!privateIncluded&&accessFlagStr.contains("private"))
                continue;

            fieldMessage.put(i,indentTabs+fieldStereotypeName(fieldInfo));
        }
        return fieldMessage;
    }

    public Map<Integer,String> methods(int indent,boolean privateIncluded){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+='\t';
        Map<Integer,String> methodMessage=new HashMap<>();
//        List<String> methodMessage=new ArrayList<>();
        for(int i=0;i<methodInfos.length;i++){
            MethodInfo methodInfo=methodInfos[i];

            String name=poolObjs[methodInfo.getNameIndex()].toString();
            if(name.equals("<init>"))
                name=className;

            String accessFlagStr=methodFlagString(methodInfo.getAccessFlag());

            if(accessFlagStr.contains("private")&&!privateIncluded)
                continue;

            StringBuilder descriptor=new StringBuilder(poolObjs[methodInfo.getDescriptorIndex()].toString().substring(1));

            String rtnType=typeMapping(descriptor.substring(descriptor.indexOf(")")+1));
            StringBuilder parameters= new StringBuilder();
            int pos=0;
            while(pos<descriptor.lastIndexOf(")")){
                String typeStr="";

                if("BCDFIJSZ".indexOf(descriptor.charAt(pos))!=-1)
                    typeStr=typeMapping(descriptor.charAt(pos++)+"");
                else if(descriptor.charAt(pos)=='['){
                    int count=0;
                    while(descriptor.charAt(pos)=='['){
                        count++;
                        pos++;
                    }
                    if("BCDFIJSZ".indexOf(descriptor.charAt(pos))!=-1){
                        typeStr=typeMapping(descriptor.charAt(pos++)+"");
                    }else{
                        int j=pos;
                        while(descriptor.charAt(j)!=';')j++;
                        String objType=descriptor.substring(pos,j+1);
                        typeStr=typeMapping(objType);
                        pos=j+1;
                    }

                    String tmp="";
                    for(int k=0;k<count;k++)
                        tmp+="[]";

                    typeStr+=tmp;
                }else{
                    int j=pos;
                    while(descriptor.charAt(j)!=';')j++;
                    String objType=descriptor.substring(pos,j+1);
                    typeStr=typeMapping(objType);
                    pos=j+1;
                }
                parameters.append(typeStr).append(", ");
            }

            if(parameters.length()>0)
                parameters = new StringBuilder(parameters.substring(0, parameters.length() - 2));

//            if(!accessFlagStr.contains("private") || privateIncluded){
//                methodMessage.add(accessFlagStr+" "+rtnType+" "+name+"("+parameters+")");
//            }
            methodMessage.put(i,indentTabs+accessFlagStr+" "+rtnType+" "+name+"("+parameters+")");
        }
        return methodMessage;
    }

    public Map<Integer,String> lineNumberTables(int indent,boolean privateIncluded){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+='\t';

        Map<Integer,String> numberTableMessage=new HashMap<>();
//        List<String> numberTables=new ArrayList<>();
        for(int i=0;i<methodInfos.length;i++){
            String accessStr=methodFlagString(methodInfos[i].getAccessFlag());
            if(!privateIncluded&&accessStr.contains("private"))
                continue;

            for(BaseAttribute attribute:methodInfos[i].getAttributes()){
                if(attribute instanceof Code){
                    Code code=(Code)attribute;
                    for(BaseAttribute subAttr:code.getAttributes()){
                        if(subAttr instanceof LineNumberTable){
                            StringBuilder builder=new StringBuilder(indentTabs+"LineNumberTable:\n");
                            LineNumberTable numberTable=(LineNumberTable)subAttr;
                            for(LineNumberTable.LineNumberTableItem item:numberTable.getTableItems())
                                builder.append(indentTabs+"\tline "+String.format("%2s:%-2s",item.getLineNumber(),item.getStartPC())+"\n");
//                            numberTables.add(builder.toString());
                            numberTableMessage.put(i,builder.toString());
                            break;
                        }
                    }
                }
            }
        }
        return numberTableMessage;
    }


    private int getArgSize(String signature){
        int count=0;
        int i=0;
        while(i<signature.length()&&!"BCDFIJSZL".contains(signature.charAt(i)+""))i++;
        while(i<signature.length()){
            while(i<signature.length()&&!"BCDFIJSZL".contains(signature.charAt(i)+""))i++;
            count++;
            if(i<signature.length()&&signature.charAt(i)=='L')
                while(i<signature.length()&&signature.charAt(i)!=';')
                    i++;
            i++;
        }
        return count;
    }
    /**getArgSize
     * 1)if_icmpge!
     * 2)ifne!
     * 3)goto
     * 4)if_icmpne!
     * 5)if_icmpeq!
     * -------------------------
     *readConstantPool
     * 6)if_icmple!
     * 7)
     * @param code
     * @param indent
     * @return
     */
    private String translateCode(int indent,byte[] code){
        String indentTabs="";
        for(int j=0;j<indent;j++)
            indentTabs+="\t";
        StringBuilder builder=new StringBuilder();
        int width=String.valueOf(code.length).length();
        int i=0;
        while(i<code.length){
            int byteCode=code[i]&0xff;
            String operationCode=codeHelpSymbols[byteCode];
            builder.append(indentTabs+String.format("%"+width+"s",i));
            builder.append(": ");
            builder.append(String.format("%-15s",operationCode));

//            if(operationCode==null){
//                System.err.println("[ "+methodName+" ]");
//                return builder.toString();
//            }
            switch (operationCode){
                //single byte command
                case "astore":
                case "iload":
                case "bipush":
                case "newarray":
                case "aload":
                case "istore":{
                    int singleByteVal = code[i + 1] & 0xff;
                    builder.append(String.format("%-10s", singleByteVal));
                    i += 1;
                    break;
                }
                //double bytes command
                case "if_icmpgt":
                case "ifne":
                case "ifle":
                case "if_icmpeq":
                case "sipush":
                case "if_icmplt":
                case "if_icmpge":
                case "goto":
                case "if_icmple":
                case "ifnonnull":
                case "ifeq":
                case "ifnull":
                case "if_icmpne":{
                    int doubleByteVal = (code[i + 1] & 0xff) << 8 | (code[i + 2] & 0xff);
                    if(operationCode.contains("if")||operationCode.equals("goto")){
                        doubleByteVal=(code[i+1]<<8)|code[i+2];
                        doubleByteVal+=i;
                    }
                    builder.append(String.format("%-10s", doubleByteVal));

                    i += 2;
                    break;
                }
                case "goto_w":{
                    int doubleDoubleVal=(code[i+1]&0xff)<<24|//offset
                            (code[i+2]&0xff)<<16|
                            (code[i+3]&0xff)<<8|
                            (code[i+4]&0xff);
                    doubleDoubleVal+=i;
                    builder.append(String.format("%-10s", doubleDoubleVal));
                    i+=4;
                    break;
                }
                //single byte index-command
                case "ldc": {
                    int constantValIndex = code[i + 1] & 0xff;
                    builder.append(String.format("%-10s", "#" + constantValIndex));

                    String objStr= poolObjs[constantValIndex].toString();
                    if(objStr.contains("//"))
                        objStr=objStr.substring(objStr.indexOf("//"));
                    else
                        objStr="//"+objStr;

                    builder.append("\t"+objStr);
                    i += 1;
                    break;
                }
                //double bytes index-command
                case "new":
                case "ldc_w":
                case "getstatic":
                case "ldc2_w":
                case "putstatic":
                case "getfield":
                case "invokespecial":
                case "invokestatic":
                case "checkcast":
                case "instanceof":
                case "putfield":
                case "anewarray":
                case "invokevirtual":
                case "invokeinterface":{
                    int constantValIndex = (code[i + 1] & 0xff) << 8 | (code[i + 2] & 0xff);
                    builder.append(String.format("%-10s", "#" + constantValIndex));
                    String objStr=poolObjs[constantValIndex].toString();
                    if (objStr.contains("//"))
                        objStr = objStr.substring(objStr.indexOf("//"));
                    else
                        objStr = "//" + objStr;

                    builder.append("\t" + objStr);
                    i += 2;
                    break;
                }
                case "iinc":{
                    int valA=code[i+1]&0xff;
                    int valB=code[i+2];
                    builder.append(String.format("%-10s",valA+","+valB));
                    i+=2;
                    break;
                }
                case "tableswitch":{
                    int initPos=i;
                    i+=4-(i%4);

                    int defaultOffset=(code[i]&0xff)<<24|
                            (code[i+1]&0xff)<<16|
                            (code[i+2]&0xff)<<8|
                            (code[i+3]&0xff);
                    i+=4;

                    int lowVal=(code[i]&0xff)<<24|
                            (code[i+1]&0xff)<<16|
                            (code[i+2]&0xff)<<8|
                            (code[i+3]&0xff);
                    i+=4;

                    int highVal=(code[i]&0xff)<<24|
                            (code[i+1]&0xff)<<16|
                            (code[i+2]&0xff)<<8|
                            (code[i+3]&0xff);
                    i+=4;

                    builder.append("{\n");
                    for(int k=lowVal;k<=highVal;k++){
                        int offset=(code[i]&0xff)<<24|
                                (code[i+1]&0xff)<<16|
                                (code[i+2]&0xff)<<8|
                                (code[i+3]&0xff);
                        i+=4;
                        builder.append(indentTabs+"\t"+String.format("%10s",k+"")+":"+(initPos+offset)+"\n");
                    }
                    i-=1;
                    builder.append(indentTabs+"\t"+String.format("%10s","default")+":"+(initPos+defaultOffset)+"\n");
                    builder.append(indentTabs+"}");
                    break;
                }
                case "lookupswitch":{
                    int initPos=i;
                    i+=4-(i%4);

                    int defaultOffset=(code[i]&0xff)<<24|
                            (code[i+1]&0xff)<<16|
                            (code[i+2]&0xff)<<8|
                            (code[i+3]&0xff);
                    i+=4;

                    int count=(code[i]&0xff)<<24|
                            (code[i+1]&0xff)<<16|
                            (code[i+2]&0xff)<<8|
                            (code[i+3]&0xff);

                    builder.append("{\n");
                    for(int k=0;k<count;k++){
                        i+=4;
                        int match=(code[i]&0xff)<<24|
                                (code[i+1]&0xff)<<16|
                                (code[i+2]&0xff)<<8|
                                (code[i+3]&0xff);
                        i+=4;
                        int offset=(code[i]&0xff)<<24|
                                (code[i+1]&0xff)<<16|
                                (code[i+2]&0xff)<<8|
                                (code[i+3]&0xff);
                        builder.append(indentTabs+"\t"+String.format("%12s",match+"")+":"+(initPos+offset)+"\n");
                    }
                    i+=3;
                    builder.append(indentTabs+"\t"+String.format("%12s","default")+(initPos+defaultOffset)+"\n");
                    builder.append(indentTabs+"}");
                    break;
                }
            }
            i+=1;
            builder.append("\n");
        }
        return builder.toString();
    }
    public Map<Integer,String> codes(int indent,boolean privateIncluded){
//        List<String> codeMessage=new ArrayList<>();
        Map<Integer,String> codeMessage=new HashMap<>();
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+="\t";

        for(int i=0;i<methodInfos.length;i++){
            String accessFlag=methodFlagString(methodInfos[i].getAccessFlag());
            if(!privateIncluded&&accessFlag.contains("private"))
                continue;
            for(BaseAttribute attribute:methodInfos[i].getAttributes()){
                if(attribute instanceof Code){
                    Code code=(Code)attribute;
                    StringBuilder builder=new StringBuilder(indentTabs+"Code:\n");

                    int argNum=accessFlag.contains("static")? 0:1;
                    String descriptorStr=poolObjs[methodInfos[i].getDescriptorIndex()].toString();
                    descriptorStr=descriptorStr.substring(0,descriptorStr.indexOf(")"));
                    argNum+=getArgSize(descriptorStr);

                    builder.append(indentTabs+"stack="+code.getMaxStack()+",locals="+code.getMaxLocals()+
                    ",args_size="+argNum+"\n");
//                    for(int j=0;j<code.getCode().length-1;j++)
//                        builder.append(Byte.toUnsignedInt(code.getCode()[j])+",");
//                    builder.append(Byte.toUnsignedInt(code.getCode()[code.getCode().length-1])+"\n");

//                    System.out.println("translating "+poolObjs[methodInfos[i].getNameIndex()]);
                    builder.append(translateCode(indent+1,code.getCode()));
//                    codeMessage.add(builder.toString());
                    codeMessage.put(i,builder.toString());
                    break;
                }
            }
        }

        return codeMessage;
    }

    public Map<Integer,String> localVariableTables(int indent,boolean privateIncluded){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+="\t";
        Map<Integer,String> lvTables=new HashMap<>();
        for(int i=0;i<methodInfos.length;i++){
            String accessStr=methodFlagString(methodInfos[i].getAccessFlag());
            if(!privateIncluded&&accessStr.contains("private"))
                continue;

            for(BaseAttribute attribute:methodInfos[i].getAttributes()){
                if(attribute instanceof Code){
//                    System.err.println("Find lvt...");
                    for(BaseAttribute subItem:((Code)attribute).getAttributes()){
                       if(subItem instanceof LocalVariableTable){
                           LocalVariableTable localVariableTable=(LocalVariableTable)subItem;
                           StringBuilder builder=new StringBuilder(indentTabs+"LocalVariableTables:\n");
                           builder.append(indentTabs+String.format("%-20s%-20s%-20s%-20s%-20s","Start","Length","Slot","Name","Signature")+"\n");
                           for(LocalVariableTable.LocalVariableTableItem item:localVariableTable.getLocalVariableTableItems()){
                               builder.append(indentTabs+String.format("%-20s%-20s%-20s%-20s%-20s",item.getStartPc()+"",item.getLength()+"",
                                       item.getIndex()+"",poolObjs[item.getNameIndex()].toString(),poolObjs[item.getDescriptorIndex()].toString())+"\n");
                           }
                           lvTables.put(i,builder.toString());
                           break;
                       }
                    }
                    break;
                }
            }
        }
        return lvTables;
    }


    private String verificationTypeInfoMapping(StackMapTable.VerificationTypeInfo[] typeInfos){
        String localStr="";
        for(int m=0;m<typeInfos.length;m++){
            if(typeInfos[m].topVariableInfoTag!=Character.MAX_VALUE)
                localStr+="top, ";
            else if(typeInfos[m].integerVariableInfoTag!=Character.MAX_VALUE)
                localStr+="int, ";
            else if(typeInfos[m].floatVariableInfoTag!=Character.MAX_VALUE)
                localStr+="float, ";
            else if(typeInfos[m].nullVariableInfoTag!=Character.MAX_VALUE)
                localStr+="null, ";
            else if(typeInfos[m].uninitializedThisVariableInfoTag!=Character.MAX_VALUE)
                localStr+="uninitializedThis, ";
            else if(typeInfos[m].objectVariableInfoTag!=Character.MAX_VALUE){
                String objStr=poolObjs[typeInfos[m].objectVariableInfoCPoolIndex].toString();
                if(objStr.contains("//"))
                    objStr=objStr.substring(objStr.indexOf("// ")+3);
                localStr+=objStr+", ";
            }
            else if(typeInfos[m].uninitializedVariableInfoTag!=Character.MAX_VALUE)
                localStr+="unitialized( "+typeInfos[m].uninitializedVariableInfoOffset+"), ";
            else if(typeInfos[m].longVariableInfoTag!=Character.MAX_VALUE)
                localStr+="long, ";
            else if(typeInfos[m].doubleVariableInfoTag!=Character.MAX_VALUE)
                localStr+="double, ";
        }

        if(localStr.length()>0){
//            localStr=localStr.substring(0,localStr.length()-2);
            localStr=localStr.substring(0,localStr.lastIndexOf(","));
        }

        return localStr;
    }
    public Map<Integer,String> stackMapTables(int indent,boolean privateIncluded){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+="\t";
        Map<Integer,String> smTables=new HashMap<>();
        for(int i=0;i<methodInfos.length;i++){
            String accessStr=methodFlagString(methodInfos[i].getAccessFlag());
            if(!privateIncluded&&accessStr.contains("private"))
                continue;
            for(BaseAttribute attribute:methodInfos[i].getAttributes()){
                if(attribute instanceof Code){
                    for(BaseAttribute item:((Code)attribute).getAttributes()){
                        if(item instanceof StackMapTable){
                            StackMapTable stackMapTable=(StackMapTable)item;
                            int numEntries=stackMapTable.getEntries().length;
                            StringBuilder builder=new StringBuilder(indentTabs+"StackMapTable: number_of_entries = "+numEntries+"\n");
                            StackMapTable.StackMapFrame[] frames=stackMapTable.getEntries();
                            for(int k=0;k<frames.length;k++){
                                if(frames[k].sameFrame!=null)
                                    builder.append(indentTabs+"frame_type = "+(int)frames[k].sameFrame.frameType+" /* same */\n");
                                else if(frames[k].sameLocals1StackItemFrame!=null){
                                    builder.append(indentTabs+"frame_type = "+(int)frames[k].sameLocals1StackItemFrame.frameType+" /* same_locals_1_stack_item_frame */\n");
                                }
                                else if(frames[k].sameLocals1StackItemFrameExtended!=null){
                                    builder.append(indentTabs+"frame_type = "+(247)+" /* same_locals_1_stack_item_frame_extended */\n");
                                    builder.append(indentTabs+"  offset_delta = "+frames[k].sameLocals1StackItemFrameExtended.offsetDelta+"\n");
                                }else if(frames[k].chopFrame!=null){
                                    builder.append(indentTabs+"frame_type = "+(int)frames[k].chopFrame.frameType+" /* chop */\n");
                                    builder.append(indentTabs+"  offset_delta = "+frames[k].chopFrame.offsetDelta+"\n");
                                }else if(frames[k].sameFrameExtended!=null){
                                    builder.append(indentTabs+"frame_type = "+(251)+" /* same_frame_extended */\n");
                                    builder.append(indentTabs+"  offset_delta = "+frames[k].sameFrameExtended.offsetDelta+"\n");
                                }else if(frames[k].appendFrame!=null){
                                    builder.append(indentTabs+"frame_type = "+(int)frames[k].appendFrame.frameType+" /* append */\n");
                                    builder.append(indentTabs+"  offset_delta = "+frames[k].appendFrame.offsetDelta+"\n");

                                    builder.append(indentTabs+"  locals = [");
                                    builder.append(verificationTypeInfoMapping(frames[k].appendFrame.locals)+"]\n");
                                }else if(frames[k].fullFrame!=null){
                                    builder.append(indentTabs+"frame_type = "+(255)+" /* full */\n");
                                    builder.append(indentTabs+" offset_delta = "+frames[k].fullFrame.offsetDelta+"\n");

                                    builder.append(indentTabs+" locals = [");
                                    builder.append(verificationTypeInfoMapping(frames[k].fullFrame.typeInfoLocals)+"" +
                                            "]\n");

                                    builder.append(indentTabs+" stack = [");
                                    builder.append(verificationTypeInfoMapping(frames[k].fullFrame.typeInfoStack)+"]\n");
                                }

                            }

                            smTables.put(i,builder.toString());
                        }
                    }
                    break;
                }
            }
        }
        return smTables;
    }

    public Map<Integer,String> methodDescriptors(int indent,boolean privateIncluded){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+="\t";
        Map<Integer,String> descriptorMessage=new HashMap<>();
        for(int i=0;i<methodInfos.length;i++){
            String accessStr=methodFlagString(methodInfos[i].getAccessFlag());
            if(!privateIncluded&&accessStr.contains("private"))
                continue;
            descriptorMessage.put(i,indentTabs+"descriptor: "+poolObjs[methodInfos[i].getDescriptorIndex()].toString()+"\n");
        }
        return descriptorMessage;
    }

    private String methodStereotype(int accessFlag){
        StringBuilder builder=new StringBuilder();
        if((accessFlag&0x0001)==0x0001)
            builder.append("ACC_PUBLIC, ");
        if((accessFlag&0x0002)==0x0002)
            builder.append("ACC_PRIVATE, ");
        if((accessFlag&0x0004)==0x0004)
            builder.append("ACC_PROTECTED, ");
        if((accessFlag&0x0008)==0x0008)
            builder.append("ACC_STATIC, ");
        if((accessFlag&0x0010)==0x0010)
            builder.append("ACC_FINAL, ");
        if((accessFlag&0x0020)==0x0020)
            builder.append("ACC_SYNCHRONIZED, ");
        if((accessFlag&0x0040)==0x0040)
            builder.append("ACC_BRIDGE, ");
        if((accessFlag&0x0080)==0x0080)
            builder.append("ACC_VARARGS, ");
        if((accessFlag&0x0100)==0x0100)
            builder.append("ACC_NATIVE, ");
        if((accessFlag&0x0400)==0x0400)
            builder.append("ACC_ABSTRACT, ");
        if((accessFlag&0x0800)==0x0800)
            builder.append("ACC_STRICTFP, ");
        if((accessFlag&0x1000)==0x1000)
            builder.append("ACC_SYNTHETIC, ");

        String stereotype=builder.toString();
        if(stereotype.length()==0)
            return "default";
        return stereotype.substring(0,stereotype.length()-2);
    }
    public Map<Integer,String> methodFlags(int indent,boolean privateIncluded){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+="\t";
        Map<Integer,String> flagMessage=new HashMap<>();
        for(int i=0;i<methodInfos.length;i++){
            String accessStr=methodFlagString(methodInfos[i].getAccessFlag());
            if(!privateIncluded&&accessStr.contains("private"))
                continue;
            flagMessage.put(i,indentTabs+"flags: "+methodStereotype(methodInfos[i].getAccessFlag())+"\n");
        }
        return flagMessage;
    }

    public Map<Integer,String> localVariableTypeTables(int indent,boolean privateIncluded){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+="\t";
        Map<Integer,String> lvtTables=new HashMap<>();
        for(int i=0;i<methodInfos.length;i++){
            String accessStr=methodFlagString(methodInfos[i].getAccessFlag());
            if(!privateIncluded&&accessStr.contains("private"))
                continue;

            for(BaseAttribute attribute:methodInfos[i].getAttributes()){
                if(attribute instanceof Code){
//                    System.err.println("Find lvt...");
                    for(BaseAttribute subItem:((Code)attribute).getAttributes()){
                        if(subItem instanceof LocalVariableTypeTable){
                            LocalVariableTypeTable localVariableTable=(LocalVariableTypeTable)subItem;
                            StringBuilder builder=new StringBuilder(indentTabs+"LocalVariableTypeTables:\n");
                            builder.append(indentTabs+String.format("%-20s%-20s%-20s%-20s%-20s","Start","Length","Slot","Name","Signature")+"\n");
                            for(LocalVariableTypeTable.LocalVariableTypeTableItem item:localVariableTable.getLocalVariableTableItems()){
                                builder.append(indentTabs+String.format("%-20s%-20s%-20s%-20s%-20s",item.getStartPc()+"",item.getLength()+"",
                                        item.getIndex()+"",poolObjs[item.getNameIndex()].toString(),poolObjs[item.getSignatureIndex()].toString())+"\n");
                            }
                            lvtTables.put(i,builder.toString());
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return lvtTables;
    }

    public Map<Integer,String> signatures(int indent,boolean privateIncluded){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+="\t";
        Map<Integer,String> signatureMessage=new HashMap<>();
        for(int i=0;i<methodInfos.length;i++){
            String accessStr=methodFlagString(methodInfos[i].getAccessFlag());
            if(accessStr.contains("private")&&!privateIncluded)
                continue;

            for(BaseAttribute attribute:methodInfos[i].getAttributes()){
                if(attribute instanceof Signature){
                    Signature signature=(Signature)attribute;
                    signatureMessage.put(i,indentTabs+"signature: #"+signature.getSignatureIndex()+"\t\t//"+poolObjs[signature.getSignatureIndex()]+"\n");
                }
            }
        }
        return signatureMessage;
    }
    //---------------------tackle with class----------------------------
    private String classStereotype(int accessFlag){
        StringBuilder builder=new StringBuilder();
        if((accessFlag&0x0001)==0x0001)
            builder.append("ACC_PUBLIC, ");
        if((accessFlag&0x0010)==0x0010)
            builder.append("ACC_FINAL, ");
        if((accessFlag&0x0020)==0x0020)
            builder.append("ACC_SUPER, ");
        if((accessFlag&0x0200)==0x0200)
            builder.append("ACC_INTERFACE, ");
        if((accessFlag&0x0400)==0x0400)
            builder.append("ACC_ABSTRACT, ");
        if((accessFlag&0x1000)==0x1000)
            builder.append("ACC_SYNTHETIC, ");
        if((accessFlag&0x2000)==0x2000)
            builder.append("ACC_ANNOTATION, ");
        if((accessFlag&0x4000)==0x4000)
            builder.append("ACC_ENUM, ");

        String stereotype=builder.toString();
        return stereotype.substring(0,stereotype.length()-2);
    }
    //---------------------tackle with field----------------------------
    public Map<Integer,String> fieldDescripors(int indent,boolean privateIncluded){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+="\t";
        Map<Integer,String> descriptorMessage=new HashMap<>();
        for(int i=0;i<fieldInfos.length;i++){
            String accessStr=fieldFlagString(fieldInfos[i].getAccessFlag());
            if(!privateIncluded&&accessStr.contains("private"))
                continue;
            descriptorMessage.put(i,indentTabs+"descriptor: "+poolObjs[fieldInfos[i].getDescriptorIndex()].toString()+"\n");
        }
        return descriptorMessage;
    }

    private String fieldStereotype(int accessFlag){
        StringBuilder builder=new StringBuilder();
        if((accessFlag&0x0001)==0x0001)
            builder.append("ACC_PUBLIC, ");
        if((accessFlag&0x0002)==0x0002)
            builder.append("ACC_PRIVATE, ");
        if((accessFlag&0x0004)==0x0004)
            builder.append("ACC_PROTECTED, ");
        if((accessFlag&0x0008)==0x0008)
            builder.append("ACC_STATIC, ");
        if((accessFlag&0x0010)==0x0010)
            builder.append("ACC_FINAL, ");
        if((accessFlag&0x0040)==0x0040)
            builder.append("ACC_VOLATILE, ");
        if((accessFlag&0x0080)==0x0080)
            builder.append("ACC_TRANSIENT, ");
        if((accessFlag&0x1000)==0x1000)
            builder.append("ACC_SYNTHETIC, ");
        if((accessFlag&0x4000)==0x4000)
            builder.append("ACC_ENUM, ");

        String stereotype=builder.toString();
        return stereotype.substring(0,stereotype.length()-2);
    }
    public Map<Integer,String> fieldFlags(int indent,boolean privateIncluded){
        String indentTabs="";
        for(int i=0;i<indent;i++)
            indentTabs+="\t";
        Map<Integer,String> flagMessage=new HashMap<>();
        for(int i=0;i<fieldInfos.length;i++){
            String accessStr=fieldFlagString(fieldInfos[i].getAccessFlag());
            if(accessStr.contains("private")&&!privateIncluded)
                continue;
            flagMessage.put(i,indentTabs+"flags: "+fieldStereotype(fieldInfos[i].getAccessFlag())+"\n");
        }
        return flagMessage;
    }

    private String innerClasses(){
        StringBuilder builder=new StringBuilder();
        for(BaseAttribute attribute:attributes){
            if(attribute instanceof InnerClasses){
                InnerClasses innerClasses=(InnerClasses)attribute;
                builder.append("InnerClasses:\n");
                for(InnerClasses.InnerClassItem item:innerClasses.getInnerClasses()){
                    if(item.getInnerNameIndex()!=0) {
                        builder.append("\t" + fieldFlagString(item.getInnerClassAccessFlag()) + " #");
                        builder.append(item.getInnerNameIndex() + "= #" + item.getInnerClassInfoIndex() + " of #");
                        builder.append(item.getOuterClassInfoIndex() + ";\t//");
                        builder.append(poolObjs[item.getInnerNameIndex()] + "=class ");
                        builder.append(poolObjs[((ClassInfo) poolObjs[item.getInnerClassInfoIndex()]).getNameIndex()] + " of class ");
                        builder.append(poolObjs[((ClassInfo) poolObjs[item.getOuterClassInfoIndex()]).getNameIndex()] + "\n");
                    }else{
                        builder.append("\t#"+item.getInnerClassInfoIndex()+";\t//class"+poolObjs[((ClassInfo) poolObjs[item.getInnerClassInfoIndex()]).getNameIndex()]+"\n");
                    }
                }
                break;
            }
        }
        return builder.toString();
    }

    private String sourceFile(){
        for(BaseAttribute attribute:attributes){
            if(attribute instanceof SourceFile){
                SourceFile sourceFile=(SourceFile)attribute;
                return poolObjs[sourceFile.getSourceFileIndex()].toString();
            }
        }
        return "";
    }
    private String classStereotypeName(){
        StringBuilder builder=new StringBuilder(className);
        for(BaseAttribute attribute:attributes){
            if(attribute instanceof Signature){
                Signature signature=(Signature)attribute;
                String sigStr=poolObjs[signature.getSignatureIndex()].toString();
                NestedString nestedString=analyzeSignature('<'+sigStr+'>');
                List<NestedString> superAndInterfaces=nestedString.getList();

                if(superAndInterfaces.size()==0)
                    return builder.toString();

                if(!accessFlag.contains("interface"))
                    builder.append(" extends "+superAndInterfaces.get(0));
                if(superAndInterfaces.size()>1){
                    builder.append(" implements ");
                    for(int i=1;i<superAndInterfaces.size()-1;i++)
                        builder.append(superAndInterfaces.get(i)+",");
                    builder.append(superAndInterfaces.get(superAndInterfaces.size()-1));
                }
                break;
            }
        }
        return builder.toString();
    }

    private static class NestedString{
        private String string;
        private List<NestedString> strings=new LinkedList<>();

        public NestedString(){}
        public NestedString(String string){
            this.string=string;
        }

        public boolean isString(){
            return this.strings.size()==0;
        }

        public String getString(){
            return string;
        }
        public void setstring(String string){
            this.string=string;
        }
        public List<NestedString> getList(){
            return strings;
        }
        public void add(NestedString nString){
            strings.add(nString);
        }
        @Override
        public String toString(){
            if(strings.size()==0)
                return string;
            StringBuilder builder=new StringBuilder();
            for(int i=0;i<strings.size()-1;i++)
                builder.append(strings.get(i).toString()).append(",");
            builder.append(strings.get(strings.size()-1));

            String nestedPart=builder.toString();
            if(string==null||string.length()==0)
                return nestedPart;
            return string+(nestedPart.length()>0? '<'+nestedPart+'>':"");
        }
    }

    private NestedString analyzeSignature(String signature){
        List<NestedString> stack=new ArrayList<>();
        int left=0;
        NestedString curr=null;
        for(int right=0;right<signature.length();right++){
            if(signature.charAt(right)=='<'){
                if(curr!=null)
                    stack.add(curr);
                if(left<right-1)
                    curr=new NestedString(signature.substring(left+1,right).replace("/","."));
                else
                    curr=new NestedString(signature.substring(left,right).replace("/","."));

                left=right+1;
            }
            else if(signature.charAt(right)==';'){
                if(signature.charAt(right-1)!='>')
                    curr.add(new NestedString(signature.substring(left+1,right).replace("/",".")));
                left=right+1;
            }else if(signature.charAt(right)=='>'){
                if(left<right)
                    curr.add(new NestedString(signature.substring(left+1,right).replace("/",".")));
                if(stack.size()>0){
                    NestedString upper=stack.remove(stack.size()-1);
                    upper.add(curr);
                    curr=upper;
                }
            }
        }
        return curr;
    }

    public String classHeadMessage(){
        StringBuilder builder=new StringBuilder();
        String fullName=accessFlag.replace("/",".")+" "+classStereotypeName();

        int width=fullName.length();
        for(int i=0;i<=Math.min(20,width);i++)
            builder.append("——");
        builder.append("\n");

        if(classFileName!=null){
            builder.append("ClassFile: "+classFileName+"\n");
            File classFile=new File(classFileName);
            builder.append("Last modified: "+ new SimpleDateFormat("YYYY-MM-dd").format(classFile.lastModified())+", "+classFile.length()+" bytes\n");
        }
        builder.append(fullName+"\n");

        String[] versions=version.split(":");
        builder.append("minor: "+versions[1]+"\n");
        builder.append("major: "+versions[0]+"\n");
        String classAccess="flags: "+classStereotype(accessVal);
        builder.append(classAccess+"\n");
        for(int i=0;i<=Math.min(20,width);i++)
            builder.append("——");
        builder.append("\n");
        return builder.toString();
    }
    public Map<Integer,String> fieldMessage(boolean privateIncluded){
        Map<Integer,String> message=new HashMap<>();

        Map<Integer,String> fields=fields(1,privateIncluded);
        Map<Integer,String> fieldFlags=fieldFlags(2,privateIncluded);
        Map<Integer,String> fieldDescriptors=fieldDescripors(2,privateIncluded);
        for(Map.Entry<Integer,String> entry:fields.entrySet()){
            StringBuilder builder=new StringBuilder();
            builder.append(entry.getValue()+";\n");
            builder.append(fieldDescriptors.get(entry.getKey()));
            builder.append(fieldFlags.get(entry.getKey())+"\n");
            message.put(entry.getKey(),builder.toString());
        }
        return message;
    }
    public Map<Integer,String> methodMessage(boolean privateIncluded){
        Map<Integer,String> message=new HashMap<>();
        Map<Integer,String> methods=methods(1,privateIncluded);
        Map<Integer,String> codes=codes(2,privateIncluded);
        Map<Integer,String> lineNumberTables=lineNumberTables(2,privateIncluded);
        Map<Integer,String> lvTables=localVariableTables(2,privateIncluded);
        Map<Integer,String> smTables=stackMapTables(2,privateIncluded);
        Map<Integer,String> methodDescripors=methodDescriptors(2,privateIncluded);
        Map<Integer,String> methodFlags=methodFlags(2,privateIncluded);
        Map<Integer,String> lvtTables=localVariableTypeTables(2,privateIncluded);
        Map<Integer,String> signatures=signatures(2,privateIncluded);
        for(Map.Entry<Integer,String> entry:methods.entrySet()){
            StringBuilder builder=new StringBuilder();
            builder.append(entry.getValue()+";\n");
            builder.append(methodDescripors.get(entry.getKey()));
            builder.append(methodFlags.get(entry.getKey()));
            builder.append(codes.getOrDefault(entry.getKey(),""));
            builder.append(lineNumberTables.getOrDefault(entry.getKey(),""));
            builder.append(lvTables.getOrDefault(entry.getKey(),""));
            builder.append(lvtTables.getOrDefault(entry.getKey(),""));
            builder.append(smTables.getOrDefault(entry.getKey(),""));
            builder.append(signatures.getOrDefault(entry.getKey(),"\n"));
            message.put(entry.getKey(),builder.toString());
        }
        return message;
    }
    public String attributeMessage(){
        StringBuilder builder=new StringBuilder();
        String sourceFile=sourceFile();
        if(sourceFile.length()>0){
            builder.append("SourceFile: "+sourceFile+"\n");
        }
        String innerClassesMessage=innerClasses();
        if(innerClassesMessage.length()>0)
            builder.append(innerClassesMessage);

        return builder.toString();
    }
//    public void verboseList(PrintStream ps,boolean privateIncluded){
//        String fullName=accessFlag.replace("/",".")+" "+classStereotypeName();
//
//        int width=fullName.length();
//        for(int i=0;i<=Math.min(20,width);i++)
//            ps.print("——");
//        ps.println();
//
//        if(classFileName!=null){
//            ps.println("ClassFile: "+classFileName);
//            File classFile=new File(classFileName);
//            ps.println("Last modified: "+ new SimpleDateFormat("YYYY-MM-dd").format(classFile.lastModified())+", "+classFile.length()+" bytes");
//        }
//        ps.println(fullName);
//
//        String[] versions=version.split(":");
//        ps.println("minor: "+versions[1]);
//        ps.println("major: "+versions[0]);
//        String classAccess="flags: "+classStereotype(accessVal);
//        ps.println(classAccess);
//        for(int i=0;i<=Math.min(20,width);i++)
//            ps.print("——");
//        ps.println();

        //constant pool
//        ps.println(poolString(1,poolObjs));
//
//        ps.println("{");
//        //field
//        Map<Integer,String> fields=fields(1,privateIncluded);
//        Map<Integer,String> fieldFlags=fieldFlags(2,privateIncluded);
//        Map<Integer,String> fieldDescriptors=fieldDescripors(2,privateIncluded);
//        for(Map.Entry<Integer,String> entry:fields.entrySet()){
//            ps.println(entry.getValue()+";");
//            ps.print(fieldDescriptors.get(entry.getKey()));
//            ps.println(fieldFlags.get(entry.getKey()));
//        }

        //method
//        Map<Integer,String> methods=methods(1,privateIncluded);
//        Map<Integer,String> codes=codes(2,privateIncluded);
//        Map<Integer,String> lineNumberTables=lineNumberTables(2,privateIncluded);
//        Map<Integer,String> lvTables=localVariableTables(2,privateIncluded);
//        Map<Integer,String> smTables=stackMapTables(2,privateIncluded);
//        Map<Integer,String> methodDescripors=methodDescriptors(2,privateIncluded);
//        Map<Integer,String> methodFlags=methodFlags(2,privateIncluded);
//        Map<Integer,String> lvtTables=localVariableTypeTables(2,privateIncluded);
//        Map<Integer,String> signatures=signatures(2,privateIncluded);
//        for(Map.Entry<Integer,String> entry:methods.entrySet()){
//            ps.println(entry.getValue()+";");
//            ps.print(methodDescripors.get(entry.getKey()));
//            ps.print(methodFlags.get(entry.getKey()));
//            ps.print(codes.getOrDefault(entry.getKey(),""));
//            ps.print(lineNumberTables.getOrDefault(entry.getKey(),""));
//            ps.print(lvTables.getOrDefault(entry.getKey(),""));
//            ps.print(lvtTables.getOrDefault(entry.getKey(),""));
//            ps.print(smTables.getOrDefault(entry.getKey(),""));
//            ps.println(signatures.getOrDefault(entry.getKey(),""));
//        }
//        ps.println("}");
//
//        String sourceFile=sourceFile();
//        if(sourceFile.length()>0){
//            ps.println("SourceFile: "+sourceFile);
//        }
//        String innerClassesMessage=innerClasses();
//        if(innerClassesMessage.length()>0)
//            ps.print(innerClassesMessage);
//    }

    public String verboseMessage(boolean privateIncluded){
        StringBuilder builder=new StringBuilder();
        builder.append(classHeadMessage());
        builder.append("Constant Pool:\n");
        builder.append(poolString(1,poolObjs));
        builder.append("{\n");
//        ps.print(classHeadMessage());
//        ps.print(poolString(1,poolObjs));
//        ps.println("{");
        for(Map.Entry<Integer,String> entry:fieldMessage(privateIncluded).entrySet())
//            ps.print(entry.getValue());
            builder.append(entry.getValue());
        for(Map.Entry<Integer,String> entry:methodMessage(privateIncluded).entrySet())
//            ps.print(entry.getValue());
            builder.append(entry.getValue());
//        ps.println("}");
//        ps.print(attributeMessage());
        builder.append("}\n");
        builder.append(attributeMessage());
        return builder.toString();
    }
    public static void main(String[] args) throws IOException {
        String classFileName="C:\\Users\\asus\\Desktop\\Test.class";
        LovyJavap javap=new LovyJavap(classFileName);
//        System.out.print(javap.verboseMessage(true));
        for(String methodInfo:javap.methods(0,true).values())
            System.out.println(methodInfo);

        System.out.println(javap.verboseMessage(true));
    }
}