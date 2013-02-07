package com.github.bluebridge.pclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Stub if printer list is empty. Or no one is selected.
 * Daneel Yaitskov
 */
public class NullPrinter implements Printer {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            NullPrinter.class);

    @Override
    public void addObserver(PrinterObserver observer) {
       // do nothing
    }

    @Override
    public void removeObserver(PrinterObserver observer) {
        // do nothing
    }

    @Override
    public PrinterStatus getStatus() {
        return PrinterStatus.NA;
    }

    @Override
    public void startPrint(File model) {
        LOGGER.info("there is not a selected printer");
    }

    @Override
    public void abortPrint() {
        LOGGER.info("there is not a selected printer");
    }

    @Override
    public void pause() {
        LOGGER.info("there is not a selected printer");
    }

    @Override
    public void resume() {
        LOGGER.info("there is not a selected printer");
    }

    @Override
    public PrinterServiceId getInfo() {
        LOGGER.info("there is not a selected printer");
        PrinterServiceId result = new PrinterServiceId();
        result.setDeviceId("stub");
        result.setConnectionUrl("btspp://stub");
        result.setLongUuid("stub");
        result.setServiceName("stub service");
        return result;
    }

    @Override
    public void connect() throws IOException {
        LOGGER.info("there is not a selected printer");
    }

    @Override
    public void disconnect() throws IOException {
        LOGGER.info("there is not a selected printer");
    }
}
