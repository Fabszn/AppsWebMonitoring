package com.app.web.monitoring;

import static org.fest.assertions.api.Assertions.*;

import com.app.web.monitoring.Helper;
import org.junit.Test;

/**
 * .
 *
 * @author fsznajderman
 *         date :  19/01/13
 */
public class HelperTest {

    @Test
    public void testLoadFile() throws Exception {

        final byte[] f = Helper.loadFile("fileTest.txt");

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
