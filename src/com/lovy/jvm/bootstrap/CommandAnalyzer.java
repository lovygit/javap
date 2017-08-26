package com.lovy.jvm.bootstrap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dyard on 2017/7/6.
 */
public class CommandAnalyzer {
    public static void main(String[] args)throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new FileReader("c:/users/asus/desktop/javap.txt"));
        String lineContent="";
        Pattern pattern=Pattern.compile("^(\\d+): ((\\w|\\d|_)+)");
        List<Pair> pairs=new ArrayList<>();
        while((lineContent=bufferedReader.readLine())!=null){
            Matcher matcher=pattern.matcher(lineContent);
            if(matcher.find()){
//                System.out.println(lineContent);
                boolean isNumber=lineContent.split("\\s+").length==3;
                boolean isIndex=lineContent.contains("#");
                pairs.add(new Pair(Integer.valueOf(matcher.group(1)),matcher.group(2),isIndex,isNumber));
            }
        }
//        for(int i=0;i<pairs.size();i++)
//            System.out.println(pairs.get(i));


        Set<String> singleIndexCommand=new HashSet<>();
        Set<String> doubleIndexCommand=new HashSet<>();
        Set<String> singleByteCommand=new HashSet<>();
        Set<String> doubleByteCommand=new HashSet<>();
        for(int i=0;i<pairs.size();i++){
            if(pairs.get(i).isIndex){
//                if(i+1>=pairs.size()){
//                    System.err.println(pairs.get(i));
//                    continue;
//                }

                int byteLength=pairs.get(i+1).line-pairs.get(i).line-1;
                if(byteLength==1)
                    singleIndexCommand.add(pairs.get(i).command);
                else
                    doubleIndexCommand.add(pairs.get(i).command);
            }else if(pairs.get(i).isNumber){
//                if(i+1>=pairs.size()){
//                    System.err.println(pairs.get(i));
//                    continue;
//                }
                int byteLength=pairs.get(i+1).line-pairs.get(i).line-1;
                if(byteLength==1)
                    singleByteCommand.add(pairs.get(i).command);
                else
                    doubleByteCommand.add(pairs.get(i).command);
            }
        }
        System.out.println("----------single index commands-----------------");
        for(String command:singleIndexCommand){
            System.out.println("case \""+command+"\":");
        }

        System.out.println("----------double index commands-----------------");
        for(String command:doubleIndexCommand){
            System.out.println("case \""+command+"\":");
        }

        System.out.println("----------single value commands-----------------");
        for(String command:singleByteCommand)
            System.out.println("case \""+command+"\":");

        System.out.println("----------double value commands------------------");
        for(String command:doubleByteCommand)
            System.out.println("case \""+command+"\":");
    }
    public static class Pair{
        int line;
        String command;
        boolean isIndex;
        boolean isNumber;
        Pair(int line,String command,boolean isIndex,boolean isNumber){
            this.line=line;
            this.command=command;
            this.isIndex=isIndex;
            this.isNumber=isNumber;
        }

        @Override
        public String toString(){
            return line+": "+command;
        }
    }
}
