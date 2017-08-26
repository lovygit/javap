package com.lovy.jvm.gui;

import com.lovy.jvm.bootstrap.LovyJavap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Created by asus on 2017/7/10.
 */
public class Menu extends JFrame implements ActionListener{
    private LovyJavap javap;
    private JButton poolButton;
    private JButton fieldButton;
    private JButton methodButton;
    private JButton attributeButton;

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==poolButton)
            createConstantPoolWindow();
        else if(e.getSource()==fieldButton)
            createFieldWindow();
        else if(e.getSource()==methodButton)
            createMethodWindow();
        else if(e.getSource()==attributeButton)
            createAttributeWindow();
    }

    public Menu(LovyJavap javap){
        this.javap=javap;

        JTextArea classIntroduction=new JTextArea();
        if(javap!=null)
            classIntroduction.setText(javap.verboseMessage(true));
        classIntroduction.setBackground(Color.LIGHT_GRAY);
        classIntroduction.setFont(new Font("DejaVu Sans Mono",Font.BOLD,13));

        JPanel domains=new JPanel(new FlowLayout(FlowLayout.LEFT));
        poolButton=new JButton("Constant Pool");
        fieldButton=new JButton("Fields");
        methodButton=new JButton("Methods");
        attributeButton=new JButton("Attributes");

        domains.add(poolButton);
        domains.add(fieldButton);
        domains.add(methodButton);
        domains.add(attributeButton);

        poolButton.addActionListener(this);
        fieldButton.addActionListener(this);
        methodButton.addActionListener(this);
        attributeButton.addActionListener(this);

        JPanel inputPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel inputLabel=new JLabel("Class File Name");
        inputLabel.setForeground(Color.red);

        JTextField inputField=new JTextField("Input a class file name");
        JLabel cancel=new JLabel(new ImageIcon(getClass().getResource("icon-remove.png")));
        inputField.setColumns(50);
        inputField.setFont(new Font("DejaVu Sans Mono",Font.BOLD,14));

        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(cancel);

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className=inputField.getText();
                File file=new File(className);
                if(file.exists()){
                    inputField.setForeground(Color.BLACK);
                    inputField.setText("Retrieving "+className);
                    Menu.this.javap=new LovyJavap(className);
                    classIntroduction.setText(Menu.this.javap.verboseMessage(true));
                }
            }
        });

        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inputField.setForeground(Color.BLACK);
                inputField.setText("");
                classIntroduction.setText("");
                Menu.this.javap=null;
            }
        });

        JPanel header=new JPanel(new GridLayout(2,1));
        header.add(inputPanel);
        header.add(domains);

        add(header,BorderLayout.NORTH);
        add(new JScrollPane(classIntroduction));
    }

    private void createFieldWindow(){
        JFrame jFrame=new JFrame("Field Window");
        if(javap!=null)
            jFrame.add(new FieldPanel(javap));
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setSize(900,400);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void createConstantPoolWindow(){
        JFrame jFrame=new JFrame("Constant Pool Window");
        if(javap!=null)
            jFrame.add(new PoolPanel(javap));
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setSize(900,400);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void createMethodWindow(){
        JFrame jFrame=new JFrame("Method Window");
        if(javap!=null)
            jFrame.add(new MethodPanel(javap));
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setSize(900,400);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void createAttributeWindow(){
        JFrame jFrame=new JFrame("Attribute Window");
        if(javap!=null)
            jFrame.add(new AttributePanel(javap));
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setSize(900,400);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    public static void main(String[] args){
//        String classFileName="c:/users/asus/desktop/LovyJavap.class";
//        LovyJavap javap=null;
        Menu menu=new Menu(null);
        menu.setTitle("LovyJavap");
        menu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menu.setSize(800,400);
        menu.setVisible(true);

    }
}
