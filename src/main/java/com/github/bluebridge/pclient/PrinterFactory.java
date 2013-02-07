package com.github.bluebridge.pclient;

/**
 * Daneel Yaitskov
 */
public interface PrinterFactory {

    Printer create(PrinterServiceId serviceId);
}
