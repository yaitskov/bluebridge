package com.github.bluebridge.pclient.form;

import com.github.bluebridge.pclient.mockup.NullPrinter;
import com.github.bluebridge.pclient.printer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.BluetoothStateException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Application object. GUI.
 * <p/>
 * Daneel Yaitskov
 */
public class MainWindow implements PrinterObserver {

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
    private DetailedPrinterView infoPanel;
    /**
     * List found printers.
     */
    private JList printerList;
    private DefaultListModel printers;
    private Thread updatePrinterListThread;
    private PrinterBus printerBus;
    private PrinterFactory printerFactory;

    public MainWindow(PrinterBus bus, PrinterFactory factory) {
        printerBus = bus;
        this.printerFactory = factory;

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

        GridBagLayout glayout = new GridBagLayout();
        mainPanel.setLayout(glayout);

        buildPrinterList();
        buildInfoPanel();

        addPrinterToPanel(glayout);
        addInfoPanelTo(glayout);
    }

    private void addInfoPanelTo(GridBagLayout glayout) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        glayout.setConstraints(infoPanel, gbc);
        mainPanel.add(infoPanel);
    }

    private void addPrinterToPanel(GridBagLayout glayout) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.weightx = 0.3;

        JScrollPane printScrol = new JScrollPane(printerList);
        JPanel jp = new JPanel();
        jp.add(printScrol);
        glayout.setConstraints(jp, gbc);
        mainPanel.add(printerList);
    }

    private void buildPrinterList() {
        printerList = new JList();
        printers = new DefaultListModel();
        printerList.setModel(printers);
        printerList.setCellRenderer(new PrinterCellRenderer());
        printerList.setBackground(Color.MAGENTA);
        // hook click on printer to connect
        printerList.addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent listSelectionEvent) {
                        activatePrinter((Printer) printerList.getSelectedValue());
                    }
                }
        );
    }

    private void buildInfoPanel() {
        infoPanel = new DetailedPrinterView();
    }

    /**
     * Protect from double {@link #activatePrinter(Printer)} execution
     * due click down and up.
     */
    private Printer currentPrinter = new NullPrinter();
    private FileModelSelector modelSelector = new FileModelSelector();

    private void activatePrinter(Printer printer) {
        if (currentPrinter != null
                && currentPrinter.getInfo().equals(printer.getInfo())) {
            return;
        }
        currentPrinter = printer;
        LOGGER.info("printer {} was selected",
                printer.getInfo().getConnectionUrl());
        infoPanel.setModel(printer);
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
        mainFrame.setTitle("Blue Bridge");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setContentPane(mainPanel);

        mainFrame.setJMenuBar(mainMenu);
        mainFrame.setMinimumSize(new Dimension(300, 200));

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void buildStartPrint() {
        startPrintMenu = new JMenuItem("Print");
        startPrintMenu.setToolTipText("Start printing process");
        startPrintMenu.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!hasPrinter()) {
                    return;
                }
                File model = modelSelector.select(mainFrame);
                if (model != null) {
                    LOGGER.info("selected file {} for printing", model);
                    getCurrentPrinter().startPrint(model);
                }
            }
        });
        printerMenu.add(startPrintMenu);
    }

    private void buildStopPrint() {
        stopPrintMenu = new JMenuItem("Stop");
        stopPrintMenu.setToolTipText(
                "Stop printing process. Operation cannot be rollback");
        stopPrintMenu.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (hasPrinter()) {
                    getCurrentPrinter().abortPrint();
                }
            }
        });
        printerMenu.add(stopPrintMenu);
    }

    private void buildScanPrinters() {
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
    }

    private void buildAppMenu() {
        appMenu = new JMenu("Application");

        JMenuItem exitItem = new JMenuItem();
        exitItem.addActionListener(
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        mainFrame.dispose();
                    }
                });
        exitItem.setText("Exit");

        appMenu.add(exitItem);
        mainMenu.add(appMenu);
    }

    private void buildPrinterMenu() {
        printerMenu = new JMenu("Printer");
        buildStartPrint();
        buildStopPrint();
        printerMenu.addSeparator();
        buildScanPrinters();

        mainMenu.add(printerMenu);
    }

    private void buildHelpMenu() {
        helpMenu = new JMenu("Help");

        JMenuItem about = new JMenuItem();
        about.setText("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(mainFrame,
                        "BlueBridge v1.0 2013 year");
            }
        });
        helpMenu.add(about);
        mainMenu.add(helpMenu);
    }

    private void buildMenu() {
        mainMenu = new JMenuBar();

        buildAppMenu();
        buildPrinterMenu();
        buildHelpMenu();
    }

    public boolean hasPrinter() {
        if (getCurrentPrinter() instanceof NullPrinter) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Select a printer from the list.",
                    "No current printer", JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    public Printer getCurrentPrinter() {
        return currentPrinter;
    }

    /**
     * Find new printer services and merge them to list
     */
    public void updatePrinterList() throws BluetoothStateException {
        if (updatePrinterListThread != null
                && updatePrinterListThread.isAlive()) {
            LOGGER.info("update printer list thread is still alive");
            return;
        }
        updatePrinterListThread = new Thread("update-device-list") {
            @Override
            public void run() {
                final java.util.List<PrinterServiceId> services = printerBus.findAllPrinters();
                SwingUtilities.invokeLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                mergeNewPrinters(services);
                            }
                        }
                );
            }
        };
        updatePrinterListThread.start();
    }

    public void mergeNewPrinters(java.util.List<PrinterServiceId> services) {
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
            Printer newPrinter = printerFactory.create(service);
            newPrinter.addObserver(this);
            printers.addElement(newPrinter);
        }
    }

    /**
     * It will useful if printer list shows dynamic info
     * not only MAC and service name.
     * But now is not useful.
     *
     * @param p printer with new status
     */
    @Override
    public void newStatus(Printer p) {
        printerList.repaint();
    }
}
