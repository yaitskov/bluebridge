package com.github.bluebridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 1/31/13
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class EntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String[] args)
            throws BluetoothStateException, InterruptedException {
        logger.info("main started");
        final Object inquiryCompletedEvent = new Object();

        final List<RemoteDevice> devicesDiscovered = new ArrayList();
        devicesDiscovered.clear();

        DiscoveryListener listener = new DiscoveryListener() {

            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                logger.info("Device {} found", btDevice.getBluetoothAddress());
                devicesDiscovered.add(btDevice);
                try {
                    logger.info("     name {}", btDevice.getFriendlyName(false));
                } catch (IOException cantGetDeviceName) {
                }
            }

            public void inquiryCompleted(int discType) {
                logger.debug("Device Inquiry completed!");
                synchronized (inquiryCompletedEvent) {
                    inquiryCompletedEvent.notifyAll();
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
                logger.info("serviceSearchCompleted transID = {}, respCode = {}",
                        transID, respCode);
            }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                logger.info("servicesDiscovered transID = {}", transID);
                for (ServiceRecord sr : servRecord) {
                    logger.info("service discovered {}", sr.getAttributeIDs().length);
                }
            }
        };

        synchronized (inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().
                    getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if (started) {
                logger.info("wait for device inquiry to complete...");
                inquiryCompletedEvent.wait();
                logger.info("{} device(s) found", devicesDiscovered.size());

            }
        }
    }
}
