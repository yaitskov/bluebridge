package com.github.bluebridge;

import com.google.gson.Gson;
import gnu.io.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/15/13
 * Time: 8:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class RxTxTest {

    private static final Logger logger = LoggerFactory.getLogger(RxTxTest.class);

    @Test
    public void testGetPorts() throws
            PortInUseException,
            IOException,
            InterruptedException,
            UnsupportedCommOperationException, TooManyListenersException {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier usb = null;
        while (ports.hasMoreElements()) {
            logger.info("---------------------------------------");

            CommPortIdentifier cpi = (CommPortIdentifier) ports.nextElement();
            logger.info("port name = {}, type = {}",
                    cpi.getName(), cpi.getPortType());
            if (usb == null && cpi.getName().contains("USB")) {
                usb = cpi;
            }
            System.out.println(String.format("port name = \n%s, type = %d",
                    cpi.getName(), cpi.getPortType()));
            logger.info("RS-232 type {}", CommPortIdentifier.PORT_SERIAL);
            // logger.info("is owner {}", cpi.isCurrentlyOwned());
            //logger.info("owner is {} ", cpi.getCurrentOwner());
        }

        if (usb == null) {
            logger.error("no usb tty");
            return;
        }
        logger.info("use port {}", usb.getName());

        CommPort port = usb.open("javatest", 2000 /* wait 2sec */);

        try {
            SerialPort sport = (SerialPort) port;

            sport.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            sport.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            sport.enableReceiveThreshold(1);
            sport.disableReceiveTimeout();
//            sport.disableReceiveFraming();
//            sport.disableReceiveThreshold();
//            sport.disableReceiveTimeout();
            final InputStream is = sport.getInputStream();
//            sport.addEventListener(new SerialPortEventListener() {
//                @Override
//                public void serialEvent(SerialPortEvent ev) {
//                    if (ev.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
//                        logger.info("new data available");
//                        byte[] got = new byte[10];
//                        try {
//                            int read = is.read(got);
//
//                            logger.info("read {} bytes as {}", read, new Gson().toJson(got));
//                        } catch (IOException e) {
//                            logger.error("failed to read", e);
//                        }
//                    }
//                }
//            });
//            sport.notifyOnDataAvailable(true);
            OutputStream os = sport.getOutputStream();
//            OutputStreamWriter osw = new OutputStreamWriter(os);

            logger.debug("on 13 diode");
            byte[] out = "2".getBytes();
            logger.debug("out = {}", new Gson().toJson(out));
            os.write(out);
            os.flush();
//            Thread.sleep(1000);

            byte[] got = new byte[3];
            int read = is.read(got);
            logger.info("read {} bytes as {}", read, new Gson().toJson(got));
            logger.debug("off 13 diode");
            //os.write("1".getBytes());
//            os.flush();
        } finally {
            port.close();
        }
    }
}
