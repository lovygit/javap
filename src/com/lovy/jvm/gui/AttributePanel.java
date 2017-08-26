package com.lovy.jvm.gui;

import com.lovy.jvm.bootstrap.LovyJavap;

import javax.swing.*;
import java.awt.*;

/**
 * Created by asus on 2017/7/10.
 */
public class AttributePanel extends JPanel {
    public AttributePanel(LovyJavap javap){
        String classAttributeMessage=javap.attributeMessage();
        JTextArea jTextArea=new JTextArea();
        jTextArea.setFont(new Font("DejaVu Sans Mono",Font.BOLD,13));
        jTextArea.setText(classAttributeMessage);

        setLayout(new GridLayout(1,1));
        add(new JScrollPane(jTextArea));
    }

}
