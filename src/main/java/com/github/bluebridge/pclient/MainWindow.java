package com.github.bluebridge.pclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.BluetoothStateException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Application object. GUI.
 * <p/>
 * Daneel Yaitskov
 */
public class MainWindow {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindow.class);

    private JFrame mainFrame;
    private JMenuBar mainMenu;
    private JMenu appMenu;
    private JMenu printerMenu;
    private JMenu helpMenu;
    private JMenuItem startPrintMenu;
    private JMenuItem stopPrintMenu;
    private JMenuItem pausePrintMenu;
    private JMenuItem resumePrintMenu;
    /**
     * Synchronously start 3d new printers lookup.
     * Adds new printers to printer list from left of main window.
     */
    private JMenuItem scanPrintersMenu;

    private JPanel mainPanel;
    /**
     * Detailed information about current printer (status, etc).
     */
    private JPanel infoPanel;
    /**
     * List found printers.
     */
    private JList printerList;
    private DefaultListModel printers;

    public MainWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initialize();
            }
        });
    }

    protected void initialize() {
        buildMenu();
        buildBody();
        buildMainFrame();
    }

    private void buildBody() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        printerList = new JList();
        printerList.setMinimumSize(new Dimension(100, 300));

        printers = new DefaultListModel();
        printerList.setModel(printers);
//        ((DefaultListModel) printers).addElement("no printers");
        // hook click on printer to connect
        printerList.addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent listSelectionEvent) {
                        activatePrinter((Printer) printerList.getSelectedValue());
                    }
                }
        );

        infoPanel = new JPanel();
        infoPanel.setMinimumSize(new Dimension(300, 300));

        mainPanel.add(printerList);
        mainPanel.add(infoPanel);
    }

    private void activatePrinter(Printer printer) {
        LOGGER.info("printer {} was selected",
                printer.getInfo().getConnectionUrl());
        if (printer.getStatus() == PrinterStatus.NA) {
            try {
                printer.connect();
            } catch (IOException e) {
                LOGGER.error("failed connect to printer", e);
            }
        }
    }

    private void buildMainFrame() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setContentPane(mainPanel);

        mainFrame.setJMenuBar(mainMenu);
        mainFrame.setMinimumSize(new Dimension(300, 200));

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void buildMenu() {
        mainMenu = new JMenuBar();

        appMenu = new JMenu("Application");

        JMenuItem exitItem = new JMenuItem();
        exitItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mainFrame.dispose();
            }
        });
        exitItem.setText("Exit");

        appMenu.add(exitItem);

        mainMenu.add(appMenu);

        printerMenu = new JMenu("Printer");

        startPrintMenu = new JMenuItem("Print");
        startPrintMenu.setToolTipText("Start printing process");
        startPrintMenu.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                getCurrentPrinter().startPrint();
            }
        });

        printerMenu.add(startPrintMenu);

        stopPrintMenu = new JMenuItem("Stop");
        stopPrintMenu.setToolTipText(
                "Stop printing process. Operation cannot be rollback");
        stopPrintMenu.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                getCurrentPrinter().startPrint();
            }
        });

        printerMenu.add(stopPrintMenu);


        printerMenu.addSeparator();

        scanPrintersMenu = new JMenuItem("scan");
        scanPrintersMenu.setToolTipText("Update list of printers");
        scanPrintersMenu.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    updatePrinterList();
                } catch (BluetoothStateException e) {
                    LOGGER.error("failed update printer list", e);
                }
            }
        });

        printerMenu.add(scanPrintersMenu);

        mainMenu.add(printerMenu);

        helpMenu = new JMenu("Help");

        JMenuItem about = new JMenuItem();
        about.setText("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(mainFrame, "BlueBridge 2013");
            }
        });
        helpMenu.add(about);
        mainMenu.add(helpMenu);
    }

    public Printer getCurrentPrinter() {
        Printer result = (Printer) printerList.getSelectedValue();
        if (result == null) {
            return new NullPrinter();
        }
        return result;
    }

    /**
     * Find new printer services and merge them to list
     */
    public void updatePrinterList() throws BluetoothStateException {
        PrinterBus bus = BlueToothPrinterBus.getBus();

        java.util.List<PrinterServiceId> services = bus.findAllPrinters();

        nextPrinter:
        for (PrinterServiceId service : services) {
            for (int i = 0; i < printers.getSize(); ++i) {
                Printer printer = (Printer) printers.getElementAt(i);
                if (service.equals(printer.getInfo())) {
                    LOGGER.info("service {} already in the list",
                            service.getConnectionUrl());
                    continue nextPrinter;
                }
            }
            LOGGER.info("new service {}", service.getConnectionUrl());
            Printer newPrinter = new PrinterImpl(service);
            printers.addElement(newPrinter);
        }
    }
}
