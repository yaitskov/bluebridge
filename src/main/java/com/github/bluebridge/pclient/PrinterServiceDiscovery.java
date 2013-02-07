package com.github.bluebridge.pclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Finds 3d printer services by UUID matching.
 * Daneel Yaitskov
 */
public class PrinterServiceDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            PrinterServiceDiscovery.class);

    /**
     * Long UUID service.
     */
    private UUID[] searchUuidSet;

    private int[] attrsToGet;

    public PrinterServiceDiscovery(String longUuid) {
        searchUuidSet = new UUID[]{ new UUID(longUuid, false) };
        attrsToGet = new int[]{BlueToothPrinterBus.SERVICE_NAME_ATTR_ID};
    }

    /**
     * Finds 3d printer services (intended SPP) on all specified devices.
     *
     * @param devices BlueTooth devices to be explored
     * @return 3d printer services
     */
    public List<PrinterServiceId> find(List<RemoteDevice> devices) {
        List<PrinterServiceId> result = new LinkedList<PrinterServiceId>();
        ServiceListener listener = new ServiceListener(result);
        for (RemoteDevice btDevice : devices) {
            synchronized (listener) {
                findServiceOnDevice(btDevice, listener);
            }
        }
        return result;
    }

    protected void findServiceOnDevice(RemoteDevice btDevice,
                                       DiscoveryListener listener) {
        LOGGER.info("search services on device {}",
                btDevice.getBluetoothAddress());
        try {
            LocalDevice.getLocalDevice().getDiscoveryAgent()
                    .searchServices(attrsToGet,
                            searchUuidSet, btDevice, listener);
            listener.wait();
        } catch (BluetoothStateException e) {
            LOGGER.error("skip device " + btDevice.getBluetoothAddress()
                    , e);
        } catch (InterruptedException e) {
            LOGGER.info("search service was interrupted");
        }
    }
}
