package com.github.bluebridge.pclient.printer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Daneel Yaitskov
 */
public class PrinterImpl implements Printer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrinterImpl.class);

    private PrinterServiceId serviceInfo;
    private PrinterStatus status;
    private List<PrinterObserver> observers;

    @Override
    public void addObserver(PrinterObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(PrinterObserver observer) {
        observers.remove(observer);
    }

    public PrinterImpl(PrinterServiceId serviceInfo) {
        this.serviceInfo = serviceInfo;
        status = PrinterStatus.NA;
        observers = new LinkedList<PrinterObserver>();
    }

    @Override
    public PrinterStatus getStatus() {
        return status;
    }

    public void setStatus(PrinterStatus status) {
        this.status = status;
    }

    @Override
    public void startPrint(File model) {
        LOGGER.debug("start printing");
    }

    @Override
    public void abortPrint() {
        LOGGER.debug("abort printing");
    }

    @Override
    public void pause() {
        LOGGER.debug("pause printing");
    }

    @Override
    public void resume() {
        LOGGER.debug("resume printing");
    }

    @Override
    public PrinterServiceId getInfo() {
        return new PrinterServiceId(serviceInfo);
    }

    @Override
    public void connect() throws IOException {
        LOGGER.debug("start connecting to printer");
    }

    @Override
    public void disconnect() throws IOException {
        LOGGER.debug("close connection with printer");

    }
}
