package com.test.spring.tomcat.servlet.conf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description:
 */
public class ServletMappingConfig {

    public static final List<ServletMapping> SERVLET_MAPPINGS = new ArrayList<ServletMapping>();

    static {
        SERVLET_MAPPINGS.add(new ServletMapping("Test1", "/test1", "com.test.spring.tomcat.servlet.servlet.MyServlet1"));
        SERVLET_MAPPINGS.add(new ServletMapping("Test2", "/test2", "com.test.spring.tomcat.servlet.servlet.MyServlet2"));
    }
}
