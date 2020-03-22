package Webapp.main.java.servlet;

import Http.HttpRequest;
import Http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletTest {

    private String message;

    public void init()
    {
        //初始化
        message = "Hello, First Servlet!";
    }

    protected void doPost() throws IOException {

    }

    protected void doGet(HttpRequest request, HttpResponse response) throws IOException {
        // 设置:响应内容类型
        response.setContentType("text/html");
        System.out.println("hello");

        // 输出文本
        response.send("WebContent/index.html");
    }

}
