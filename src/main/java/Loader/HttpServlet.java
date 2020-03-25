package Loader;

import Http.HttpRequest;
import Http.HttpResponse;

import java.io.IOException;

public abstract class HttpServlet {

    public void init() {
        System.out.println("init");
    }

    protected void service(HttpRequest request, HttpResponse response) throws IOException {
        if (request != null) {
            String method = request.getMethod();
            if (method.equals("POST"))
                doPost(request, response);
            else if (method.equals("GET"))
                doGet(request, response);
        }
    }

    protected abstract void doPost(HttpRequest request, HttpResponse response) throws IOException;

    protected abstract void doGet(HttpRequest request, HttpResponse response) throws IOException;

}
