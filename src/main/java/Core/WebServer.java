package Core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class WebServer {

    private static Selector selector;
    private static int PORT = 80;//监听8080端口

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

            //serverSocket = new ServerSocket(PORT);
        } catch (Exception e) {
            if (selector != null)
                selector.close();
            System.out.println("无法启动HTTP服务器:" + e.getLocalizedMessage());
        }
        if (selector == null)
            System.exit(1);//无法开始服务器
        Runnable runnable = new NioListen(selector);
        new Thread(runnable, "NioListen").start();
        System.out.println("HTTP服务器正在运行,端口: " + PORT);
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
        System.out.println("这是一个java服务器，默认端口是80。");
    }

    /**
     * 启动简易 Web 服务器
     */
    public static void main(String[] args) throws IOException {
        try {
            if (args.length != 1) {
                usage();
            } else if (args.length == 1) {
                PORT = Integer.parseInt(args[0]);
            }
        } catch (Exception e) {
            System.err.println("参数输入不合理，服务器启动失败。");
        }
        new WebServer();
    }

}
