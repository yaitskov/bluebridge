package com.github.bluebridge;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/2/13
 * Time: 7:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
public class LocalDeviceTest {

    private static final Logger logger =
            LoggerFactory.getLogger(LocalDeviceTest.class);


    @Test
    public void testIsPowerOn() {
        logger.info("isPowerOn {}", LocalDevice.isPowerOn());
    }

    @Test
    public void test1() throws BluetoothStateException {
        LocalDevice ld = LocalDevice.getLocalDevice();
        logger.info("blue tooth address {}", ld.getBluetoothAddress());
        logger.info("is discoverable {}", ld.getDiscoverable());
        logger.info("friendly name {}", ld.getFriendlyName());
        DeviceClass dc = ld.getDeviceClass();
        logger.info("dc major device {}; minor device {}",
                dc.getMajorDeviceClass(), dc.getMinorDeviceClass());
        logger.info("device classes {}", dc.getServiceClasses());

        DiscoveryAgent da = ld.getDiscoveryAgent();

        logger.info("blue tooth address {}", ld.getBluetoothAddress());
        logger.info("blue tooth address {}", ld.getBluetoothAddress());
        logger.info("blue tooth address {}", ld.getBluetoothAddress());
        logger.info("blue tooth address {}", ld.getBluetoothAddress());
    }
}
