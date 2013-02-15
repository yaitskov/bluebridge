package com.github.bluebridge.pclient.printer;

import com.github.bluebridge.pclient.discovery.PrinterServiceDiscovery;
import com.github.bluebridge.pclient.discovery.BlueToothDeviceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Singleton uses Simple Discovery Protocol (SDP)
 * to find BlueTooth printers with RFCOMM.
 *
 * BlueCove initialization.
 * Serial Port Profile (SPP).
 *
 * Daneel Yaitskov
 */
public class BlueToothPrinterBus implements PrinterBus {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            BlueToothPrinterBus.class);

    /**
     * BlueTooth standard id for service name
     */
    public static final int SERVICE_NAME_ATTR_ID = 0x100;

    /**
     * BlueTooth long service UUID (SPP).
     */
    private static String serviceUuid = "12312f0c68af4fbf8dbe6bbaf7aa432a";

    /**
     * BlueTooth service name (SPP).
     */
    private static String serviceName = "BlueBridge";

    /**
     * Lone instance of BlueTooth printer bus
     */
    private static PrinterBus bus;

    private BlueToothDeviceDiscovery deviceDiscovery;
    private PrinterServiceDiscovery serviceDiscovery;

    @Override
    public List<PrinterServiceId> findAllPrinters() {
        List<RemoteDevice> devices;
        try {
             devices = deviceDiscovery.find();
        } catch (BluetoothStateException e) {
            LOGGER.error("failed to get device list", e);
            return new ArrayList<PrinterServiceId>();
        }
        List<PrinterServiceId> services = serviceDiscovery.find(devices);
        removeUnknowServices(services);
        return services;
    }

    private void removeUnknowServices(List<PrinterServiceId> services) {
        LOGGER.info("remove unknown services");
        Iterator<PrinterServiceId> iter = services.iterator();
        while (iter.hasNext()) {
            PrinterServiceId serviceId = iter.next();
            if (!serviceName.equals(serviceId.getServiceName())) {
                LOGGER.info("remove service with name {}",
                        serviceId.getServiceName());
                iter.remove();
            }
        }
    }


    private BlueToothPrinterBus() throws BluetoothStateException {
        // reinitialize BlueCove if previous process crashed.
        // year its strange that previous launch have sense
        // but it seems it helps
        LocalDevice.getLocalDevice().setDiscoverable(
                DiscoveryAgent.NOT_DISCOVERABLE);

        deviceDiscovery = new BlueToothDeviceDiscovery();
        serviceDiscovery = new PrinterServiceDiscovery(serviceUuid);
    }

    public static PrinterBus getBus() throws BluetoothStateException {
        if (bus == null) {
            bus = new BlueToothPrinterBus();
        }
        return bus;
    }

    public static String getServiceUuid() {
        return serviceUuid;
    }

    public static void setServiceUuid(String serviceUuid) {
        BlueToothPrinterBus.serviceUuid = serviceUuid;
    }

    public static String getServiceName() {
        return serviceName;
    }

    public static void setServiceName(String serviceName) {
        BlueToothPrinterBus.serviceName = serviceName;
    }
}
