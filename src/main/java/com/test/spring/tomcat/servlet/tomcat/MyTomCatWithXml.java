package com.test.spring.tomcat.servlet.tomcat;

import java.util.HashMap;
import java.util.List;

import com.test.spring.tomcat.servlet.util.UtilsXml;
import org.dom4j.Element;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description: 使用web.xml方式初始化的TomCat
 */
public class MyTomCatWithXml extends BaseTomCat {

    /** web.xml配置方式下用来存储servlet */
    private static final HashMap<String, String> SERVLET = new HashMap<String, String>();

    public MyTomCatWithXml(int port) {
        super(port);
    }

    @Override
    void init() {

        this.urlServletMap = new HashMap<String, String>();
        try {
            System.out.println("加载配置文件开始");

            // web.xml方式
            UtilsXml xml = new UtilsXml(UtilsXml.class.getResource("/") + "web.xml");

            // 将servlet类存储到容器并生成对象
            List<Element> list = xml.getNodes("servlet");
            for (Element element : list) {
                SERVLET.put(element.elementText("servlet-name"), element.elementText("servlet-class"));
            }

            // 创建映射关系
            List<Element> mappings = xml.getNodes("servlet-mapping");
            for (Element mapping : mappings) {
                urlServletMap.put(mapping.elementText("url-pattern"), SERVLET.get(mapping.elementText("servlet-name")));
            }

            System.out.println("加载配置文件结束");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyTomCatWithXml myTomCat = new MyTomCatWithXml(8080);

        myTomCat.start();
    }
}
