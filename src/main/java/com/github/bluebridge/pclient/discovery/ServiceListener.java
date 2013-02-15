package com.github.bluebridge.pclient.discovery;

import com.github.bluebridge.pclient.printer.PrinterServiceId;
import com.github.bluebridge.pclient.SkipException;
import com.github.bluebridge.pclient.printer.BlueToothPrinterBus;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.*;
import java.util.List;

/**
 * Object is also for synchronization.
 */
public class ServiceListener implements DiscoveryListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            ServiceListener.class);

    private List<PrinterServiceId> foundServices;

    public ServiceListener(List<PrinterServiceId> foundServices) {
        this.foundServices = foundServices;
    }

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        // do nothing
        LOGGER.error("cannot be called.");
    }

    public void inquiryCompleted(int discType) {
        // do nothing
        LOGGER.error("cannot be called.");
    }

    public void servicesDiscovered(int transID, ServiceRecord[] sRecords) {
        LOGGER.info("servicesDiscovered transID = {}, num service records = {}",
                transID, sRecords.length);
        for (int i = 0; i < sRecords.length; i++) {
            PrinterServiceId serviceId = null;
            try {
                serviceId = convert(sRecords[i]);
                foundServices.add(serviceId);
            } catch (SkipException e) {
                LOGGER.error("skip service due {}", e.getMessage());
            }
        }
    }


    public void serviceSearchCompleted(int transID, int respCode) {
        LOGGER.info("service search transId = {} and respCode = {} completed",
                transID, respCode);
        synchronized (this) {
            notifyAll();
        }
    }

    private PrinterServiceId convert(ServiceRecord sRecord) throws SkipException {
        PrinterServiceId result = new PrinterServiceId();
        result.setServiceName(getServiceName(sRecord));
        RemoteDevice btDev = sRecord.getHostDevice();
        result.setDeviceId(btDev.getBluetoothAddress());
        String url = sRecord.getConnectionURL(
                ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
        if (url == null) {
            throw new SkipException("service '" + result.getServiceName()
                    + "' url is null");
        }
        result.setConnectionUrl(url);
        // todo: long uuid is not get
        printAllAttributes(result.getServiceName(), sRecord);
        return result;
    }

    private String getServiceName(ServiceRecord sRecord) {
        DataElement serviceName = sRecord.getAttributeValue(
                BlueToothPrinterBus.SERVICE_NAME_ATTR_ID);
        if (serviceName == null) {
            LOGGER.error("service no name");
            return "no name service";
        }
        if (serviceName.getDataType() != DataElement.STRING) {
            throw new IllegalStateException("service name attribute "
                    + BlueToothPrinterBus.SERVICE_NAME_ATTR_ID
                    + " is not string");
        }
        String result = serviceName.getValue().toString();
        LOGGER.info("service name {}", result);
        return result;
    }

    private void printAllAttributes(String name, ServiceRecord sRecord) {
        int[] attrIds = sRecord.getAttributeIDs();
        Gson gson = new Gson();
        LOGGER.info("service {} has {} attributes", name, attrIds.length);
        for (int attrId : attrIds) {
            DataElement de = sRecord.getAttributeValue(attrId);
            try {
                Object value = getValue(de);
                if (value == null) {
                    LOGGER.info("attribute {} is null", attrId);
                    continue;
                }
                LOGGER.info("attr {} -> {}", attrId, gson.toJson(value));
            } catch (SkipException e) {
                LOGGER.error("skip attribute " + attrId
                        + " case " + e.getMessage());
            }
        }
    }

    /**
     * Stupid developers why classcase exception????
     *
     * @param de
     * @return
     */
    private Object getValue(DataElement de) throws SkipException {
        switch (de.getDataType()) {
            case DataElement.U_INT_1:
            case DataElement.U_INT_2:
            case DataElement.U_INT_4:
            case DataElement.INT_1:
            case DataElement.INT_2:
            case DataElement.INT_4:
            case DataElement.INT_8:
                return de.getLong();
            case DataElement.URL:
            case DataElement.STRING:
            case DataElement.UUID:
            case DataElement.U_INT_8:
            case DataElement.U_INT_16:
            case DataElement.INT_16:
            case DataElement.DATSEQ:
            case DataElement.DATALT:
                return de.getValue();
            case DataElement.BOOL:
                return de.getBoolean();
            default:
                throw new SkipException("attribute has unsupported type "
                        + de.getDataType());
        }
    }
}
