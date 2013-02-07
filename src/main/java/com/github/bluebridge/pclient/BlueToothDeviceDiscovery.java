package com.github.bluebridge.pclient;

import com.intel.bluetooth.RemoteDeviceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.*;
import java.io.IOException;
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

    private class DeviceListener implements DiscoveryListener {

        private List<RemoteDevice> found;

        private DeviceListener(List<RemoteDevice> found) {
            this.found = found;
        }

        @Override
        public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
            LOGGER.info("Device with MAC {} found. It uses encryption {}",
                    btDevice.getBluetoothAddress(),
                    btDevice.isEncrypted());
            LOGGER.info("authenticated {}; trusted {}",
                    btDevice.isAuthenticated(),
                    btDevice.isTrustedDevice());

//            if (!btDevice.isAuthenticated()) {
//                LOGGER.info("trying to authenticate");
//                try {
//                    // todo: pin store. ask user new value with popup if pin not found or invalid
//                    boolean r = RemoteDeviceHelper.authenticate(btDevice, "123");
//                    LOGGER.info("auth result {} for {}",
//                            r, btDevice.getBluetoothAddress());
//                } catch (IOException e) {
//                    LOGGER.error("failed to authenticate", e);
//                }
//            }
            found.add(btDevice);
            try {
                LOGGER.info("friendly name {}", btDevice.getFriendlyName(false));
            } catch (IOException cantGetDeviceName) {
                LOGGER.error("failed to get friendly name", cantGetDeviceName);
            }
        }

        @Override
        public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
            // do nothing. not called.
            LOGGER.debug("servicesDiscovered. this message should not printed.");
        }

        @Override
        public void serviceSearchCompleted(int transID, int respCode) {
            // do nothing. not called.
            LOGGER.debug("serviceSearchCompleted. this message should not printed.");
        }

        /**
         * Notify about end discovery process. Result is complete.
         *
         * @param discType
         */
        @Override
        public void inquiryCompleted(int discType) {
            LOGGER.info("Device inquiry of type {} completed", discType);
            synchronized (this) {
                notifyAll();
            }
        }
    }

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
