package Loader;

import java.util.*;

public class ServletMap {

    private HashMap<String, Servlet> map;

    public ServletMap() {
        this.map = new HashMap<>();
    }

    public void setMap(String url, String servletName) {
        Servlet node = new Servlet();
        node.setURLPattern(url);
        node.setServletName(servletName);
        map.put(url, node);
    }

    public void useServletNameSetServletClass(String servletName, Class servletClass) {
        Set<Map.Entry<String, Servlet>> set = map.entrySet();
        for (Map.Entry<String, Servlet> entry: set) {
            Servlet node = entry.getValue();
            if (node.getServletName().equals(servletName)) {
                node.setServletClass(servletClass);
            }
        }
    }

    public Servlet getServlet(String url) {
        return map.get(url);
    }
}
