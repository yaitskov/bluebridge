package com.github.bluebridge.pclient;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test GUI layout with multiple printer in the list.
 * Daneel Yaitskov
 */
public class FakeBusEntryPoint {

    private static int newDeviceId = 0;

    public static void main(String[] args) {

        new MainWindow(
                new PrinterBus() {
                    @Override
                    public List<PrinterServiceId> findAllPrinters() {
                        List<PrinterServiceId> result = new ArrayList<PrinterServiceId>();
                        PrinterServiceId serviceNa = new PrinterServiceId();
                        serviceNa.setLongUuid("NA uid " + newDeviceId);
                        serviceNa.setConnectionUrl("NA conn " + newDeviceId);
                        serviceNa.setDeviceId("NA device " + newDeviceId);
                        serviceNa.setServiceName("NA service " + newDeviceId);
                        result.add(serviceNa);

                        PrinterServiceId serviceFree = new PrinterServiceId();
                        serviceFree.setLongUuid("new uid " + newDeviceId);
                        serviceFree.setConnectionUrl("new conn " + newDeviceId);
                        serviceFree.setDeviceId("new device " + newDeviceId);
                        serviceFree.setServiceName("new service " + newDeviceId);
                        result.add( serviceFree);
                        return result;
                    }
                },
                new PrinterFactory() {
                    @Override
                    public Printer create(PrinterServiceId serviceId) {
                        PojoPrinter pp = new PojoPrinter();
                        pp.setServiceId(serviceId);
                        if (serviceId.getServiceName().contains("NA")) {
                            pp.setStatus(PrinterStatus.NA);
                        } else {
                            pp.setStatus(PrinterStatus.FREE);
                        }

                        return pp;
                    }
                }
        );
    }
}
