package com.github.bluebridge.trash;

import com.github.bluebridge.pclient.NullPrinter;
import com.github.bluebridge.pclient.PrinterCellRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Daneel Yaitskov
 */
public class ScrollPanes {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        JFrame jf = new JFrame();
                        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        JPanel gui = new JPanel(new LimitedWidthLayout());

                        gui.setBorder(new EmptyBorder(3, 3, 3, 3));
                        gui.setBackground(Color.red);

                        JPanel p1 = new JPanel();
                        p1.setBackground(Color.GRAY);
                        p1.setMinimumSize(new Dimension(100,100));
                        p1.setMaximumSize(new Dimension(300, 300));
                        p1.setPreferredSize(new Dimension(140,140));
                        gui.add(p1);

                        JPanel p2 = new JPanel();
                        p2.setBackground(Color.GREEN);
                        p2.setMinimumSize(new Dimension(300,100));
                        p2.setMaximumSize(new Dimension(400, 300));
                        p2.setPreferredSize(new Dimension(300,140));
                        gui.add(p2);


                        jf.setContentPane(gui);
                        jf.pack();
                        jf.setVisible(true);
                    }
                });
    }

    public static JList getList() {
        JList list = new JList();
        // new Object[] { 1,2,3,4,5,6,7,8,9,10,12222222});
        list.setCellRenderer(new PrinterCellRenderer());
        DefaultListModel m = new DefaultListModel();
        m.addElement(new NullPrinter());
        m.addElement(new NullPrinter());
        m.addElement(new NullPrinter());
        m.addElement(new NullPrinter());
        m.addElement(new NullPrinter());
        list.setModel(m);
        return list;
    }
}
