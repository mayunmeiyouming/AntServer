package Loader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AntServerLoader {

    private ServletMap map;

    private String webXmlPath = "WebContent/conf/web.xml";  //web.xml相对地址
    private String servletDefautlPath = "Webapp.main.java.";//servlet默认路径

    public AntServerLoader() throws JDOMException, IOException, ClassNotFoundException {
        map = new ServletMap();
        parser();
    }

    public ServletMap getMap() {
        return map;
    }

    private void parser() throws JDOMException, IOException, ClassNotFoundException {

        // 通过SAXBuilder解析xml
        SAXBuilder builder = new SAXBuilder();

        File file = new File(webXmlPath);
        Document doc = builder.build(file);
        Element root = doc.getRootElement(); // 获取根元素

        List<Element> list = root.getChildren("servlet-mapping");
        for (Element element: list) {
            Element e = element.getChild("servlet-name");
            String servletName = e.getText();
            e = element.getChild("url-pattern");
            String url = e.getText();
            map.setMap(url, servletName);
        }

        list = root.getChildren("servlet");
        for (Element element: list) {
            Element e = element.getChild("servlet-name");
            String servletName = e.getText();
            e = element.getChild("servlet-class");
            String text = servletDefautlPath + e.getText();
            Class cl = Class.forName(text);
            map.useServletNameSetServletClass(servletName, cl);
        }
    }

}
