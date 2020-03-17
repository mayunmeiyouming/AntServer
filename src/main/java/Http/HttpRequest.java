package Http;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

public class HttpRequest {
    private String resource;
    private HashMap<String, String> map;
    private String protocol;
    private String method;
    private String Host;
    private String Connection;
    private String Pragma;
    private String CacheControl;
    private String UserAgent;
    private String SecFetchDest;
    private String SecFetchSite;
    private String SecFetchMode;
    private String SecFetchUser;
    private String Accept;
    private String AcceptEncoding;
    private String AcceptLanguage;
    private String Referer;
    private String UpgradeInsecureRequests;
    private String ContentLength;
    private String Origin;
    private String ContentType;

    private MultipartContent multipartContent;

    private InputStream inputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public final boolean isMultipartContent() {
        if (!"POST".equalsIgnoreCase(this.getMethod())) {
            return false;
        }
        String contentType = this.getContentType();
        if (contentType == null) {
            return false;
        }
        return contentType.toLowerCase(Locale.ENGLISH).startsWith("multipart/");
    }

    public MultipartContent getMultipartContent() {
        return multipartContent;
    }

    public void setMultipartContent(MultipartContent multipartContent) {
        this.multipartContent = multipartContent;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getContentLength() {
        return ContentLength;
    }

    public void setContentLength(String contentLength) {
        ContentLength = contentLength;
    }

    public HttpRequest() {
        map = new HashMap<>();
    }

    public String getParameter(String key) {
        return map.get(key);
    }

    public void setParameter(String key, String value) {
        map.put(key, value);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getConnection() {
        return Connection;
    }

    public void setConnection(String connection) {
        Connection = connection;
    }

    public String getPragma() {
        return Pragma;
    }

    public void setPragma(String pragma) {
        Pragma = pragma;
    }

    public String getCacheControl() {
        return CacheControl;
    }

    public void setCacheControl(String cacheControl) {
        CacheControl = cacheControl;
    }

    public String getUserAgent() {
        return UserAgent;
    }

    public void setUserAgent(String userAgent) {
        UserAgent = userAgent;
    }

    public String getSecFetchDest() {
        return SecFetchDest;
    }

    public void setSecFetchDest(String secFetchDest) {
        SecFetchDest = secFetchDest;
    }

    public String getAccept() {
        return Accept;
    }

    public void setAccept(String accept) {
        Accept = accept;
    }

    public String getSecFetchSite() {
        return SecFetchSite;
    }

    public void setSecFetchSite(String secFetchSite) {
        SecFetchSite = secFetchSite;
    }

    public String getSecFetchMode() {
        return SecFetchMode;
    }

    public void setSecFetchMode(String secFetchMode) {
        SecFetchMode = secFetchMode;
    }

    public String getReferer() {
        return Referer;
    }

    public void setReferer(String referer) {
        Referer = referer;
    }

    public String getAcceptEncoding() {
        return AcceptEncoding;
    }

    public void setAcceptEncoding(String acceptEncoding) {
        AcceptEncoding = acceptEncoding;
    }

    public String getAcceptLanguage() {
        return AcceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        AcceptLanguage = acceptLanguage;
    }

    public String getSecFetchUser() {
        return SecFetchUser;
    }

    public void setSecFetchUser(String secFetchUser) {
        SecFetchUser = secFetchUser;
    }

    public String getUpgradeInsecureRequests() {
        return UpgradeInsecureRequests;
    }

    public void setUpgradeInsecureRequests(String upgradeInsecureRequests) {
        UpgradeInsecureRequests = upgradeInsecureRequests;
    }
}
