package Core;

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
    private ExecutorService service;

    //事件处理
    //private ServerEventHandler handler = new ServerEventHandler();

    public NioListen(Selector selector) {
        NioListen.selector = selector;
        int nThreads = Runtime.getRuntime().availableProcessors();
        service = new ThreadPoolExecutor(nThreads, nThreads * 2,
                60, TimeUnit.SECONDS, new ArrayBlockingQueue(64));

    }

    //监听器
    private void listen() {
        while (true) {
            try {
                selector.select();
                Iterator it = selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    //System.out.println(key);
                    handleKey(key);
                    it.remove();//确保不重复处理
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    //处理方法
    private void handleKey(SelectionKey key)
            throws IOException {
        SocketChannel channel = null;

        try {
            if (key.isAcceptable()) {
                channel = ServerEventHandler.acceptableHanler(selector, key);
            } else if (key.isReadable()) {
                ServerEventHandler handler = new ServerEventHandler(key);
                service.submit(handler);
            } else if (!key.isValid()) {
                channel = (SocketChannel) key.channel();
                System.out.println("channel被终止");
                channel.close();
            }
        } catch (Exception e) {
            key.cancel();
        }


    }

    public void run() {
        listen();
    }
}
