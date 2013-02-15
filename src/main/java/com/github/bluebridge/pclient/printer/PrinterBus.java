package com.github.bluebridge.pclient.printer;

import java.util.List;

/**
 * BlueTooth device and service discovery.
 *
 * Daneel Yaitskov
 */
public interface PrinterBus {

    /**
     * Returns list of all available supported 3d printers.
     * @return list of all available supported 3d printers
     */
    List<PrinterServiceId> findAllPrinters();
}
