package com.github.bluebridge.pclient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Daneel Yaitskov
 */
public class PrinterListCell extends JPanel {
    private JLabel serviceName;
    private JLabel devId;

    public PrinterListCell(Printer model) {

        // vertical orientation of labels
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(new EmptyBorder(4, 4, 4, 4));
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
        serviceName = new JLabel(model.getInfo().getServiceName());
        serviceName.setToolTipText("BlueTooth service name");
        add(serviceName);

        devId = new JLabel(model.getInfo().getDeviceId());
        devId.setToolTipText("BlueTooth MAC");
        add(devId);

        newStatus(model.getStatus());
    }


    public void newStatus(PrinterStatus newStatus) {
        switch (newStatus) {
            case NA:
                setToolTipText("Printer is not available. It seems it's down.");
                serviceName.setForeground(Color.GRAY);
                devId.setForeground(Color.GRAY);
                break;
            case FREE:
                setToolTipText("Free for printing");
                serviceName.setForeground(Color.GREEN);
                devId.setForeground(Color.GREEN);
                break;
            case NO_EXTRUDER:
                setToolTipText("Insert new coil of plastic and press resume menu item.");
                serviceName.setForeground(Color.RED);
                devId.setForeground(Color.RED);
                break;
            default:
                setToolTipText("Printer is available. Minor state.");
                serviceName.setForeground(Color.BLACK);
                devId.setForeground(Color.BLACK);
        }
    }
}
