package ex02.pyrmont;


import javafx.scene.shape.Path;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class ServletProcessor1 {
    public void process(Request request, Response response) {
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;

        URL[] urls = new URL[1]; // create a URLClassLoader
        URLStreamHandler streamHandler = null;
        File classPath = new File(Constants.WEB_ROOT);

        try {
            String repository = (new URL("file", null, classPath.getCanonicalPath()
            + File.separator)).toString();
            System.out.println("The repository is : " + repository);
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Class myclass = null;
        System.out.println("servletName : " + servletName);
        String packageName = "ex02.pyrmont.";
        String theFullPath = packageName + servletName;
        try {
            myclass = loader.loadClass(theFullPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Servlet servlet = null;
        try {
            servlet = (Servlet) myclass.newInstance();
            servlet.service(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
