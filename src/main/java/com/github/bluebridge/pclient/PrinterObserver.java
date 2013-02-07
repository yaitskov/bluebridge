package com.github.bluebridge.pclient;

/**
 * Process events with printers.
 * Daneel Yaitskov
 */
public interface PrinterObserver {

    /**
     * Called when printer gets new status.
     * @param p printer with new status
     */
    void newStatus(Printer p);
}
