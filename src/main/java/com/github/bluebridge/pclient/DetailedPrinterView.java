package com.github.bluebridge.pclient;

import javax.swing.*;
import java.awt.*;

/**
 * Differ from PrinterListCell entire form for one printer (current).
 * Daneel Yaitskov
 */
public class DetailedPrinterView extends JPanel
        implements PrinterObserver {

    private Printer model;
    private JLabel valMac;
    private JLabel valConnectionUrl;
    private JLabel valServiceName;
    private JLabel valStatus;

    public DetailedPrinterView() {
        init();
    }

    private void init() {
        setBackground(Color.BLUE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel pnMac = new JPanel();
        pnMac.setLayout(new BoxLayout(pnMac, BoxLayout.X_AXIS));
        JLabel lblMac = new JLabel("MAC:");
        valMac = new JLabel();
        pnMac.add(lblMac);
        pnMac.add(valMac);
        add(pnMac);

        JPanel pnStatus = new JPanel();
        pnStatus.setLayout(new BoxLayout(pnStatus, BoxLayout.X_AXIS));
        JLabel lblStatus = new JLabel("Status:");
        valStatus = new JLabel();
        pnStatus.add(lblStatus);
        pnStatus.add(valStatus);
        add(pnStatus);

        JPanel pnConnection = new JPanel();
        pnConnection.setLayout(new BoxLayout(pnConnection, BoxLayout.X_AXIS));
        JLabel lblUrl = new JLabel("URL: ");
        lblUrl.setToolTipText("Connection url to the printer");
        valConnectionUrl = new JLabel();
        pnConnection.add(lblUrl);
        pnConnection.add(valConnectionUrl);
        add(pnConnection);
    }

    public Printer getModel() {
        return model;
    }

    public void setModel(Printer model) {
        if (this.model != null) {
            this.model.removeObserver(this);
        }
        this.model = model;
        if (model != null) {
            model.addObserver(this);
            newStatus(model);
        }
    }

    @Override
    public void newStatus(Printer p) {
        final PrinterStatus st = p.getStatus();
        final PrinterServiceId info = p.getInfo();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                valConnectionUrl.setText(info.getConnectionUrl());
                valStatus.setText("" + st);
                valMac.setText(info.getDeviceId());
                //valServiceName.setText(info.getServiceName());
            }
        });
    }
}
