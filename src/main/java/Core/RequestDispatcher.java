package Core;

import Http.HttpRequest;
import Http.HttpResponse;
import Loader.Servlet;
import Loader.ServletMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RequestDispatcher {

    private ServletMap map;

    public RequestDispatcher(ServletMap map) {
        this.map = map;
    }

    public boolean dispatch(String url, HttpRequest request, HttpResponse response) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        Servlet servlet = map.getServlet(url);
        if (servlet != null) {
            Class cl = servlet.getServletClass();
            if (cl == null)
                return false;

            Method method = cl.getDeclaredMethod("doGet", HttpRequest.class, HttpResponse.class);
            if(!method.isAccessible()){
                method.setAccessible(true);
            }
            Class<?>[] cla = method.getParameterTypes();
            List<Object> listValue = new ArrayList<Object>();
            //循环参数类型
            for (int i = 0; i < cla.length; i++) {
                if (cla[i].getTypeName().equals("Http.HttpRequest"))
                    listValue.add(request);
                else if (cla[i].getTypeName().equals("Http.HttpResponse"))
                    listValue.add(response);
            }
            method.invoke(cl.newInstance(), listValue.toArray());
            return true;
        }
        return false;
    }

}
