package dto;



import org.junit.Test;

import static org.fest.assertions.api.Assertions.*;

/**
 * .
 *
 * @author fsznajderman
 *         date :  19/01/13
 */
public class AppMonitoredTest {

    @Test
    public void testToString() throws Exception {


        AppMonitored am = new AppMonitored("un", "deux", "3");
        assertThat(extractProperty("JSon").from(new Object[]{am})).containsExactly("{\"key\":\"un\",\"name\":\"deux\",\"url\":\"3\"}");


    }
}
