package com.lovy.jvm.gui;

import com.lovy.jvm.bootstrap.LovyJavap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created by asus on 2017/7/10.
 */
public class MethodPanel extends JPanel {
    private int currIndex=0;
    public MethodPanel(LovyJavap javap){
        Map<Integer,String> methods=javap.methods(0,true);
        Map<Integer,String> codes=javap.codes(0,true);
        Map<Integer,String> lineNumberTables=javap.lineNumberTables(0,true);
        Map<Integer,String> lvTables=javap.localVariableTables(0,true);
        Map<Integer,String> smTables=javap.stackMapTables(0,true);
        Map<Integer,String> methodDescripors=javap.methodDescriptors(0,true);
        Map<Integer,String> methodFlags=javap.methodFlags(0,true);
        Map<Integer,String> lvtTables=javap.localVariableTypeTables(0,true);
        Map<Integer,String> signatures=javap.signatures(0,true);


        JPanel name=new JPanel(new GridLayout(0,1));
        java.util.List<JLabel> recorder=new ArrayList<>();
        for(Map.Entry<Integer,String> entry:methods.entrySet()){
            JLabel label=new JLabel(entry.getValue());
            label.setFont(new Font("DejaVu Sans Mono",Font.BOLD,12));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    currIndex=entry.getKey();
                    for(JLabel item:recorder)
                        item.setForeground(Color.BLACK);
                    label.setForeground(Color.green);
                }
            });
            recorder.add(label);
            name.add(label);
        }


        JPanel searchPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel=new JLabel("Search");
        searchLabel.setForeground(Color.red);
        JTextField searchContent=new JTextField("Input method name");
        searchContent.setColumns(30);
        JButton cancel=new JButton(new ImageIcon(getClass().getResource("icon-remove.png")));

        searchPanel.add(searchLabel);
        searchPanel.add(searchContent);
        searchPanel.add(cancel);

        searchContent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Set<Integer> curr=new TreeSet<>();
                String keyword=searchContent.getText();
                boolean find=false;
                int firstIndex=-1;
                int id=0;
                int count=0;
                for(JLabel label:recorder){
                    label.setForeground(Color.BLACK);
                    if(label.getText().toLowerCase().contains(keyword.toLowerCase())){
                        if(!find){
                            label.setForeground(Color.GREEN);
                            firstIndex=id;
                            find=true;
                            currIndex=id;
                        }else{
                            label.setForeground(Color.GRAY);
                            curr.add(id);
                        }
                        count++;
                    }
                    id++;
                }

                searchContent.setText(keyword+" , "+count+" available results");

                if(find){
                    name.removeAll();
                    name.add(recorder.get(firstIndex));
                    for(int i:curr)
                        name.add(recorder.get(i));

                    for(int i=0;i<recorder.size();i++)
                        if(!curr.contains(i)&&i!=firstIndex)
                            name.add(recorder.get(i));

                    name.validate();
                }
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchContent.setText("");
                name.removeAll();
                for(JLabel label:recorder){
                    label.setForeground(Color.BLACK);
                    name.add(label);
                }
                name.validate();
            }
        });

        JPanel menuPanel=new JPanel(new GridLayout(8,1));
        JButton codeButton=new JButton("Code");
        JButton lineNumberTableButton=new JButton("LineNumberTable");
        JButton localVariableTableButton=new JButton("LocalVariableTable");
        JButton stackMapTableButton=new JButton("StackMapTable");
        JButton descriptorButton=new JButton("Descriptor");
        JButton flagButton=new JButton("Flag");
        JButton localVariableTypeTableButton=new JButton("LocalVariableTypeTable");
        JButton signatureButton=new JButton("Signature");

        menuPanel.add(codeButton);
        menuPanel.add(lineNumberTableButton);
        menuPanel.add(localVariableTableButton);
        menuPanel.add(stackMapTableButton);
        menuPanel.add(descriptorButton);
        menuPanel.add(flagButton);
        menuPanel.add(localVariableTypeTableButton);
        menuPanel.add(signatureButton);



        codeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonHandler(codes,methods,"Code");
            }
        });

        lineNumberTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonHandler(lineNumberTables,methods,"LineNumberTable");
            }
        });

        localVariableTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonHandler(lvTables,methods,"LocalVariableTable");
            }
        });

        stackMapTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonHandler(smTables,methods,"StackMapTable");
            }
        });

        descriptorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonHandler(methodDescripors,methods,"Descriptor");
            }
        });

        flagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonHandler(methodFlags,methods,"Flag");
            }
        });

        localVariableTypeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonHandler(lvtTables,methods,"LocalVariableTypeTable");
            }
        });

        signatureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonHandler(signatures,methods,"Signature");
            }
        });

        JPanel tmp=new JPanel(new BorderLayout());

        tmp.add(searchPanel,BorderLayout.NORTH);
        tmp.add(new JScrollPane(name));
        tmp.add(menuPanel,BorderLayout.EAST);
        setLayout(new GridLayout(1,1));

        add(tmp);
    }

    private void buttonHandler(Map<Integer,String> message,Map<Integer,String> methods,String title){
        String item=message.get(currIndex);
        JFrame jFrame=new JFrame(title);
        JTextArea jtaCode=new JTextArea();
        jtaCode.setFont(new Font("DejaVu Sans Mono",Font.BOLD,12));
        jtaCode.setText("Method: "+methods.get(currIndex)+"\n"+item);
//        JPanel jpCode=new JPanel();
//        jpCode.add(jtaCode);
        jFrame.setLayout(new GridLayout(1,1));
        jFrame.add(new JScrollPane(jtaCode));
        jFrame.setSize(800,400);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
