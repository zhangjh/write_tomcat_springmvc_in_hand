package com.test.tomcat.servlet.tomcat;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.test.tomcat.servlet.servlet.ServletProcess;

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
    /** 线程池最大线程数 */
    private static final int THREAD_POOL_MAX_SIZE = 200;
    /** 线程池常驻线程数 */
    private static final int THREAD_CORE_POOL_SIZE = 10;
    /** 闲置线程存活时间 */
    private static final int THREAD_KEEP_ALIVE = 30;

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
                ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Tomcat-work-%d").build();
                ExecutorService exec = new ThreadPoolExecutor(
                    THREAD_CORE_POOL_SIZE,
                    THREAD_POOL_MAX_SIZE,
                    THREAD_KEEP_ALIVE,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<Runnable>(),
                    threadFactory
                );
                exec.submit(new ServletProcess(serverSocket.accept(), urlServletMap));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
