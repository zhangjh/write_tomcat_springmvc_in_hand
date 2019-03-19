package com.test.spring.tomcat.servlet.servlet;

import com.test.spring.tomcat.servlet.domain.Request;
import com.test.spring.tomcat.servlet.domain.Response;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description:
 */
public class MyServlet1 extends BaseServlet {

    @Override
    public void doGet(Request request, Response response) {
        System.out.println("My servlet 1 doGet");
        this.doPost(request, response);
    }

    @Override
    public void doPost(Request request, Response response) {
        System.out.println("My servlet 1 doPost");
        response.outPrint("This is My first servlet 1");
    }

}
