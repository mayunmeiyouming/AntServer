package Http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.StringTokenizer;

public class HttpRequestParser {
    private InputStream inputStream;

    public HttpRequestParser(InputStream is) {
        this.inputStream = is;
    }

    public HttpRequest parser() throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        System.out.println("解析请求信息 begin: ***************");
        // 读取第一行, 请求地址
        String line = in.readLine();
        System.out.println(line);
        // 获取请求方法, GET 或者 POST
        String method = new StringTokenizer(line).nextElement().toString();
        httpRequest.setMethod(method);

        //获得请求的资源的地址
        String url = line.substring(line.indexOf('/') + 1, line.lastIndexOf('/') - 5);
        url = URLDecoder.decode(url, "UTF-8");//反编码 URL 地址
        String resource = "";
        int i = url.indexOf("/?");
        if (i > 0)
            resource = url.substring(0, i);
        else if (i == -1)
            resource = url;
        httpRequest.setResource(resource);

        //解析GET请求的url参数
        if (i > 0 && "GET".equals(method)) {
            String[] strings = url.substring(i + 2).split("&");
            for (int j = 0; j < strings.length; j++) {
                String str = strings[j];
                int pIndex = str.indexOf('=');
                if (pIndex == -1 || pIndex == 0 || pIndex == str.length() - 1)
                    continue;
                String key = str.substring(0, pIndex);
                String value = str.substring(pIndex + 1);
                httpRequest.setParameter(key, value);
            }
        }

        String version = line.substring(line.lastIndexOf(' ') + 1);
        httpRequest.setHttpVersion(version);

        // 读取所有浏览器发送过来的请求参数头部信息
        while ((line = in.readLine()) != null) {
            if (line.equals(""))
                break;  //读到尾部即跳出
            int index = line.indexOf(':');
            String title = line.substring(0, index);
            String value = line.substring(index + 2);
            if ("Host".equals(title)) {
                httpRequest.setHost(value);
            } else if ("Connection".equals(title)) {
                httpRequest.setConnection(value);
            } else if ("Pragma".equals(title)) {
                httpRequest.setPragma(value);
            } else if ("Cache-Control".equals(title)) {
                httpRequest.setCacheControl(value);
            } else if ("User-Agent".equals(title)) {
                httpRequest.setUserAgent(value);
            } else if ("Sec-Fetch-Dest".equals(title)) {
                httpRequest.setSecFetchDest(value);
            } else if ("Sec-Fetch-Site".equals(title)) {
                httpRequest.setSecFetchSite(value);
            } else if ("Sec-Fetch-Mode".equals(title)) {
                httpRequest.setSecFetchMode(value);
            } else if ("Accept".equals(title)) {
                httpRequest.setAccept(value);
            } else if ("Referer".equals(title)) {
                httpRequest.setReferer(value);
            } else if ("Accept-Encoding".equals(title)) {
                httpRequest.setAcceptEncoding(value);
            } else if ("Accept-Language".equals(title)) {
                httpRequest.setAcceptLanguage(value);
            } else if ("Upgrade-Insecure-Requests".equals(title)) {
                httpRequest.setUpgradeInsecureRequests(value);
            } else if ("Sec-Fetch-User".equals(title)) {
                httpRequest.setSecFetchUser(value);
            } else {
                System.out.println(title + "参数未被支持");
            }
        }

        System.out.println("解析请求信息 end: ***************");
        System.out.println("用户请求的资源是: " + resource);
        System.out.println("请求的类型是: " + method);
        return httpRequest;
    }

}
