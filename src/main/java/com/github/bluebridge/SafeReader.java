package com.github.bluebridge;

import org.apache.commons.lang3.StringUtils;
import org.dan.lastjcl.ScalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/3/13
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SafeReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SafeReader.class);
    private long readTimeout = 10000;

    private int maxSize = 1024 * 1024;
    private InputStream unsafeStream;

    public SafeReader(InputStream unsafeStream) {
        this.unsafeStream = unsafeStream;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    protected int readSize() throws IOException, TimeoutException {
        byte[] size = readData(4);
        int result = bytesToInt(size);
        LOGGER.debug("awaiting {} byte(s) as {}", result,
                StringUtils.join(ScalUtils.wrapList(size), ", "));
        return result;
    }

    public int bytesToInt(byte[] input) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(input));
        return  ois.readInt();
    }

    public byte[] readPackage() throws IOException, TimeoutException {
        int size = readSize();
        if (size > maxSize) {
            throw new IllegalStateException("expected package to big " + size);
        }
        return readData(size);
    }

    /**
     * Read strict specified number bytes of throw timeout exception
     * @param size number bytes to read
     * @return array of read bytes
     */
    public byte[] readData(int size) throws IOException, TimeoutException {
        byte[] result = new byte[size];
        int firstFree = 0;
        long inputStarted = System.currentTimeMillis();
        while (true) {
            int read = unsafeStream.read(result, firstFree, size - firstFree);
            LOGGER.debug("read {} byte(s) of {}", read, size);
            firstFree += read;
            if (read > 0) {
                inputStarted = System.currentTimeMillis();
            }
            if (firstFree == size) {
                return result;
            }

            if (System.currentTimeMillis() - inputStarted > readTimeout) {
                throw new TimeoutException("data not get in "
                        + readTimeout + " milliseconds");
            }
        }
    }
}
