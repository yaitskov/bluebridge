package com.github.bluebridge;

import org.apache.commons.lang3.StringUtils;
import org.dan.lastjcl.ScalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import javax.microedition.io.*;
import javax.bluetooth.*;

public class RfcommClient {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(RfcommClient.class);

    public static void main(String args[])
            throws IOException, InterruptedException,
            NoSuchAlgorithmException, TimeoutException {
        LocalDevice.getLocalDevice().setDiscoverable(
                DiscoveryAgent.NOT_DISCOVERABLE);
        UUID uuid = new UUID("27012f0c68af4fbf8dbe6bbaf7aa432a", false);
        String name = "EchoServer";

        RfcommClient client = new RfcommClient(name, uuid);
        client.run();
    }

    private String serviceName;
    private UUID serviceId;

    public RfcommClient(String serviceName, UUID serviceId) {
        this.serviceName = serviceName;
        this.serviceId = serviceId;
    }


    public void run() throws IOException, InterruptedException,
            TimeoutException, NoSuchAlgorithmException {
        String url = ServiceDiscovery.discoverService(serviceName, serviceId);

        if (url == null) {
            LOGGER.info("service not found.");
            return;
        }

        LOGGER.info("service url is '{}'", url);

        Connection con = Connector.open(url);
        StreamConnection sc = (StreamConnection) con;
        try {
            LOGGER.info("bluetooth connection was created");
            SafeWriter output = new SafeWriter(sc.openOutputStream());
            SafeReader input = new SafeReader(sc.openInputStream());

            String message = "Hello world. Big man.";

            output.write(message.getBytes());
            LOGGER.debug("message was sent.");

            byte[] hashGot = input.readPackage();

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashExpected = md.digest(message.getBytes());

            LOGGER.debug("hashes equal {}", Arrays.equals(hashExpected, hashGot));
            LOGGER.debug("got {} expected {}",
                    StringUtils.join(
                            ScalUtils.wrapList(hashGot), ", "),
                    StringUtils.join(
                            ScalUtils.wrapList(hashExpected), ", "));
        } finally {
            LOGGER.info("close connection");
            con.close();
        }
    }
}