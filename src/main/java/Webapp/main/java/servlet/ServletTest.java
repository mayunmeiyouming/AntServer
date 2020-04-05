package Webapp.main.java.servlet;

import Http.Cookie;
import Http.HttpRequest;
import Http.HttpResponse;
import Loader.HttpServlet;

import java.io.IOException;
import java.util.Arrays;

public class ServletTest extends HttpServlet {

    private String message;

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) throws IOException {

    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) throws IOException {
        // 设置:响应内容类型
        response.setContentType("text/html");
        //System.out.println("hello");

        System.out.println("收到请求：");
        System.out.println(Arrays.toString(request.getCookies()));
        Cookie cookie = new Cookie("hw", "hello");
        cookie.setMaxAge(60);
        response.setCookies(cookie);
        Cookie cookie1 = new Cookie("hw2", "fswejf");
        response.setCookies(cookie1);
        // 输出文本
        response.write("WebContent/index.html");
    }

}
