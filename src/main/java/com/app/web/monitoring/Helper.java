package com.app.web.monitoring;

import com.google.common.base.Preconditions;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * .
 *
 * @author fsznajderman
 *         date :  17/01/13
 */
public class Helper {


    public static byte[] loadFile(final String path) {

        Preconditions.checkNotNull(path);

        InputStream fis = null;
        try {
            System.out.println(path);
            fis = new FileInputStream(path);

            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] byteArray = new byte[5121];
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
