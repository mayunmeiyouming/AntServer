package Core;

import Http.HttpRequest;
import Http.HttpRequestParser;
import Http.HttpResponse;
import sun.reflect.annotation.ExceptionProxy;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class ServerEventHandler implements Runnable {

    //编码器初始化
    private static CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
    private static CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

    private SelectionKey key;

    public ServerEventHandler(SelectionKey key) {
        this.key = key;
    }

    public static SocketChannel acceptableHanler(Selector selector, SelectionKey clinetKey) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) clinetKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        System.out.println("客户端连接: " + socketChannel.getRemoteAddress());
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        return socketChannel;
    }

    public boolean readHandler() throws IOException {
        boolean res = true;

        if (!key.isValid())
            return false;
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(5500);
        readBuffer.clear();
        try {
            int count = socketChannel.read(readBuffer);

            if (count > 0) {
                readBuffer.flip();
                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                CharBuffer charBuffer = decoder.decode(readBuffer);
                String request = charBuffer.toString();
                InputStream is = new ByteArrayInputStream(request.getBytes());
                HttpRequestParser parser = new HttpRequestParser(is);

                //Http请求包
                HttpRequest requestPackage = parser.parser();

                res = response(requestPackage, key);
                //socketChannel.close();
            } else {
                System.out.println("读取时channel关闭");
                socketChannel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //key.cancel();
            //key.selector().wakeup();
        }


        return res;
    }

    public boolean writeHandler() {
        boolean res = true;

        return res;
    }

    private boolean response(HttpRequest requestPackage, SelectionKey key) throws IOException {
        boolean res = false;
        String method = requestPackage.getMethod();
        String resource = requestPackage.getResource();
        String filename = "WebContent/";
        if ("".equals(resource))
            filename += "index.html";
        else
            filename += resource;

        String contentType = "";
        if ((resource.equals("") || resource.endsWith(".html") || resource.endsWith(".htm")) && method.equals("GET")) {
            contentType = "text/html";
        } else if (resource.endsWith(".js") && method.equals("GET")) {
            contentType = "application/javascript";
        } else if (resource.endsWith(".css") && method.equals("GET")) {
            contentType = "text/css";
        } else if (resource.endsWith(".ico") && method.equals("GET")) {
            contentType = "image/ico";
        } else if (resource.endsWith(".jpg") && method.equals("GET")) {
            contentType = "image/jpeg";
        }

        HttpResponse response = new HttpResponse(key);
        response.setContentType(contentType);
        response.send(filename);
        return res;
    }

    @Override
    public void run() {
        synchronized (key) {
            try {
                readHandler();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
