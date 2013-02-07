package com.github.bluebridge.pclient;

import java.io.File;
import java.io.IOError;
import java.io.IOException;

/**
 * Daneel Yaitskov
 */
public interface Printer {
    PrinterStatus getStatus();

    /**
     * File to be printed.
     */
    void startPrint(File model);

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

    void addObserver(PrinterObserver observer);
    void removeObserver(PrinterObserver observer);
}
