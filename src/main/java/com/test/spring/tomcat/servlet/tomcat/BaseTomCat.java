package com.test.spring.tomcat.servlet.tomcat;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;

import com.test.spring.tomcat.servlet.servlet.ServletProcess;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description: 抽象类Tomcat，用来给两种初始化方式的Tomcat继承
 */
abstract class BaseTomCat {
    /** 启动端口 */
    private int port;
    /** url入口和具体servlet的映射 */
    protected Map<String, String> urlServletMap;

    public BaseTomCat(int port) {
        this.port = port;
    }

    /** 由子类实现不同的初始化方法，支持xml和JAVA配置 */
    abstract void init();

    void start() {
        this.init();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Tomcat 已启动， 地址：localhost, 端口：" + port);

            // 监听，处理任务
            while (true) {
                new ServletProcess(serverSocket.accept(), urlServletMap).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
