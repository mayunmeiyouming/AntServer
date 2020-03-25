package Core;

import Loader.ServletClassMap;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.*;

public class NioListen implements Runnable {

    private static Selector selector;
    private static HashMap<SocketChannel, ByteBuffer> map = new HashMap<>();
    private ArrayBlockingQueue<SelectionKey> queue;

    public NioListen(Selector selector, ServletClassMap map) {
        NioListen.selector = selector;
        int nThreads = Runtime.getRuntime().availableProcessors();
        queue = new ArrayBlockingQueue(256);
        for (int i = 0 ; i < nThreads * 2 ; i ++) {
            RequestDispatcher dispatcher = new RequestDispatcher(map);
            Runnable r = new ServerReadEventHandleThread(queue, dispatcher);
            new Thread(r).start();
        }
        System.out.println("启动完成");
        System.out.println();
    }

    //监听器
    private void listen() {
        while (true) {
            try {
                selector.select();
                Iterator it = selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    handleKey(key);
                    it.remove();//确保不重复处理
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    //处理方法
    private void handleKey(SelectionKey key) {

        try {
            if (key.isAcceptable()) {
                acceptableHanler(key);
            } else if (key.isReadable()) {
                queue.put(key);
            } else if (!key.isValid()) {
                System.out.println("channel被终止");
            }
        } catch (Exception e) {
            key.cancel();
        }
    }

    public boolean acceptableHanler(SelectionKey clientKey) throws IOException {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) clientKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            System.out.println("客户端连接: " + socketChannel.getRemoteAddress());
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void run() {
        listen();
    }
}
