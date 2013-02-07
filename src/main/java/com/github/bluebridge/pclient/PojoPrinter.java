package com.github.bluebridge.pclient;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Daneel Yaitskov
 */
public class PojoPrinter implements Printer {

    private PrinterServiceId serviceId;
    private PrinterStatus status;
    private List<PrinterObserver> observers =
            new LinkedList<PrinterObserver>();

    @Override
    public void addObserver(PrinterObserver observer) {
        observers.add(observer);
    }

    @Override
    public PrinterStatus getStatus() {
        return status;
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
        return new PrinterServiceId(serviceId);
    }

    @Override
    public void connect() throws IOException {

    }

    @Override
    public void disconnect() throws IOException {

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
