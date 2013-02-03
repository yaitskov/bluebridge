package com.github.bluebridge;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class EchoServer {

    private static final Logger logger =
            LoggerFactory.getLogger(EchoServer.class);

    public final UUID uuid = new UUID(
            //the uid of the service, it has to be unique,
            //it can be generated randomly
            "27012f0c68af4fbf8dbe6bbaf7aa432a", false);


    public final String name = "EchoServer";
    //the name of the service
    public final String url = "btspp://localhost:" + uuid
            //the service url
            + ";name=" + name
            + ";authenticate=false;encrypt=false;";
    LocalDevice local = null;
    StreamConnectionNotifier server = null;
    StreamConnection conn = null;

    public EchoServer() {
        try {
            logger.info("uuid {}", uuid);
            logger.info("Setting device to be discoverable...");
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.NOT_DISCOVERABLE);
            local.setDiscoverable(DiscoveryAgent.GIAC);
            logger.info("Start advertising service...");
            server = (StreamConnectionNotifier) Connector.open(url);
            try {
                logger.info("Getting service record");
                ServiceRecord sr = local.getRecord(server);
                String connUrl = sr.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
//            sr.setDeviceServiceClasses(DeviceClass);

                logger.info("Waiting for incoming connection at {}...", connUrl);
                conn = server.acceptAndOpen();

                try {
                    logger.info("Client Connected...");
                    InputStream is = conn.openInputStream();
                    String cmd = IOUtils.toString(is);
                    logger.info("Received " + cmd);
                    is.close();

                } finally {
                    conn.close();
                }

            } finally {
                server.close();
            }


        } catch (Exception e) {
            logger.info("Exception Occured: " + e.toString());
        }
    }

    public static void main(String args[]) {
        EchoServer echoserver = new EchoServer();
    }

}