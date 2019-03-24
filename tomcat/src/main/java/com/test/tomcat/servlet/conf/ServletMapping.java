package com.test.tomcat.servlet.conf;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description:
 */
public class ServletMapping {

    private String servletName;

    private String url;

    private String clazz;;

    public ServletMapping(String servletName, String url, String clazz) {
        this.servletName = servletName;
        this.url = url;
        this.clazz = clazz;
    }

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}
