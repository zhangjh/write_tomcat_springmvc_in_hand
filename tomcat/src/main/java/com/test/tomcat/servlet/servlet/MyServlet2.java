package com.test.tomcat.servlet.servlet;

import com.test.tomcat.servlet.domain.Request;
import com.test.tomcat.servlet.domain.Response;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description:
 */
public class MyServlet2 extends BaseServlet {

    @Override
    public void doGet(Request request, Response response) {
        System.out.println("My servlet 2 doGet");
       this.doPost(request, response);
    }

    @Override
    public void doPost(Request request, Response response) {
        System.out.println("My servlet 2 doPost");
        response.outPrint("This is My servlet 2");
    }

}
