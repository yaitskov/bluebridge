package com.github.bluebridge.pclient.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import java.io.IOException;
import java.util.List;

public class DeviceListener implements DiscoveryListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceListener.class);

    private List<RemoteDevice> found;

    public DeviceListener(List<RemoteDevice> found) {
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
