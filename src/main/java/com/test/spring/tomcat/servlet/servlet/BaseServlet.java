package com.test.spring.tomcat.servlet.servlet;

import com.test.spring.tomcat.servlet.domain.Request;
import com.test.spring.tomcat.servlet.domain.Response;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description:
 */
public abstract class BaseServlet {

    void service(Request request, Response response) {
        if("get".equalsIgnoreCase(request.getMethod())) {
            this.doGet(request, response);
        }else {
            this.doPost(request, response);
        }
    }

    public abstract void doGet(Request request, Response response);

    public abstract void doPost(Request request, Response response);
}
