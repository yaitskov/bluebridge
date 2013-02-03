package com.github.bluebridge;

import org.apache.commons.lang3.StringUtils;
import org.dan.lastjcl.ScalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

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
    private OutputStream unsafeStream;

    public SafeWriter(OutputStream unsafeStream) {
        this.unsafeStream = unsafeStream;
    }

    protected void writeSize(int size) throws IOException {
        byte[] output = new byte[4];
        output[0] = (byte) (size >>> 24);
        output[1] = (byte) ((size & 0xff0000) >>> 16);
        output[2] = (byte) ((size & 0xff00) >>> 8);
        output[3] = (byte) (size & 0xff);
        LOGGER.debug("write {} byte(s) as {}", size,
                StringUtils.join(ScalUtils.wrapList(output), ", "));
        unsafeStream.write(output);
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
