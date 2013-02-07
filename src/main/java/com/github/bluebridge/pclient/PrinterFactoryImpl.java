package com.github.bluebridge.pclient;

/**
 * Daneel Yaitskov
 */
public class PrinterFactoryImpl implements PrinterFactory {
    @Override
    public Printer create(PrinterServiceId serviceId) {
        return new PrinterImpl(serviceId);
    }
}
