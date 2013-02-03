package com.github.bluebridge;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.Mocked;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/4/13
 * Time: 12:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class SafeWriterTest {


    @Test
    public void testWriteSize(@Injectable final OutputStream os) throws IOException {

        SafeWriter sw = new SafeWriter(os);

        new Expectations() {
            {
                os.write(withEqual(new byte[] {0,0,0,123}));
                os.write(withInstanceOf(byte[].class));

                os.write(withEqual(new byte[] {0,0,0x23,0x45}));
                os.write(withInstanceOf(byte[].class));
            }
        };

        sw.write(new byte[123]);
        sw.write(new byte[0x2345]);


    }
}
