package com.github.bluebridge.trash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/2/13
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class EchoClient {

    private static final Logger logger =
            LoggerFactory.getLogger(EchoClient.class);


    public static void main(String[] args)
            throws IOException, InterruptedException {
        LocalDevice.getLocalDevice().setDiscoverable(
                DiscoveryAgent.NOT_DISCOVERABLE);
        UUID uid = new UUID("27012f0c68af4fbf8dbe6bbaf7aa432a", false);
        String url = ServiceDiscovery.discoverService("EchoServer", uid);

        if (url == null) {
            logger.info("service not found.");
            return;
        }


        ///url = "btspp://001167733157:1;authenticate=false;encrypt=false;master=false";
        logger.info("service url is '{}'", url);

        Connection con = Connector.open(url);
        try {


            logger.info("connection has class {}", con.getClass());
            StreamConnection sc = (StreamConnection) con;
            //InputStream is = sc.openInputStream();

            logger.info("input stream was opened");
            OutputStream os = sc.openOutputStream();
            logger.info("output stream was opened");

            OutputStreamWriter osw = new OutputStreamWriter(os);
            for (int i = 0; i < 10; ++i) {
                osw.write("Hello " + i + " world\n");
                osw.flush();

                osw.write("I;ll be back\n");
            }
            osw.close();
            //is.close();
        } finally {
            logger.info("close connection");
            con.close();
        }
    }
}
