package com.test.spring.tomcat.servlet.domain;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description:
 */
public class Response {

    private OutputStream outputStream;

    /** HTTP响应头固定格式 */
    private static final String RESPONSE_HEADER =
        "HTTP/1.1 200 \n"
        + "Content-Type: text/html\n"
        + "\n";

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void outPrint(String content) {
        StringBuilder response = new StringBuilder();

        response.append(RESPONSE_HEADER).append(content);

        try {
            this.outputStream.write(response.toString().getBytes("UTF8"));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(this.outputStream != null) {
                try {
                    this.outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
