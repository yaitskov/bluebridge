package com.github.bluebridge.trash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/6/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Test2.class);
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JFrame frame;
    private JPanel panel1;
    private JButton button1;
    private JProgressBar progressBar1;

    public Test2() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                button1 = new JButton("press me");

                progressBar1 = new JProgressBar(0,100);
                progressBar1.setValue(44);

                panel1 = new JPanel();

                panel1.add(button1);
                panel1.add(progressBar1);

                menuBar = new JMenuBar();
                menuFile = new JMenu("File");

                JMenuItem mitem = new JMenuItem();

                mitem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        button1.setText(button1.getText() + " hel");
                    }
                });
                mitem.setText("Change button text");

                menuFile.add(mitem);

                menuBar.add(menuFile);

                frame = new JFrame("Test1");
                frame.setContentPane(panel1);
                frame.setJMenuBar(menuBar);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }


    public static void main(String[] args) {
        Test2  t = new Test2();

    }
}
