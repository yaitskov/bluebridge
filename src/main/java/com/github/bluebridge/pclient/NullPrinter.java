package com.github.bluebridge.pclient;

import java.io.IOException;

/**
 * Daneel Yaitskov
 */
public class NullPrinter implements Printer {

    @Override
    public PrinterStatus getStatus() {
        return PrinterStatus.NA;
    }

    @Override
    public void startPrint() {

    }

    @Override
    public void abortPrint() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public PrinterServiceId getInfo() {
        return new PrinterServiceId();
    }

    @Override
    public void connect() throws IOException {

    }

    @Override
    public void disconnect() throws IOException {

    }
}
