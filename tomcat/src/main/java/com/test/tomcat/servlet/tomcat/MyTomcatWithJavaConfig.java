package com.test.tomcat.servlet.tomcat;

import java.util.HashMap;

import com.test.tomcat.servlet.conf.ServletMapping;
import com.test.tomcat.servlet.conf.ServletMappingConfig;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description: 使用Java配置方式初始化的TomCat
 */
public class MyTomcatWithJavaConfig extends BaseTomCat {

    public MyTomcatWithJavaConfig(int port) {
        super(port);
    }

    @Override
    public void init() {
        this.urlServletMap = new HashMap<String, String>();
        try {
            System.out.println("加载配置文件开始");

            // Java class方式
            for (ServletMapping mapping : ServletMappingConfig.SERVLET_MAPPINGS) {
                this.urlServletMap.put(mapping.getUrl(), mapping.getClazz());
            }

            System.out.println("加载配置文件结束");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyTomcatWithJavaConfig myTomcat = new MyTomcatWithJavaConfig(8081);

        myTomcat.start();
    }
}
