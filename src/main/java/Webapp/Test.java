package Webapp;

import Http.HttpRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

public class Test {

    public static void main(String[] args) throws FileUploadException {
        HttpRequest request = (HttpRequest) new HttpRequest();
        ServletFileUpload.isMultipartContent((HttpServletRequest) request);
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(16);
        File file = new File("WebContent/");
        factory.setRepository(file);

        ServletFileUpload upload = new ServletFileUpload();
        upload.setSizeMax(1024 * 1024);

        List items = upload.parseRequest((RequestContext) request);

        Iterator iterator = items.iterator();
        while (iterator.hasNext()) {
            FileItem item = (FileItem) iterator.next();

            if (item.isFormField()) {

            } else {

            }
        }
    }

}
