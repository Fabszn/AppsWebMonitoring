package com.app.web.monitoring.dto;

/**
 * .
 *
 * @author fsznajderman
 *         date :  19/01/13
 */
public class AppMonitored {

    private String key;
    private String name;
    private String url;


    public AppMonitored(final String key, final String name, final String url) {
        this.key = key;
        this.name = name;
        this.url = url;
    }


    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getJSon() {
        final StringBuilder sb = new StringBuilder();

        sb.append("{\"key\"").append(":").append("\""+key+"\"");
        sb.append(",");
        sb.append("\"name\"").append(":").append("\""+name+"\"");
        sb.append(",");
        sb.append("\"url\"").append(":").append("\""+url+"\"");

        sb.append('}');
        return sb.toString();


    }



}
