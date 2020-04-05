package Http;

import java.util.*;

public class HttpResponsePackage {

    private HashMap<String, String> map = new HashMap<>();
    public int statusCode;
    private static String NEWLINE = "\r\n";
    private String head = "HTTP/1.1 200 OK" + NEWLINE;

    private List<Cookie> cookies;

    public HttpResponsePackage() {
        map.put("Cache-Control", "no-cache, no-store, must-revalidate");
        map.put("Pragma", "no-cache");
        map.put("Expires", "0");
        map.put("Date", new Date().toString());
        map.put("Connection", "Keep-Alive");
        map.put("Server", "Ant Server 0.1");
    }

    public Cookie[] getCookies() {
        if (cookies == null || cookies.isEmpty())
            return null;
        Cookie[] c = new Cookie[0];
        return cookies.toArray(c);
    }

    public void setCookies(Cookie cookie) {
        if (cookie == null)
            return;
        if (cookies == null)
            cookies = new ArrayList<>();
        String name = cookie.getName();
        String value = cookie.getValue();
        if (!"".equals(name) && name != null && !"".equals(value) && value != null)
            cookies.add(cookie);
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setContentLength(long contentLength) {
        map.put("Content-Length", contentLength + "");
    }

    public void setLastModified(long lastModified) {
        map.put("Last-Modified", new Date(lastModified).toString());
    }

    public void setContentType(String contentType) {
        map.put("Content-Type", contentType + "; charset=utf-8");
    }

    public void setCacheControl(String cacheControl) {
        map.put("Cache-Control", cacheControl);
    }

    public void setPragma(String pragma) {
        map.put("Pragma", pragma);
    }

    public void setExpires(String expires) {
        map.put("Expires", expires);
    }

    public void setDate(String date) {
        map.put("Date", new Date().toString());
    }

    public void setConnection(String connection) {
        map.put("Connection", connection);
    }

    public void setServer(String server) {
        map.put("Server", server);
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
        if (statusCode == 200)
            setHead("HTTP/1.1 200 OK" + NEWLINE);
        else if (statusCode == 302) {
            setHead("HTTP/1.1 302" + NEWLINE);
        } else if (statusCode == 404) {
            setHead("HTTP/1.1 404 Not Found" + NEWLINE);
        }
    }

    public void setLocation(String location) {
        map.put("Location", location);
    }

    public Set<Map.Entry<String, String>> getEntries() {
        return map.entrySet();
    }
}
