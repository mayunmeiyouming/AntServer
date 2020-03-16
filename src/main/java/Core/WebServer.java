package Core;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.List;

public class WebServer {

    private static Selector selector;
    private static int PORT = 80;//默认监听80端口

    // 开始服务器 Socket 线程.
    public WebServer() throws IOException {
        ServerSocketChannel servSocketChannel;
        try {
            servSocketChannel = ServerSocketChannel.open();
            servSocketChannel.configureBlocking(false);
            //绑定端口
            servSocketChannel.socket().bind(new InetSocketAddress(PORT));
            //Select 创建
            selector = Selector.open();
            //向Selector 注册通道（用户监控单一事件）
            servSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            if (selector != null)
                selector.close();
            System.out.println("无法启动HTTP服务器:" + e.getLocalizedMessage());
        }
        if (selector == null)
            System.exit(1);//无法开始服务器
        Runnable runnable = new NioListen(selector);
        new Thread(runnable, "NioListen").start();
    }

    //关闭客户端 socket 并打印一条调试信息.
    void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(socket.getInetAddress() + " Port: " + socket.getPort() + " 离开了HTTP服务器");
    }

    //命令行打印用途说明.
    private static void usage() {
        System.out.println("This is Ant Server, Welcome to use it.");
        System.out.println("Ant Server 监听 " + PORT + " 端口.");
        System.out.println();
    }

    private static void conf() throws IOException, JDOMException {
        // 通过SAXBuilder解析xml
        SAXBuilder builder = new SAXBuilder();

        File file = new File("WebContent/conf/server.xml");
        Document doc = builder.build(file);
        Element root = doc.getRootElement(); // 获取根元素

        Element service = root.getChild("Service");
        Element connector = service.getChild("Connector");

        Attribute attribute = connector.getAttribute("port");
        PORT = Integer.valueOf(attribute.getValue());
    }

    /**
     * Starting Ant Server
     */
    public static void main(String[] args) throws IOException, JDOMException {
        conf();
        usage();
        new WebServer();
    }

}
