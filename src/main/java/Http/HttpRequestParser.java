package Http;

import java.io.*;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
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


    public HttpRequestParser(SelectionKey key) {
        this.key = key;
    }

    public HttpRequest parser() throws IOException {
        request = new HttpRequest();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(5500);
        readBuffer.clear();
        boolean headEnd = false;
        String method = null;
        String line = null;

        while (socketChannel.read(readBuffer) > 0) {
            readBuffer.flip();
            Charset charset = Charset.forName("UTF-8");
            CharBuffer charBuffer = charset.decode(readBuffer);
            String data = charBuffer.toString();
            InputStream is = new ByteArrayInputStream(data.getBytes());
            BufferedReader in = new BufferedReader(new InputStreamReader(is));

            if (headEnd == false) {
                System.out.println(Thread.currentThread().getName() + "解析请求头信息 begin: ***************");
                // 读取第一行, 请求地址
                line = in.readLine();
                System.out.println(line);
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

                String version = line.substring(line.lastIndexOf(' ') + 1);
                request.setHttpVersion(version);

                // 读取所有浏览器发送过来的请求参数头部信息
                while ((line = in.readLine()) != null) {
                    if ("".equals(line)) {    //请求头解析完成
                        headEnd = true;
                        break;
                    }

                    parserRequestHeader(line);
                }

                System.out.println(Thread.currentThread().getName() + "解析请求头信息 end: ***************");
                System.out.println("用户请求的资源是: " + resource);
                System.out.println("请求的类型是: " + method);
            }

            //解析请求数据
            if ("POST".equals(method) && Integer.valueOf(request.getContentLength()) != 0) {
                parserContent(in);
            }

            readBuffer.clear();
        }
        request.setMultipartContent(content);
        //System.out.println("文件类型: " + request.getContentType());
        return request;
    }
    
    private boolean parserParameter(String line) {
        String[] strs = line.split("&");
        for (int i = 0 ; i < strs.length ; i ++) {
            String str = strs[i];
            int pIndex = str.indexOf('=');
            if (pIndex == -1 || pIndex == 0)
                continue;
            String key = str.substring(0, pIndex);
            String value = "";
            if (pIndex != str.length() - 1)
                value = str.substring(pIndex + 1);
            request.setParameter(key, value);
        }
        return true;
    }

    private boolean parserRequestHeader(String line) {
        int index = line.indexOf(':');
        String title = line.substring(0, index);
        String value = line.substring(index + 2);
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

    private void parserContent(BufferedReader in) throws IOException {
        String line = null;
        String contentType = request.getContentType();
        if (contentType.contains("multipart/form-data")) {  //解析带文件的post请求
            if (boundary == null) {
                request.setContentType("multipart/form-data");
                content = new MultipartContent();
                os = new ByteArrayOutputStream();
                boundary = "--" + contentType.substring(contentType.indexOf("=") + 1);
                boundaryEnd = boundary + "--";
            }

            System.out.println("请求包含文件");
            while ((line = in.readLine()) != null) {
                if (boundary.equals(line) || boundaryEnd.equals(line)) {
                    System.out.println(fileContent);
                    if (isFile) {
                        byte[] bytes = os.toByteArray();
                        os.reset();
                        InputStream input = new ByteArrayInputStream(bytes);
                        fileContent.setInputStream(input);
                        content.setFile(fileContent);
                        fileContent = null;
                    } else if (!isKey) {
                        content.setParameter(mapKey, line);
                    }
                    isKey = true;
                    isFile = false;
                    continue;
                } else if ("".equals(line)) {
                    isKey = false;
                    continue;
                }

                if (isKey) {
                    if (line.contains("Content-Disposition")) {
                        String[] strings = line.substring(line.indexOf("name")).split("; ");
                        for (int i = 0 ; i < strings.length ; i ++) {
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
                } else {
                    if (!isFile) {
                        content.setParameter(mapKey, line);
                    }
                    else {
                        os.write(line.getBytes());
                    }
                    System.out.println(line);
                }
            }
        } else {  //解析普通post请求
            while ((line = in.readLine()) != null) {
                System.out.println("POST: " + line);
                parserParameter(line);
            }
        }
    }
}
