package AppsWebMonitoring;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * .
 *
 * @author fsznajderman
 *         date :  17/01/13
 */
public class Helper {


    private static final String GET = "GET";

    public static byte[] loadFile(final String path) {

        Preconditions.checkNotNull(path);

        FileInputStream fis = null;
        try {

            final URL url = AppMonitoring.class.getClassLoader().getResource(path);

            File file;
            file = new File(url.toURI());

            byte[] byteArray = new byte[(int) file.length()];
            fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(byteArray, 0, byteArray.length);
            return byteArray;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static boolean httpQuery(final String url) throws Exception {
        Preconditions.checkNotNull(url);
        HttpURLConnection server = null;
        try {
            final URL u = new URL(url);

            server = (HttpURLConnection) u.openConnection();
            server.setRequestMethod(AppMonitoredConstant.GET);
            server.setConnectTimeout(5000);
            server.connect();

            final int response = server.getResponseCode();
            return response == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            return Boolean.FALSE;
        } finally {
            if (server != null) {
                server.disconnect();
            }
        }

    }
}
