package Http;

import java.util.*;

public class HttpResponsePackage {

    private HashMap<String,String> map = new HashMap<>();
    public int statusCode;

    public HttpResponsePackage() {
        map.put("Cache-Control", "no-cache, no-store, must-revalidate");
        map.put("Pragma", "no-cache");
        map.put("Expires", "0");
        map.put("Date", new Date().toString());
        map.put("Connection", "Keep-Alive");
        map.put("Server", "Ant Server 0.1");
    }

    public String getHead() {
        return map.get("head");
    }

    public void setHead(String head) {
        map.put("head", head);
    }

    public void setContentLength(long contentLength) {
        map.put("Content-Length",contentLength + "");
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

    public Set<Map.Entry<String, String>> getEntries() {
        return map.entrySet();
    }
}
