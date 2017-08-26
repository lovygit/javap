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
public class PoolPanel extends JPanel{
    private List<JLabel> recorder;
    private JPanel constantPanel;
    public PoolPanel(LovyJavap javap){
        String poolString=javap.getConstantPool(0);
        String[] constants=poolString.split("\n");

        constantPanel=new JPanel(new GridLayout(0,1));
        recorder=new ArrayList<>();

        for(String constant:constants){
            JLabel jLabel=new JLabel(constant);
            jLabel.setFont(new Font("DejaVu Sans Mono",Font.BOLD,12));
            recorder.add(jLabel);
            constantPanel.add(jLabel);
        }

        JPanel searchPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel=new JLabel("Search");
        searchLabel.setForeground(Color.RED);

        JTextField searchField=new JTextField("Input contant's name");
        searchField.setColumns(30);
        JLabel cancel=new JLabel(new ImageIcon(getClass().getResource("icon-remove.png")));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(cancel);

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Set<Integer> curr=new TreeSet<>();
                String keyword=searchField.getText();
                boolean find=false;
                int id=0;
                int firstIndex=-1;
                int count=0;
                for(JLabel label:recorder){
                    label.setForeground(Color.BLACK);
                    if(label.getText().toLowerCase().contains(keyword.toLowerCase())){
                        if(!find){
                            label.setForeground(Color.GREEN);
                            find=true;
                            firstIndex=id;
                        }else{
                            label.setForeground(Color.GRAY);
                            curr.add(id);
                        }
                        count++;
                    }
                    id++;
                }

                searchField.setText(keyword+", "+count+" available results");
                if(firstIndex!=-1) {
                    constantPanel.removeAll();
                    constantPanel.add(recorder.get(firstIndex));
                    for (int i : curr)
                        constantPanel.add(recorder.get(i));
                    for (int i = 0; i < recorder.size(); i++)
                        if (!curr.contains(i)&&i!=firstIndex)
                            constantPanel.add(recorder.get(i));
                    constantPanel.validate();
                }
            }
        });

        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchField.setText("");
                constantPanel.removeAll();
                for(JLabel label:recorder){
                    constantPanel.add(label);
                    label.setForeground(Color.BLACK);
                }
                constantPanel.validate();
            }
        });

        setLayout(new GridLayout(1,1));
        JPanel tmp=new JPanel(new BorderLayout());
        tmp.add(searchPanel,BorderLayout.NORTH);
        tmp.add(constantPanel);
        add(new JScrollPane(tmp));
    }
}
