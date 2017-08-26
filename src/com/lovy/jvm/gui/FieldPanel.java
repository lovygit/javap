package com.lovy.jvm.gui;

import com.lovy.jvm.bootstrap.LovyJavap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by asus on 2017/7/10.
 */
public class FieldPanel extends JPanel {
    public FieldPanel(LovyJavap javap){
        Map<Integer,String> fieldNames=javap.fields(0,true);
        Map<Integer,String> fieldDescriptors=javap.fieldDescripors(0,true);
        Map<Integer,String> fieldFlags=javap.fieldFlags(0,true);


        JPanel descriptor=new JPanel(new BorderLayout());
        JTextArea jtaDescriptor=new JTextArea("This is the area which shows the descriptor content...");
        jtaDescriptor.setFont(new Font("DejaVu Sans Mono",Font.BOLD,13));

        JLabel descriptorLable=new JLabel("Descriptor");
        descriptorLable.setForeground(Color.BLUE);
        descriptor.add(descriptorLable,BorderLayout.NORTH);
        descriptor.add(jtaDescriptor);

        JPanel flag=new JPanel(new BorderLayout());
        JTextArea jtaFlag=new JTextArea("This is the area which shows the flags content...");
        jtaFlag.setFont(new Font("DejaVu Sans Mono",Font.BOLD,13));
        JLabel flagLabel=new JLabel("Flag");
        flagLabel.setForeground(Color.BLUE);
        flag.add(flagLabel,BorderLayout.NORTH);
        flag.add(jtaFlag);

        JPanel right=new JPanel(new GridLayout(2,1));
        right.add(new JScrollPane(descriptor));
        right.add(new JScrollPane(flag));

        JPanel name=new JPanel(new GridLayout(0,1));
        List<JLabel> recorder=new ArrayList<>();
        for(Map.Entry<Integer,String> entry:fieldNames.entrySet()){
            JLabel label=new JLabel(entry.getValue());
            label.setFont(new Font("DejaVu Sans Mono",Font.BOLD,12));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for(JLabel item:recorder)
                        item.setForeground(Color.BLACK);
                    label.setForeground(Color.GREEN);

                    jtaDescriptor.setText(fieldDescriptors.get(entry.getKey()));
                    jtaFlag.setText(fieldFlags.get(entry.getKey()));
                }
            });
            recorder.add(label);
            name.add(label);
        }


        JPanel left=new JPanel(new BorderLayout());
        JPanel leftHeader=new JPanel(new BorderLayout());
//        leftHeader.add(new JLabel("Full Name"),BorderLayout.NORTH);

        JLabel jLabelSearch=new JLabel("Search");
        jLabelSearch.setForeground(Color.red);
        leftHeader.add(jLabelSearch,BorderLayout.WEST);

        JTextField searchField=new JTextField("Input field name");
        searchField.setColumns(20);
        leftHeader.add(searchField);

        searchField.addActionListener(e -> {
            Set<Integer> curr=new TreeSet<>();
            String keyword=searchField.getText();
            int id=0;
            boolean find=false;
            int firstIndex=-1;
            int count=0;
            for(JLabel label:recorder){
                label.setForeground(Color.BLACK);
                String[] parts=label.getText().split("\\s");
                String simpleFieldName=parts[parts.length-1];
                if(simpleFieldName.toLowerCase().contains(keyword.toLowerCase())){
                    if(!find) {
                        label.setForeground(Color.GREEN);
                        jtaDescriptor.setText(fieldDescriptors.get(id));
                        jtaFlag.setText(fieldFlags.get(id));
                        find = true;
                        firstIndex=id;
                    }else{
                        label.setForeground(Color.GRAY);
                        curr.add(id);
                    }
                    count++;
                }
                id++;
            }

            searchField.setText(keyword+" , "+count+" available results");
            if(firstIndex!=-1){
                name.removeAll();
                name.add(recorder.get(firstIndex));
                for(int i:curr)
                    name.add(recorder.get(i));
                for(int i=0;i<recorder.size();i++)
                    if(!curr.contains(i)&&i!=firstIndex)
                        name.add(recorder.get(i));
                name.validate();
            }
        });

        JLabel jLabelClear=new JLabel(new ImageIcon(getClass().getResource("icon-remove.png")));
        jLabelClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchField.setText("");
                name.removeAll();
                for(JLabel label:recorder){
                    label.setForeground(Color.BLACK);
                    name.add(label);
                }
                name.validate();
            }
        });
        leftHeader.add(jLabelClear,BorderLayout.EAST);

        left.add(leftHeader,BorderLayout.NORTH);
        left.add(name,BorderLayout.CENTER);

        setLayout(new GridLayout(1,2));
        add(new JScrollPane(left));
        add(right);
    }
}
