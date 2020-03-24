package Http;

import java.io.*;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.StringTokenizer;

public class HttpRequestParser {
    private SelectionKey key;
    private HttpRequest request;

    // 解析请求的数据区需要的变量
    private String boundary = null;
    private String boundaryEnd = null;
    private MultipartContent content = null;
    private ByteArrayOutputStream os = null;
    private boolean isKey = true;
    private boolean isFile = false;
    private String mapKey = null;
    private FileContent fileContent = null;

    boolean headEnd = false;    //false代表头部没有被解析完成
    boolean header = false;     //false代表请求行没有被解析完成
    String method = null;
    String line = null;

    public HttpRequestParser(SelectionKey key) {
        this.key = key;
    }

    public HttpRequest parser() throws IOException {
        request = new HttpRequest();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(102400);
        readBuffer.clear();

        while (socketChannel.read(readBuffer) > 0 || readBuffer.position() != 0) {
            readBuffer.flip();
            ByteBuffer buffer = handleByteBuffer(readBuffer);
            ByteBuffer temp = ByteBuffer.allocate(buffer.limit());
            while (buffer.position() != buffer.limit()) {
                byte b = buffer.get();
                temp.put(b);
                if (b == 10) {
                    handleStream(temp);
                }
            }
            if (temp.position() != 0) {
                handleStream(temp);
            }
        }
        request.setMultipartContent(content);
        test(); //测试http请求解析
        //System.out.println("文件类型: " + request.getContentType());
        return request;
    }

    private void handleStream(ByteBuffer temp) throws IOException {
        temp.flip();
        byte[] bytes = temp.array();
        int limit = temp.limit();
        Charset charset = StandardCharsets.UTF_8;
        CharBuffer charBuffer = charset.decode(temp);
        String data = charBuffer.toString();
        InputStream is = new ByteArrayInputStream(data.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        if (headEnd == false) {
            //System.out.println(Thread.currentThread().getName() + "解析请求头信息 begin: ***************");

            line = in.readLine();
            if ("".equals(line)) {  //请求头解析完成
                System.out.println("用户请求的资源是: " + request.getResource());
                System.out.println("请求的类型是: " + request.getMethod());
                System.out.println("ContentType: " + request.getContentType());
                System.out.println("请求头解析完成");
                headEnd = true;
                temp.clear();
                return;
            }
            if (header == false) {   //解析请求行
                System.out.println("header: " + line);
                // 获取请求方法, GET 或者 POST
                method = new StringTokenizer(line).nextElement().toString();
                request.setMethod(method);

                //获得请求的资源的地址
                String url = line.substring(line.indexOf('/') + 1, line.lastIndexOf('/') - 5);
                url = URLDecoder.decode(url, "UTF-8");//反编码 URL 地址
                String resource = "";
                int i = url.indexOf("/?");
                if (i > 0)
                    resource = url.substring(0, i);
                else if (i == -1)
                    resource = url;
                request.setResource(resource);

                //解析GET请求的url参数
                if (i > 0 && "GET".equals(method)) {
                    String str = url.substring(i + 2);
                    parserParameter(str);
                }
                String protocol = line.substring(line.lastIndexOf(' ') + 1);
                request.setProtocol(protocol);
                header = true;
            } else {
                // 读取所有浏览器发送过来的请求参数头部信息
                parserRequestHeader(line);
            }

            //System.out.println(Thread.currentThread().getName() + "解析请求头信息 end: ***************");

        }

        if (headEnd && request.isMultipartContent()) {
            //解析请求数据
            //System.out.println("解析请求数据");
            parserContent(bytes, in, limit);
        } else if (headEnd && !request.isMultipartContent() && "POST".equals(request.getMethod())) {
            //解析普通post请求
            System.out.println("解析简单post");

            line = in.readLine();
            parserParameter(line);
            System.out.println("完成解析post");
        }

        temp.clear();
    }

    private boolean parserParameter(String line) {
        String[] strs = line.split("&");
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            int pIndex = str.indexOf('=');
            if (pIndex == -1 || pIndex == 0)
                continue;
            String key = str.substring(0, pIndex);
            String value = "";
            if (pIndex != str.length() - 1)
                value = str.substring(pIndex + 1);
            try {
                String k = URLDecoder.decode(value, "UTF-8");
                value = k;
            } catch (UnsupportedEncodingException e) {

            }
            System.out.println("请求参数: " + key + " " + value);
            request.setParameter(key, value);
        }
        return true;
    }

    private boolean parserRequestHeader(String line) {
        int index = line.indexOf(':');
        String title = line.substring(0, index);
        String value = line.substring(index + 2);
        System.out.println("HTTP参数: " + line);
        if ("Host".equals(title)) {
            request.setHost(value);
        } else if ("Connection".equals(title)) {
            request.setConnection(value);
        } else if ("Pragma".equals(title)) {
            request.setPragma(value);
        } else if ("Cache-Control".equals(title)) {
            request.setCacheControl(value);
        } else if ("User-Agent".equals(title)) {
            request.setUserAgent(value);
        } else if ("Sec-Fetch-Dest".equals(title)) {
            request.setSecFetchDest(value);
        } else if ("Sec-Fetch-Site".equals(title)) {
            request.setSecFetchSite(value);
        } else if ("Sec-Fetch-Mode".equals(title)) {
            request.setSecFetchMode(value);
        } else if ("Accept".equals(title)) {
            request.setAccept(value);
        } else if ("Referer".equals(title)) {
            request.setReferer(value);
        } else if ("Accept-Encoding".equals(title)) {
            request.setAcceptEncoding(value);
        } else if ("Accept-Language".equals(title)) {
            request.setAcceptLanguage(value);
        } else if ("Upgrade-Insecure-Requests".equals(title)) {
            request.setUpgradeInsecureRequests(value);
        } else if ("Sec-Fetch-User".equals(title)) {
            request.setSecFetchUser(value);
        } else if ("Content-Length".equals(title)) {
            request.setContentLength(value);
        } else if ("Origin".equals(title)) {
            request.setOrigin(value);
        } else if ("Content-Type".equals(title)) {
            request.setContentType(value);
        } else {
            System.out.println(title + "参数未被支持");
        }
        return true;
    }

    private void parserContent(byte[] b, BufferedReader in, int limit) throws IOException {
        String line = null;
        String contentType = request.getContentType();
        if (boundary == null) {
            request.setContentType("multipart/form-data");
            content = new MultipartContent();
            os = new ByteArrayOutputStream();
            boundary = "--" + contentType.substring(contentType.indexOf("=") + 1);
            boundaryEnd = boundary + "--";
        }

        //System.out.println("请求包含文件");
        line = in.readLine();
        if (b.length == 2) {
            System.out.println("空字符");
        }
        if (line == null && b.length != 0) {

            if (!isKey && isFile) {
                os.write(b, 0, limit);
            }
            return;
        }
        if (line != null) {
            if (boundary.equals(line) || boundaryEnd.equals(line)) { //处理界限
                if (isFile) {
                    byte[] bytes = os.toByteArray();
                    bytes = Arrays.copyOfRange(bytes, 0, bytes.length - 2);
                    InputStream input = new ByteArrayInputStream(bytes);
                    os.reset();
                    fileContent.setInputStream(input);
                    content.setFile(fileContent);
                    fileContent = null;

                }
                isKey = true;
                isFile = false;
                return;
            } else if ("".equals(line) && isKey) {
                isKey = false;
                return;
            }

            if (isKey) {    // 参数头
                if (line.contains("Content-Disposition")) {
                    String[] strings = line.substring(line.indexOf("name")).split("; ");
                    for (int i = 0; i < strings.length; i++) {
                        String[] str = strings[i].split("=");
                        if (strings.length == 1) {
                            content.setParameter(str[1], null);
                            mapKey = str[1];
                        } else {
                            if (fileContent == null)
                                fileContent = new FileContent();
                            if ("name".equals(str[0]))
                                fileContent.setName(str[1].substring(1, str[1].length() - 1));
                            else if ("filename".equals(str[0])) {
                                isFile = true;
                                fileContent.setFilename(str[1].substring(1, str[1].length() - 1));
                            }
                        }
                    }
                    System.out.println(line);
                } else if (line.contains("Content-Type")) {
                    String[] strs = line.split(": ");
                    fileContent.setContentType(strs[1]);
                    System.out.println(line);
                }

            } else {  //参数值
                if (!isFile) {
                    content.setParameter(mapKey, line);
                } else {
                    os.write(b, 0, limit);
                }
                //System.out.println(line);
            }
        }
    }

    private ByteBuffer handleByteBuffer(ByteBuffer byteBuffer) {
        ByteBuffer buffer = ByteBuffer.allocate(byteBuffer.limit());
        ByteBuffer temp = ByteBuffer.allocate(byteBuffer.limit());
        buffer.clear();
        temp.clear();

        while (byteBuffer.position() != byteBuffer.limit()) {
            byte b = byteBuffer.get();
            temp.put(b);
            if (b == 10) {
                temp.flip();
                buffer.put(temp);
                temp.clear();
            }
        }
        if (temp.position() != 0) {
            temp.flip();
            if (byteBuffer.capacity() != byteBuffer.limit()) {
                //System.out.println("最后一行");
                buffer.put(temp);
                byteBuffer.clear();
            } else {
                //System.out.println("数据下移");
                byteBuffer.clear();
                byteBuffer.put(temp);
                //System.out.println("temp的位置: " + temp.position());
                //System.out.println("数据位置: " + byteBuffer.position());
                //System.out.println("数据limit: " + byteBuffer.limit());
            }
            temp.clear();
        } else {
            byteBuffer.clear();
        }
        temp = null;
        buffer.flip();
        return buffer;
    }

    /*
     * 测试http请求解析
     */
    private void test() throws IOException {
        System.out.println("test begin");
        System.out.println(request.getMultipartContent());
        MultipartContent multipartContent = request.getMultipartContent();
        if (multipartContent != null) {
            FileContent fileContent1 = multipartContent.getFile("filehw");
            if (fileContent1 != null) {
                InputStream inputStream = fileContent1.getInputStream();
                if (inputStream == null)
                    return;
                String line = null;
                if (fileContent1.getFilename().equals("") || fileContent1.getFilename() == null)
                    return;
                File file = new File("WebContent/" + fileContent1.getFilename());
                OutputStream outputStream = new FileOutputStream(file);
                byte[] bytes2 = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(bytes2)) > 0) {
                    outputStream.write(bytes2, 0, len);
                }
                outputStream.close();
                //inputStream.close();
            }
        }
        System.out.println("test end");
    }
}
