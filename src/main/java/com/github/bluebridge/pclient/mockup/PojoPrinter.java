package com.github.bluebridge.pclient.mockup;

import com.github.bluebridge.pclient.printer.Printer;
import com.github.bluebridge.pclient.printer.PrinterObserver;
import com.github.bluebridge.pclient.printer.PrinterServiceId;
import com.github.bluebridge.pclient.printer.PrinterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Daneel Yaitskov
 */
public class PojoPrinter implements Printer {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            PojoPrinter.class);

    private PrinterServiceId serviceId;
    private PrinterStatus status;
    private List<PrinterObserver> observers =
            new CopyOnWriteArrayList<PrinterObserver>();
    //LinkedList<PrinterObserver>();
    private Thread printingThread;

    @Override
    public void addObserver(PrinterObserver observer) {
//        LOGGER.debug("add observer {}. old size {}", observer, observers.size());
        observers.add(observer);
//        LOGGER.debug("add observer. new size {}", observers.size());
    }

    @Override
    public void removeObserver(PrinterObserver observer) {
//        LOGGER.debug("remove observer {}. old size {}", observer, observers.size());
        observers.remove(observer);
//        LOGGER.debug("remove observer. new size {}", observers.size());
    }

    private void notifyObservers() {
        for (PrinterObserver obs : observers) {
//            LOGGER.debug("notify observer {} from {}", obs, this);
            obs.newStatus(this);
        }
    }

    @Override
    public PrinterStatus getStatus() {
        return status;
    }

    @Override
    public void startPrint(final File model) {
        if (status != PrinterStatus.FREE) {
            LOGGER.error("improper state of the printer. It should be FREE.");
            return;
        }
        status = PrinterStatus.PRINTING;
        notifyObservers();
        printingThread = new Thread("printing thread") {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    is = new FileInputStream(model);
                    while(true) {
                        if (isInterrupted()) {
                            LOGGER.info("thread was interrupted");
                            break;
                        }
                        int b = is.read();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            LOGGER.info("thread was interrupted");
                            break;
                        }
                        notifyObservers();
                        if (b < 0) break;
                        LOGGER.info("read one byte {}", b);
                    }
                    LOGGER.info("finish print file");
                } catch (IOException e) {
                    LOGGER.error("failed to read file", e);
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                            LOGGER.debug("file {} closed", model);
                        } catch (IOException e) {
                            LOGGER.error("failed close file", e);
                        }
                    }
                    status = PrinterStatus.FREE;
                    notifyObservers();
                }
            }
        };
        printingThread.start();
    }

    @Override
    public void abortPrint() {
        if (status == PrinterStatus.PRINTING) {
            if (printingThread != null) {
                printingThread.interrupt();
            }
            status = PrinterStatus.FREE;
            notifyObservers();
        }
    }

    @Override
    public void pause() {
        if (status == PrinterStatus.PRINTING) {
            status = PrinterStatus.PAUSE;
            notifyObservers();
        }
    }

    @Override
    public void resume() {
        if (status == PrinterStatus.PAUSE) {
            status = PrinterStatus.PRINTING;
            notifyObservers();
        }
    }

    @Override
    public PrinterServiceId getInfo() {
        return new PrinterServiceId(serviceId);
    }

    @Override
    public void connect() throws IOException {
        if (status == PrinterStatus.NA) {
            status = PrinterStatus.FREE;
            notifyObservers();
        }
    }

    @Override
    public void disconnect() throws IOException {
        status = PrinterStatus.NA;
        notifyObservers();
    }

    public PrinterServiceId getServiceId() {
        return serviceId;
    }

    public void setServiceId(PrinterServiceId serviceId) {
        this.serviceId = serviceId;
    }

    public List<PrinterObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<PrinterObserver> observers) {
        this.observers = observers;
    }

    public void setStatus(PrinterStatus status) {
        this.status = status;
    }
}
