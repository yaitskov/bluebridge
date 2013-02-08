package com.github.bluebridge.pclient.printer;

/**
 * Daneel Yaitskov
 */
public interface PrinterFactory {

    Printer create(PrinterServiceId serviceId);
}
