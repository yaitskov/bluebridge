package com.github.bluebridge.pclient.mockup;

import com.github.bluebridge.pclient.form.MainWindow;
import com.github.bluebridge.pclient.printer.*;

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

                        newDeviceId += 1;

                        PrinterServiceId serviceFree = new PrinterServiceId();
                        serviceFree.setLongUuid("new uid " + newDeviceId);
                        serviceFree.setConnectionUrl("new conn " + newDeviceId);
                        serviceFree.setDeviceId("new device " + newDeviceId);
                        serviceFree.setServiceName("new service " + newDeviceId);
                        result.add( serviceFree);

                        newDeviceId += 1;

                        PrinterServiceId noExtruder = new PrinterServiceId();
                        noExtruder.setLongUuid("no extruder " + newDeviceId);
                        noExtruder.setConnectionUrl("no extruder " + newDeviceId);
                        noExtruder.setDeviceId("no extruder " + newDeviceId);
                        noExtruder.setServiceName("no extruder " + newDeviceId);
                        result.add( noExtruder);
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
                        } else if (serviceId.getServiceName().contains("extruder")) {
                            pp.setStatus(PrinterStatus.NO_EXTRUDER);
                        } else {
                            pp.setStatus(PrinterStatus.FREE);
                        }

                        return pp;
                    }
                }
        );
    }
}
