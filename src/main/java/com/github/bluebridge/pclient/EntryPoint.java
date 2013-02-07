package com.github.bluebridge.pclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.BluetoothStateException;

/**
 * Entry point to the GUI Swing 3d printer client
 * Daneel Yaitskov
 */
public class EntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String[] args) {
        LOGGER.debug("init BlueTooth layer");
        BlueToothPrinterBus.setServiceName("BlueBridgeService");
        BlueToothPrinterBus.setServiceUuid("31212f0c68af4fbf8dbe6bbaf7aa432a");
        try {
            BlueToothPrinterBus.getBus();
        } catch (BluetoothStateException e) {
            LOGGER.error("failed to init BlueCove", e);
        }

        LOGGER.debug("launch swing interface");
        new MainWindow();
    }
}
