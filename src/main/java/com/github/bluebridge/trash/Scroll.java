package com.github.bluebridge.trash;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Daneel Yaitskov
 */
public class Scroll {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JPanel gui = new JPanel(new BorderLayout(5, 5));
                gui.setBorder(new EmptyBorder(3, 3, 3, 3));
                gui.setBackground(Color.red);

                JPanel scrollPanel = new JPanel(new BorderLayout(2, 2));
                scrollPanel.setBackground(Color.green);
                scrollPanel.add(new JLabel("Center"), BorderLayout.CENTER);
                gui.add(new JScrollPane(scrollPanel), BorderLayout.CENTER);

                final JPanel componentPanel = new JPanel(new GridLayout(0, 1, 3, 3));
                componentPanel.setBackground(Color.orange);
                scrollPanel.add(componentPanel, BorderLayout.NORTH);

                JButton add = new JButton("Add");
                gui.add(add, BorderLayout.NORTH);
                add.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        componentPanel.add(new JTextField());
                        gui.validate();
                    }
                });

                Dimension d = gui.getPreferredSize();
                d = new Dimension(d.width, d.height + 100);
                gui.setPreferredSize(d);

                JOptionPane.showMessageDialog(null, gui);
            }
        });
    }
}
