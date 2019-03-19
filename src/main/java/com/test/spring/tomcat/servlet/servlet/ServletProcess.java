package com.test.spring.tomcat.servlet.servlet;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import com.test.spring.tomcat.servlet.domain.Request;
import com.test.spring.tomcat.servlet.domain.Response;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description:
 */
public class ServletProcess extends Thread {
    private Socket socket;

    /** 存储url路径和servlet的映射 */
    private Map<String, String> urlServletMap;

    public ServletProcess(Socket socket, Map<String, String> urlServletMap) {
        this.socket = socket;
        this.urlServletMap = urlServletMap;
    }

    private void dispatch(Request request, Response response) {

        String clazz = this.urlServletMap.get(request.getUrl());

        try {
            if(clazz == null) {
                response.outPrint("404:未找到对应servlet");
                return;
            }
            Class myServletClass = Class.forName(clazz);
            BaseServlet myServlet = (BaseServlet) myServletClass.newInstance();
            myServlet.service(request, response);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread());
            Request request = new Request(this.socket.getInputStream());
            Response response = new Response(this.socket.getOutputStream());
            this.dispatch(request, response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
