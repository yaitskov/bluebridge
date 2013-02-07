package com.github.bluebridge.trash;

import com.github.bluebridge.rfcomm.SafeReader;
import com.github.bluebridge.rfcomm.SafeWriter;
import org.apache.commons.lang3.StringUtils;
import org.dan.lastjcl.ScalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Daneel Yaitskov
 *  */
public class ServerThread extends Thread {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ServerThread.class);

    private StreamConnection connection;
    private SafeReader input;
    private OutputStream output;

    /**
     * Called at the end of the thread. Only if thread was run.
     */
    private Runnable finalizer;

    /**
     * @param sconn     cannot be null
     * @param finalizer cannot be null
     */
    public ServerThread(StreamConnection sconn, Runnable finalizer) {
        connection = sconn;
        this.finalizer = finalizer;
    }

    @Override
    public void run() {
        try {
            input = new SafeReader(connection.openInputStream());
            output = new SafeWriter(connection.openOutputStream());
            communicate();
        } catch (IOException e) {
            LOGGER.error("failed open data stream", e);
        } catch (TimeoutException e) {
            LOGGER.error("failed open data stream", e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("failed open data stream", e);
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                LOGGER.error("failed close bluetooth connection", e);
            }
            finalizer.run();
        }
    }

    protected void communicate() throws IOException, TimeoutException,
            NoSuchAlgorithmException {
        byte[] data = input.readPackage();
        String message = new String(data);
        LOGGER.debug("message is '{}'", message);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(data);
        LOGGER.debug("md5 digest of input {}",
                StringUtils.join(
                        ScalUtils.wrapList(hash), ", "));
        output.write(hash);
    }


}
