package com.github.bluebridge.trash;

import com.intel.bluetooth.RemoteDeviceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/2/13
 * Time: 10:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeviceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(DeviceDiscovery.class);

    private static final List<RemoteDevice> devicesDiscovered =
            new ArrayList<RemoteDevice>();

    public static List<RemoteDevice> discoverDevices()
            throws IOException, InterruptedException {

        final Object inquiryCompletedEvent = new Object();

        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice,
                                         DeviceClass cod) {
                logger.info("Device "
                        + btDevice.getBluetoothAddress() + " found");
                logger.info("is authenticated {}", btDevice.isAuthenticated());
                logger.info("is encrypted {}", btDevice.isEncrypted());
                logger.info("is trusted {}", btDevice.isTrustedDevice());

                if (!btDevice.isAuthenticated()) {
                    logger.info("trying to authenticate");
                    try {
                       boolean r = RemoteDeviceHelper.authenticate(btDevice, "123");
                        logger.info("auth result {}", r);
                    } catch (IOException e) {
                        logger.error("failed to auto", e); //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                devicesDiscovered.add(btDevice);
                try {
                    logger.info(" name " + btDevice.getFriendlyName(false));
                } catch (IOException cantGetDeviceName) {}
            }

            public void inquiryCompleted(int discType) {
                logger.info("Device Inquiry completed!");
                synchronized(inquiryCompletedEvent){
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {}
            public void servicesDiscovered(int transID,
                                           ServiceRecord[] servRecord) { }
        };

        synchronized(inquiryCompletedEvent) {

            boolean started = LocalDevice.getLocalDevice()
                    .getDiscoveryAgent()
                    .startInquiry(DiscoveryAgent.GIAC, listener);

            if (started) {
                logger.info("wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
                logger.info(devicesDiscovered.size() + " device(s) found");
            }
        }
        return devicesDiscovered;
    }
}