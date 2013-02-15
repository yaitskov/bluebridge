package com.github.bluebridge.pclient.printer;

/**
 * Unique identifies 3d printer and its SPP service.
 *
 * Daneel Yaitskov
 */
public class PrinterServiceId {

    /**
     * Long BlueTooth service uuid.
     */
    private String longUuid;
    /**
     * Service name SPP without spaces!
     */
    private String serviceName;

    /**
     * BlueTooth MAC.
     */
    private String deviceId;

    /**
     * Url to SPP service like btspp://localhost:123456...
     */
    private String connectionUrl;

    public String getLongUuid() {
        return longUuid;
    }

    public void setLongUuid(String longUuid) {
        this.longUuid = longUuid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        PrinterServiceId serviceId = (PrinterServiceId) o;

        return connectionUrl == null &&
                serviceId.connectionUrl == null
                || connectionUrl != null
                    && connectionUrl.equals(serviceId.connectionUrl);
    }

    public PrinterServiceId(PrinterServiceId orig) {
        this.connectionUrl = orig.connectionUrl;
        this.deviceId = orig.deviceId;
        this.serviceName = orig.serviceName;
        this.longUuid = orig.longUuid;
    }

    public PrinterServiceId() {
    }
}
