package Loader;

public class ServletClass {
    private String URLPattern;
    private String ServletName;
    private Class ServletClass;

    public ServletClass() {
    }

    public ServletClass(String URLPattern, String servletName) {
        this.URLPattern = URLPattern;
        ServletName = servletName;
    }

    public String getURLPattern() {
        return URLPattern;
    }

    public void setURLPattern(String URLPattern) {
        this.URLPattern = URLPattern;
    }

    public String getServletName() {
        return ServletName;
    }

    public void setServletName(String servletName) {
        ServletName = servletName;
    }

    public Class getServletClass() {
        return ServletClass;
    }

    public void setServletClass(Class servletClass) {
        ServletClass = servletClass;
    }
}
