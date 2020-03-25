package Loader;

import java.util.*;

public class ServletClassMap {

    private HashMap<String, ServletClass> map;

    public ServletClassMap() {
        this.map = new HashMap<>();
    }

    public void setMap(String url, String servletName) {
        ServletClass node = new ServletClass();
        node.setURLPattern(url);
        node.setServletName(servletName);
        map.put(url, node);
    }

    public void useServletNameSetServletClass(String servletName, Class servletClass) {
        Set<Map.Entry<String, ServletClass>> set = map.entrySet();
        for (Map.Entry<String, ServletClass> entry: set) {
            ServletClass node = entry.getValue();
            if (node.getServletName().equals(servletName)) {
                node.setServletClass(servletClass);
            }
        }
    }

    public ServletClass getServlet(String url) {
        return map.get(url);
    }
}
