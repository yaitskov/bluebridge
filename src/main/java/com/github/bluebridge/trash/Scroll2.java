package com.github.bluebridge.trash;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Daneel Yaitskov
 */
public class Scroll2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame jf = new JFrame();
                JPanel gui = new JPanel(new BorderLayout(5, 5));
                gui.setBorder(new EmptyBorder(3, 3, 3, 3));
                gui.setBackground(Color.red);

                JList list = new JList(new Object[] { 1,2,3,4,5,6,7,8,9,10,12222222});
                gui.add(new JScrollPane(list));

                jf.setContentPane(gui);
                jf.pack();
                jf.setVisible(true);


            }
        });
    }
}
