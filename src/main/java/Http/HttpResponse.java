package Http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class HttpResponse extends HttpResponsePackage {

    private SelectionKey key;
    private SocketChannel channel;

    //http 响应字段
    private static String NEWLINE = "\r\n";

    private CharBuffer headerBuffer = CharBuffer.allocate(1024);
    private static CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();

    public HttpResponse(SelectionKey key) {
        this.key = key;
        this.channel = (SocketChannel) key.channel();
    }

    public void write(String filename) throws IOException {
        File file = new File(filename);
        if (file.exists() && !file.isDirectory()) {
            setStatus(200);
            setContentLength(file.length());
        } else {
            setStatus(404);
        }
        commitResponseHeader();
        if (statusCode == 200 || statusCode == 302) {
            sendData(file);
        } else if (statusCode == 404) {
            file = new File("WebContent/404/404.html");
            sendData(file);
        }
    }

    private void sendData(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        FileChannel fileChannel = inputStream.getChannel();
        ByteBuffer data = ByteBuffer.allocate(1024);
        try {
            while (fileChannel.read(data) > 0) {
                data.flip();
                while (data.hasRemaining())
                    channel.write(data);
                data.clear();
            }
        } finally {
            fileChannel.close();
            inputStream.close();
        }
    }

    public void commitResponseHeader() throws IOException {
        headerBuffer.clear();
        String head = getHead();
        headerBuffer.put(head);
        Set<Map.Entry<String, String>> entries = getEntries();
        for (Map.Entry<String, String> entry : entries) {
            appendHeaderValue(entry.getKey(), entry.getValue());
        }
        appendCookie();
        headerBuffer.put(NEWLINE);
        headerBuffer.flip();
        channel.write(encoder.encode(headerBuffer));
    }

    private void appendHeaderValue(String name, String value) {
        headerBuffer.put(name);
        headerBuffer.put(": ");
        headerBuffer.put(value);
        headerBuffer.put(NEWLINE);
    }

    private void appendCookie() {
        Cookie[] cookies = getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String value = cookie.getName() + "=" + cookie.getValue();
                int timeout = cookie.getMaxAge();
                if (timeout > 0) {
                    value += "; " + "Max-Age=" + timeout + "; ";
                    Date now = new Date();
                    long time = timeout * 1000;//秒
                    Date afterDate = new Date(now.getTime() + time);
                    value += "Expires=" + afterDate.toString();
                }
                String domain = cookie.getDomain();
                if (domain != null && !"".equals(domain))
                    value += "; " + "Domain=" + domain;
                String path = cookie.getPath();
                if (path != null && !"".equals(path))
                    value += "; " + "Path=" + path;
                String sameSite = cookie.getSameSite();
                if (sameSite != null && !"".equals(sameSite))
                    value += "; " + "SameSite=" + sameSite;
                if (cookie.getSecure())
                    value += "; " + "Secure";
                if (cookie.isHttpOnly())
                    value += "; " + "HttpOnly";
                appendHeaderValue("Set-Cookie", value);
            }
        }
    }

    public void sendRedirect(String url) throws IOException {
        setStatus(302);
        setLocation(url);
        commitResponseHeader();
    }

}
