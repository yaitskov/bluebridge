package com.github.bluebridge;

import junit.framework.Assert;
import mockit.Expectations;
import mockit.Injectable;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/4/13
 * Time: 12:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class SafeReaderTest {


    @Test
    public void testWriteSize() throws IOException {

        SafeReader sr = new SafeReader(null);

        Assert.assertEquals(123, sr.bytesToInt(new byte[]{0, 0, 0, 123}));
        Assert.assertEquals(0x2345, sr.bytesToInt(new byte[]{0, 0, 0x23, 0x45}));
        Assert.assertEquals(123, sr.bytesToInt(new byte[] {0,0,0,123}));
        Assert.assertEquals(123, sr.bytesToInt(new byte[]{0, 0, 0, 123}));


    }
}
