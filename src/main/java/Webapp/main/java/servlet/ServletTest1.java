package Webapp.main.java.servlet;

import Http.HttpRequest;
import Http.HttpResponse;
import Loader.HttpServlet;

import java.io.IOException;

public class ServletTest1 extends HttpServlet {

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) throws IOException {
        response.setContentType("text/html");
        response.sendRedirect("test2.html");
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) throws IOException {
        // 设置:响应内容类型
        response.setContentType("text/html");

        // 输出文本
        //response.sendRedirect("test2.html");
        request.getRequestDispatcher("test2.html").forward(request, response);
    }
}
