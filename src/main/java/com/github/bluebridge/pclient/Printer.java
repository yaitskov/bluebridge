package com.github.bluebridge.pclient;

import java.io.IOError;
import java.io.IOException;

/**
 * Daneel Yaitskov
 */
public interface Printer {
    PrinterStatus getStatus();

    /**
     *
     */
    void startPrint();

    /**
     * Terminate printing process.
     */
    void abortPrint();

    /**
     * Sleep printing process.
     */
    void pause();

    /**
     * Continue printing process.
     */
    void resume();

    PrinterServiceId getInfo();

    /**
     * Open RFCOMM connection.
     */
    void connect() throws IOException;

    /**
     * Close RFCOMM connection.
     */
    void disconnect() throws IOException;
}
