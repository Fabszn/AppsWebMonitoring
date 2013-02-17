package com.app.web.monitoring;

import static org.fest.assertions.api.Assertions.*;

import com.app.web.monitoring.Helper;
import org.junit.Test;

import java.net.URL;

/**
 * .
 *
 * @author fsznajderman
 *         date :  19/01/13
 */
public class HelperTest {

    @Test
    public void testLoadFile() throws Exception {


        URL url = Helper.class.getClassLoader().getResource("fileTest.txt");
        final byte[] f = Helper.loadFile(url.getPath());

        assertThat(f).isNotEmpty();


    }

    @Test(expected = NullPointerException.class)
    public void testLoadFileNullParam() throws Exception {
        Helper.loadFile(null);
    }


    @Test
    public void testHttpQueryValidRL() throws Exception {
        assertThat(Helper.httpQuery("http://blog.fsznajderman.fr")).isTrue();

    }

    @Test
    public void testHttpQueryUnvalidRL() throws Exception {
        assertThat(Helper.httpQuery("http://blog.toto")).isFalse();
    }


}
