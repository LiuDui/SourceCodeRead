# 简单的servlet容器
共有两个应用程序
* 较简单的一个，仅仅用于说明servlet容器如何运行
* 由简单的那个演变而来，复杂一些

## javax.servlet.Servlet接口
Servlet编程需要使用到`javax.servlet`和`javax.servlet.http`两个包下面的接口和类。
这两个包可通过依赖工具添加（附[依赖查询链接](https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api/4.0.1)）

在Servlet接口中声明了5个方法：
```java
public interface Servlet {
    void init(ServletConfig var1) throws ServletException;

    ServletConfig getServletConfig();

    void service(ServletRequest var1, ServletResponse var2) throws ServletException, IOException;

    String getServletInfo();

    void destroy();
}
```
`init()`、`service()`、`destroy()`与servlet生命周期相关。

当实例化某个servlet类后，servlet容器调用其`init()`方法进行初始化，该方法只会调用该方法一次，调用后就可以执行服务方法了`service()`，在servlet接受请求前，必须正确初始化实例（init()方法成功执行），其中编写仅仅需要执行一次地初始化，例如：
* 载入数据库驱动
* 初始化默认值等

该方法可以留空。

当servlet一个客户端请求到达后，servlet容器就会调用相应的servlet的`service()`方法，并将`javax.servlet.servletRequest`对象和`javax.servlet.ServletResponse`对象传入，其中`ServletRequest`对象包含客户端的HTTP请求信息，`ServletResponse`对象则封装servlet的响应信息。
在servlet对象的整个生命周期内，`service()`方法会被多次调用。

servlet实例从服务中移除前，servlet容器会调用servlet实例的`destroy()`方法，一般当servlet容器关闭或servlet容器要释放内存时，才会将servlet实例移除，而且只有当servlet实例的`service()`方法中的所有线程都退出后或执行超时后，才会调用`destory()`方法。

当servlet容器调用`destory()`方法让servlet对象有机会去清理自身持有资源，如内存、文件句柄和线程等，确保所有持久化状态与内存中该servlet对象当前的状态同步。

## 创建一个servlet如下
因为所有的servlet都要实现`javax.servlet.Servlet`接口。
```java

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
```
## Servlet容器1


servlet编程中，浏览器请求的是servlet，当然连接还是通过socket，这时候就会找到对应的servlet，完成初始化工作，并通过socke得到inputStream和outStream，分别构造解析成request和response，传入该servlet的serviece方法中。

> 对一个servlet的每个HTTP请求，一个功能齐全的servlet容器有以下几件事做：
1. 当第一次调用么讴歌servlet时，要载入该servlet类，并调用其`init()`方法（仅此一次）
2. 针对每个request请求，创建一个`javax.servlet.ServletRequest`实例和一个`javax.servlet.ServletResponse`实例
3. 调用该servlet的`service()`方法时，将servletRequest对像和servletResponse对象作为参数传入
4. 当关闭该servlet类时，将调用其`destory()`方法，并卸载该servlet类

下面的代码功能阉割，仅仅实现下面的工能：
> 1. 等待HTTP请求
2. 创建一个servletRequst对象和一个servletResponse对象
3. 若请求的是静态资源，则调用StataicResourceProcessor对象的process()方法，传入servletResquest对象和servletResponse对象
4. 若请求的是servlet，则载入相应servlet类，调用其`service()`方法，传入servletRequest对象和servletResponse对象。

### HttpServer类
整个程序的入口。接受HTTP请求。提供两个功能
* 请求静态资源，浏览器对应的URL为`http://hostname:port/staticResource`
* 请求servlet，浏览器对应的URL为`http://hostname:port/servlet/servletClass`

这里的处理方式是通过将不同类型的请求分发给不同类型对象处理实现的。
```java
// 判断请求的是静态资源还是servlet
                  if(request.getUri().startsWith("/servlet")) {
                      ServletProcessor1 processor = new ServletProcessor1();
                      processor.process(request, response);
                  }else{
                      StaticResourceProcessor processor = new StaticResourceProcessor();
                      processor.process(request, response);
                  }
```
### Request类
servlet的`service()`方法会从servlet 容器中接受一个javax.servlet.ServletRequest实例和一个javax.servlet.ServletResponse实例。

`ex02.pyrmont.Request`对象表示被传递给servlet的`service()`方法的一个Request对象，它必须实现javax.servlet.servletRequest接口中声明的方法。

相比于第一章的Request类，这个类实现了javax.servlet.ServletRequest接口，其他没有太大变化。

### Response
相比于第一章的Request类，这个类实现了javax.servlet.ServletResponse接口，其他没有太大变化。

```java

    // 第二个参数代表autoFlush()，选择true表示对println()方法的任何调用都会刷新输出，但是调用print()方法不会刷新输出
    // 因此，如果在servlet的sevice()方法最后一行调用print()方法，则内容不会被发送给浏览器。
    public PrintWriter getWriter() throws IOException {
        writer = new PrintWriter(outputStream, true);// 这里没有做判断就直接new一个，为什么呢?
        return writer;
    }
```
### StaticResourceProcessor
该类主要用来处理静态资源请求，里面也是调用的Response的方法。
```java
public class StaticResourceProcessor {
    // 这里直接调用了response的方法
    public void process(Request request, Response response) {
        response.sendStaticResource();
    }
}
```

有点像个代理。
### ServletProcessor1
这个是用来处理servlet请求的，也只有一个`process()`方法。
