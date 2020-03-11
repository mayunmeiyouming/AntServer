package Http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.StringTokenizer;

public class HttpRequestParser {
    private InputStream inputStream;
    private HttpRequest request;

    public HttpRequestParser(InputStream is) {
        this.inputStream = is;
    }

    public HttpRequest parser() throws IOException {
        request = new HttpRequest();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        System.out.println("解析请求信息 begin: ***************");
        // 读取第一行, 请求地址
        String line = in.readLine();
        System.out.println(line);
        // 获取请求方法, GET 或者 POST
        String method = new StringTokenizer(line).nextElement().toString();
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
            if ("".equals(line))
                break;  //读到尾部即跳出
            parserRequestHeader(line);
        }

        //解析请求数据
        if ("POST".equals(method) && Integer.valueOf(request.getContentLength()) != 0) {
            while ((line = in.readLine()) != null) {
                parserParameter(line);
            }
        }

        System.out.println("解析请求信息 end: ***************");
        System.out.println("用户请求的资源是: " + resource);
        System.out.println("请求的类型是: " + method);
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
}
