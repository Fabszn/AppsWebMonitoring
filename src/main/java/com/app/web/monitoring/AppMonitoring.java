package com.app.web.monitoring;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.sun.net.httpserver.*;
import com.app.web.monitoring.dto.AppMonitored;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Executors;

import static java.net.HttpURLConnection.*;

/**
 * .@author fsznajderman
 *         date :  17/01/13
 */
public class AppMonitoring {


    private final static Properties conf = new Properties();
    private static final String PORT = "port";
    private static final String ALL_ITEMS = "allItems";
    private static final String CHECK = "check";
    private static Map<String, AppMonitored> appMonitoreds = Maps.newHashMap();

    private static String pathConf;
    private static String pathWebFile;


    public static void main(String[] args) throws IOException {
        System.out.println("Start setup...");
        System.out.println(System.getProperty("user.dir"));
        pathConf = System.getProperty("user.dir")+"/"+args[0];
        pathWebFile = System.getProperty("user.dir")+"/"+args[1];

        loadconfiguration();
        System.out.println("... setup finished");
        launchServer();
        System.out.println("server started, listen on port : " + conf.getProperty(PORT));
    }

    private static void loadconfiguration() {
        //loading configuration
        try {

            conf.load(new FileInputStream(new File(pathConf)));


            final Collection<Object> webApps = Collections2.filter(conf.keySet(), new Predicate<Object>() {
                public boolean apply(Object o) {
                    final String currentKey = (String) o;
                    return currentKey.startsWith(AppMonitoredConstant.WEBAPP);
                }
            });

            for (Object o : webApps) {

                final String key = (String) o;
                String v = conf.getProperty(key);
                String[] vs = v.split(";");

                appMonitoreds.put(key, new AppMonitored(key, vs[1], vs[0]));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void launchServer() throws IOException {

        InetSocketAddress addr = new InetSocketAddress(Integer.valueOf(conf.getProperty(PORT)));
        HttpServer server = HttpServer.create(addr, 0);


        server.createContext("/", new HttpHandler() {


            public void handle(HttpExchange exchange) throws IOException {
                String requestMethod = exchange.getRequestMethod();
                if (requestMethod.equalsIgnoreCase(AppMonitoredConstant.GET)) {
                    Headers responseHeaders = exchange.getResponseHeaders();
                    responseHeaders.set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(Helper.loadFile(pathWebFile));
                    responseBody.close();
                }
            }
        });


        server.createContext("/" + ALL_ITEMS, new HttpHandler() {
            public void handle(HttpExchange httpExchange) throws IOException {

                Headers responseHeaders = httpExchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/json");
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);


                Collection<String> tranformed = Collections2.transform(appMonitoreds.values(), new Function<AppMonitored, String>() {
                    public String apply(AppMonitored appMonitored) {
                        return appMonitored.getJSon();
                    }
                });

                final OutputStream o = httpExchange.getResponseBody();
                o.write(("[" + Joiner.on(',').join(tranformed) + "]").getBytes());
                o.close();


            }


        });

        HttpContext ctx = server.createContext("/" + CHECK, new HttpHandler() {
            public void handle(HttpExchange httpExchange) throws IOException {

                Headers responseHeaders = httpExchange.getResponseHeaders();

                final String appKey = ((Map<String, String>) httpExchange.getAttribute(AppMonitoredConstant.PARAMS_KEY)).get(AppMonitoredConstant.URL_KEY);
                AppMonitored current = appMonitoreds.get(appKey);


                responseHeaders.set("Content-Type", "text/html");
                String result = "OK";
                try {
                    result = Helper.httpQuery(current.getUrl()) ? "OK" : "KO";
                } catch (Exception e) {
                    e.printStackTrace();
                }

                httpExchange.sendResponseHeaders(HTTP_OK, 0);
                final OutputStream o = httpExchange.getResponseBody();

                o.write((result + "-" + current.getKey()).getBytes());
                o.close();
            }
        });

        ctx.getFilters().add(new Filter() {
            @Override
            public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {

                final Map<String, String> params = Maps.newHashMap();
                final URI currentUri = httpExchange.getRequestURI();
                final String query = currentUri.getQuery();


                Iterable<String> keyValues = Splitter.on('&').split(query);

                for (String kv : keyValues) {
                    final Iterable<String> skv = Splitter.on('=').split(kv);
                    params.put(FluentIterable.from(skv).first().get(), FluentIterable.from(skv).last().get());
                }

                httpExchange.setAttribute(AppMonitoredConstant.PARAMS_KEY, params);
                chain.doFilter(httpExchange);

            }

            @Override
            public String description() {
                return "catch parameters";
            }
        });


        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("Server is listening on port 8080");
    }

}



