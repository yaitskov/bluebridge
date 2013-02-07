package com.github.bluebridge.pclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import java.util.LinkedList;
import java.util.List;

/**
 * Finds with SDP all available BlueTooth devices for
 * the current machine.
 * <p/>
 * Daneel Yaitskov
 */
public class BlueToothDeviceDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            BlueToothDeviceDiscovery.class);

    /**
     * Finds and returns all BlueTooth devices.
     *
     * @return returns all BlueTooth devices in the area
     */
    public List<RemoteDevice> find() throws BluetoothStateException {
        List<RemoteDevice> result = new LinkedList<RemoteDevice>();

        DeviceListener listener = new DeviceListener(result);
        synchronized (listener) {
            boolean started = LocalDevice.getLocalDevice()
                    .getDiscoveryAgent()
                    .startInquiry(DiscoveryAgent.GIAC, listener);

            if (started) {
                LOGGER.info("start device discovery...");
                try {
                    listener.wait();
                } catch (InterruptedException e) {
                    LOGGER.info("discovery process was interrupted another thread");
                }
                LOGGER.info("discovery process found {} device(s)", result.size());
            } else {
                LOGGER.error("blueTooth discovery is not started");
            }
        }
        return result;
    }
}
