package com.github.bluebridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.*;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/2/13
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);

    private static String serviceURL=null;
    private static List<RemoteDevice> devicesDiscovered = null;

    public static String discoverService(final String serviceNAME,
                                         final UUID serviceID)
            throws IOException, InterruptedException {
        UUID[] searchUuidSet = new UUID[] { serviceID };
        int[] attrIDs = new int[] { 0x0100 };

        devicesDiscovered = DeviceDiscovery.discoverDevices();
         // Discover devices

        final Object serviceSearchCompletedEvent = new Object();

        DiscoveryListener listener = new DiscoveryListener() {
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) { }
            public void inquiryCompleted(int discType) { }

            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                logger.info("servicesDiscovered transID = {}, num service records = {}",
                        transID, servRecord.length);
                for (int i = 0; i < servRecord.length; i++) {
                    String url = servRecord[i].getConnectionURL(
                            ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                    int[] attrIds  = servRecord[i].getAttributeIDs();
                    logger.info("service {} has {} attributes", i, attrIds.length);
                    for (int attrId : attrIds) {
                        DataElement de = servRecord[i].getAttributeValue(attrId);

                        logger.info("attr {} -> {}", attrId, de.getDataType()); //de.getValue());
                    }
                    if (url == null) {
                        logger.info("skip service {} due url is null", i);
                        continue;
                    }
                    DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
                    if (serviceName != null) {
                        logger.info("Checking service "
                                + serviceName.getValue() + " found " + url);
                        logger.info("'{}' = '{}'", serviceName.getValue(),
                                serviceNAME);
                        if(serviceName.getValue().toString().equals(
                                serviceNAME)) {
                            serviceURL=url;
                            return;
                        }

                    } else {
                        logger.info("Checking service found " + url);
                    }
                }
            }

            public void serviceSearchCompleted(int transID, int respCode) {
                logger.info("service search completed!");
                synchronized(serviceSearchCompletedEvent){
                    serviceSearchCompletedEvent.notifyAll();
                }
            }
        };

        for(RemoteDevice btDevice : devicesDiscovered) {
            synchronized(serviceSearchCompletedEvent) {
                logger.info("search services on " +
                        btDevice.getBluetoothAddress() + " "
                        + btDevice.getFriendlyName(false));
                LocalDevice.getLocalDevice().getDiscoveryAgent()
                        .searchServices(attrIDs, searchUuidSet, btDevice, listener);
                serviceSearchCompletedEvent.wait();
                if( serviceURL != null) {
                    break; // Exit on first service match found
                }
            }
        }
        return serviceURL;
    }

    public static void main(String[] args)
            throws IOException, InterruptedException {
        UUID uid = new UUID("27012f0c68af4fbf8dbe6bbaf7aa432a", false);
        ServiceDiscovery.discoverService("Echo Service", uid);
    }
}