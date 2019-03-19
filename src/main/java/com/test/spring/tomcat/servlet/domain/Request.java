package com.test.spring.tomcat.servlet.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description:
 */
public class Request {

    private String method;

    private String url;

    public Request(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = bufferedReader.readLine();
        if(line != null) {
            String[] methodAndUrl = line.split(" ");
            this.method = methodAndUrl[0];
            this.url = methodAndUrl[1];

            System.out.println(method);
            System.out.println(url);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
