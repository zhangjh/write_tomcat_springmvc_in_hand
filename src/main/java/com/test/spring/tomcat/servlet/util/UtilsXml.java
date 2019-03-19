package com.test.spring.tomcat.servlet.util;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description:
 */
public class UtilsXml {

    private Document document;

    public UtilsXml(String path) {
        SAXReader saxReader = new SAXReader();

        try {
            document = saxReader.read(path);
        }catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Element> getNodes(String name) {
        Element root = document.getRootElement();
        return root.elements(name);
    }

}
