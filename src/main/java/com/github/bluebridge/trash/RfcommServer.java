package com.github.bluebridge.trash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import javax.microedition.io.*;
import javax.bluetooth.*;

/**
 * Multi threaded RFCOMM server. One thread per client.
 * Server takes specified number bytes from client.
 * Calculates md5 checksum of the got content
 * and sends it back to the client.
 */
public class RfcommServer {
    public static void main(String args[]) throws IOException {
        UUID uuid = new UUID("27012f0c68af4fbf8dbe6bbaf7aa432a", false);
        String name = "EchoServer";
        RfcommServer server = new RfcommServer(name, uuid);
        server.listen();

    }

    private static final Logger LOGGER =
            LoggerFactory.getLogger(RfcommServer.class);

    private UUID uuid;
    private String serviceName;
    private StreamConnectionNotifier service;
    /**
     * Max size of {@link #workers}.
     */
    private int maxClients = 2;

    /**
     * Life worker thread. One per client.
     */
    private Set<ServerThread> workers;

    public RfcommServer(String serviceName, UUID uuid) {
        this.uuid = uuid;
        this.serviceName = serviceName;
        workers = new HashSet<ServerThread>();
    }

    /**
     * Starts bluetooth RFCOMM server.
     *
     * @throws BluetoothStateException
     */
    public void listen() throws IOException {
        initService();
        while (true) {
            StreamConnection sconn = null;
            try {
                sconn = service.acceptAndOpen();
                LOGGER.info("new client");
                synchronized (workers) {
                    if (workers.size() < maxClients) {
                        processNewClient(sconn);
                    } else {
                        kickNewClient(sconn);
                    }
                }
            } catch (IOException e) {
                handleConnectionException(e, sconn);
            }
        }
    }

    private void initService() throws IOException {
        LocalDevice ldev = LocalDevice.getLocalDevice();
        ldev.setDiscoverable(DiscoveryAgent.NOT_DISCOVERABLE);
        ldev.setDiscoverable(DiscoveryAgent.GIAC);
        service = (StreamConnectionNotifier) Connector.open(
                genServiceUrl(serviceName, uuid));
        LOGGER.info("rfcomm service was created. awaiting clients...");
    }

    private void handleConnectionException(IOException e, StreamConnection sconn) {
        LOGGER.error("new worker failed", e);
        try {
            if (sconn != null) sconn.close();
        } catch (IOException e1) {
            LOGGER.error(
                    "failed close connection after fail worker start",
                    e1);
        }
    }

    private void kickNewClient(StreamConnection sconn) throws IOException {
        LOGGER.error("limit {} of number workers reached", maxClients);
        sconn.close();
    }

    private void processNewClient(StreamConnection sconn) {
        final ServerThread[] workerPointer = new ServerThread[1];
        Runnable finalizer = new Runnable() {
            @Override
            public void run() {
                synchronized (workers) {
                    workers.remove(workerPointer[0]);
                    LOGGER.debug("worker {} ended new size {}",
                            workerPointer[0], workers.size());
                }
            }
        };

        workerPointer[0] = new ServerThread(sconn, finalizer);
        workerPointer[0].start();
        workers.add(workerPointer[0]);
    }

    public String genServiceUrl(String name, UUID uuid) {
        String url = "btspp://localhost:" + uuid
                + ";name=" + name
                + ";authenticate=false;encrypt=false;";
        return url;
    }
}