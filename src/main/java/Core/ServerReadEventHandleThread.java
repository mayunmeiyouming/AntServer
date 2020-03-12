package Core;

import Http.HttpRequest;
import Http.HttpRequestParser;
import Http.HttpResponse;
import Http.MimeTypes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;

public class ServerReadEventHandleThread implements Runnable {

    private ArrayBlockingQueue<SelectionKey> queue;

    //编码器初始化
    private static CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
    private static CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

    public ServerReadEventHandleThread(ArrayBlockingQueue<SelectionKey> queue) {
        this.queue = queue;
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
            }
        }
    }

    private boolean readHandler(SelectionKey key) throws IOException {
        boolean res = true;
        if (!key.isValid() || !key.isReadable())
            return false;

        HttpRequestParser parser = new HttpRequestParser(key);
        //Http请求包
        HttpRequest requestPackage = parser.parser();

        res = response(requestPackage, key);

        key.channel().close();

        return res;
    }

    private boolean response(HttpRequest request, SelectionKey key) throws IOException {
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
            index = resource.indexOf('.');
        if (index == -1)
            contentType = "text/html";
        else {
            contentType = MimeTypes.getContentType(resource.substring(index));
        }

        HttpResponse response = new HttpResponse(key);
        response.setContentType(contentType);
        response.send(filename);
        return res;
    }

}
