package ex02.pyrmont;


import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class PrimitiveeServlet implements Servlet {

    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init()");
    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("service()");
        PrintWriter out = servletResponse.getWriter();
        out.println("hello, this is message from PrimitveServlet");
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {
        System.out.println("destory()");
    }
}
