package com.github.bluebridge.pclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Daneel Yaitskov
 */
public class PrinterImpl implements Printer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrinterImpl.class);

    private PrinterServiceId serviceInfo;
    private PrinterStatus status;

    public PrinterImpl(PrinterServiceId serviceInfo) {
        this.serviceInfo = serviceInfo;
        status = PrinterStatus.NA;
    }

    @Override
    public PrinterStatus getStatus() {
        return status;
    }

    public void setStatus(PrinterStatus status) {
        this.status = status;
    }

    @Override
    public void startPrint() {
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

    }

    @Override
    public void disconnect() throws IOException {

    }
}
