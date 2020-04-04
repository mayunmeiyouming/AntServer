package Core;

import Http.HttpRequest;
import Http.HttpRequestParser;
import Http.HttpResponse;
import Http.MimeTypes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ArrayBlockingQueue;

public class ServerReadEventHandleThread implements Runnable {

    private ArrayBlockingQueue<SelectionKey> queue;

    private HttpRequest request;
    private HttpResponse response;
    private HttpDispatcher dispatcher;

    public ServerReadEventHandleThread(ArrayBlockingQueue<SelectionKey> queue,
                                       HttpDispatcher dispatcher) {
        this.queue = queue;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        while (true) {
            try {
                SelectionKey key = queue.take();
                if (key.isValid() && key.isReadable()) {
                    synchronized (key) {
                        if (key.isValid() && key.isReadable())
                            readHandler(key);
                    }
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean readHandler(SelectionKey key) throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        boolean res = true;
        if (!key.isValid() || !key.isReadable())
            return false;

        HttpRequestParser parser = new HttpRequestParser(key);
        //Http请求包
        request = parser.parser();

        response = new HttpResponse(key);

        // 只对有隐写url的请求进行分发
        res = dispatcher.dispatch(request.getResource(), request, response);

        if (res == false) {   // 直接访问页面
            res = response(key);
        }

        key.channel().close();

        return res;
    }

    private boolean response(SelectionKey key) throws IOException {
        boolean res = false;
        String method = request.getMethod();
        String resource = request.getResource();
        String filename = "WebContent/";
        if ("".equals(resource))
            filename += "index.html";
        else
            filename += resource;

        String contentType = "";
        int index = -1;
        if (!"".equals(resource))
            index = resource.lastIndexOf('.');
        if (index == -1)
            contentType = "text/html";
        else {
            contentType = MimeTypes.getContentType(resource.substring(index));
        }

        response = new HttpResponse(key);
        response.setContentType(contentType);
        response.write(filename);
        return res;
    }

}
