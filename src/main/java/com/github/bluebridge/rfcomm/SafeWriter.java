package com.github.bluebridge.rfcomm;

import org.apache.commons.lang3.StringUtils;
import org.dan.lastjcl.ScalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/3/13
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class SafeWriter extends OutputStream {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SafeWriter.class);
    private DataOutputStream unsafeStream;

    public SafeWriter(OutputStream unsafeStream) {
        this.unsafeStream = new DataOutputStream(unsafeStream);
    }

    protected void writeSize(int size) throws IOException {
        byte[] packed = ByteBuffer.allocate(4).putInt(size).array();
        LOGGER.debug("write {} byte(s) as {}", size,
                StringUtils.join(ScalUtils.wrapList(packed), ","));
        unsafeStream.write(packed);
    }

    @Override
    public void write(int i) throws IOException {
        writeSize(1);
        unsafeStream.write(i);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        writeSize(bytes.length);
        unsafeStream.write(bytes);
    }

    @Override
    public void write(byte[] bytes, int ofs, int len) throws IOException {
        writeSize(len);
        unsafeStream.write(bytes, ofs, len);
    }

    @Override
    public void flush() throws IOException {
        unsafeStream.flush();
    }

    @Override
    public void close() throws IOException {
        unsafeStream.close();
    }
}
