package Core;

import Http.HttpRequest;
import Http.HttpResponse;
import Loader.HttpServlet;
import Loader.ServletClass;
import Loader.ServletClassMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpDispatcher {

    private ServletClassMap map;
    private HashMap<String, HttpServlet> classMap;

    public HttpDispatcher(ServletClassMap map) {
        this.map = map;
        classMap = new HashMap<>();
    }

    public boolean dispatch(String url, HttpRequest request, HttpResponse response) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        ServletClass servlet = map.getServlet(url);
        if (servlet != null) {
            Class cl = servlet.getServletClass();
            if (cl == null)
                return false;

            HttpServlet o = classMap.get(url);
            if (o == null) {   //判断classMap是否有实例对象
                //System.out.println(Thread.currentThread().getName() + "创建对象");
                o = (HttpServlet) cl.newInstance();
                classMap.put(url, o);
            }
            Method m = getDeclaredMethod(cl, "init", null);
            invoke(o, m, null);

            List<Class> list = new ArrayList<>();
            list.add(HttpRequest.class);
            list.add(HttpResponse.class);
            Method method = getDeclaredMethod(cl, "service", list);

            Class<?>[] cla = method.getParameterTypes();
            List<Object> listValue = new ArrayList<Object>();
            //循环获取函数参数
            for (int i = 0; i < cla.length; i++) {
                if (cla[i].getTypeName().equals("Http.HttpRequest"))
                    listValue.add(request);
                else if (cla[i].getTypeName().equals("Http.HttpResponse"))
                    listValue.add(response);
            }

            method.invoke(o, listValue.toArray());
            return true;
        }
        return false;
    }

    /*
     * 获取Class对象中的方法
     */
    private Method getDeclaredMethod(Class cl, String name, List<Class> list) throws NoSuchMethodException {
        Method method = null;
        Class[] a = {};
        try {
            if (list != null)
                method = cl.getDeclaredMethod(name, list.toArray(a));
            else
                method = cl.getDeclaredMethod(name);

        } catch (NoSuchMethodException e) { //子类未实现该方法，将调用父类的该方法
            Class superclass = cl.getSuperclass();
            if (list != null)
                method = superclass.getDeclaredMethod(name, list.toArray(a));
            else
                method = superclass.getDeclaredMethod(name);
        } finally {
            if (method != null && !method.isAccessible()) {
                method.setAccessible(true);
            }
            return method;
        }
    }

    /*
     * 利用反射调用Class对象中的方法
     */
    private void invoke(Object o, Method method, List<Object> listValue)
            throws InvocationTargetException, IllegalAccessException {
        if (method != null && !method.isAccessible()) {
            method.setAccessible(true);
        }
        if (listValue != null)
            method.invoke(o, listValue.toArray());
        else
            method.invoke(o);
    }

}
