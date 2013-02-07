package com.github.bluebridge.pclient;

/**
 * Daneel Yaitskov
 */
public enum PrinterStatus {
    FREE,      // free for printing
    NA,        // not available, device is power off
    PRINTING,  // building a model
    PAUSE,     // manual waiting
    NO_EXTRUDER, // plastic is exhaust
    BUSY       // another request is processed by device
}
