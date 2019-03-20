package com.test.tomcat.servlet.conf;

import java.util.ArrayList;
import java.util.List;

import com.test.tomcat.servlet.servlet.MyServlet1;
import com.test.tomcat.servlet.servlet.MyServlet2;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description:
 */
public class ServletMappingConfig {

    public static final List<ServletMapping> SERVLET_MAPPINGS = new ArrayList<ServletMapping>();

    static {
        SERVLET_MAPPINGS.add(new ServletMapping(MyServlet1.class.getSimpleName(), "/test1", MyServlet1.class.getName()));
        SERVLET_MAPPINGS.add(new ServletMapping(MyServlet2.class.getSimpleName(), "/test2", MyServlet2.class.getName()));
    }
}
