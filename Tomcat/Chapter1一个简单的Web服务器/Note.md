Web服务器也称超文本传输协议服务器（HyperText Transfer Protocol,HTTP），使用`HTTP`协议进行服务器与客户端（通常是Web浏览器）的通信。

通信依然是用`Socket`与`ServerSocket`。

## HTTP
* HTTP是基于“请求-响应”的协议，客户端发起请求，服务器进行响应
* HTTP使用可靠的TCP连接，默认使用TCP 80端口
* HTTP发展过程：HTTP/0.9 、HTTP/1.0 、HTTP/1.1

在HTTP中，总是**客户端通过建立连接并发送HTTP请求来初始化一个事务，WEB服务器并不负责联系客户端或者建立一个到客户端的回调连接**，也就是服务器仅仅是接受来自客户端的请求，并根据请求，将数据返回给客户端。

### HTTP请求
> HTTP请求包含三部分，以ASCII的形式传输
* 请求方法 统一资源标识符（URI） 协议/版本：出现在第一行。URI用于指定资源的完整路径。
* 请求头：包含客户端环境和请求实体正文的相关信息，例如浏览器使用的语言，各个请求头条目使用
回车换行间隔开。
* 实体：正文

```
POST /hello.txt HTTP/1.1
User-Agent: curl/7.16.3 libcurl/7.16.3 OpenSSL/0.9.7l zlib/1.2.3
Host: www.example.com
Accept-Language: en, mi

lastName=TheName&age=13
```
请求头个实体之间有一个空行，属于强制格式要求。

### HTTP响应
> HTTP响应包含三部分
* 状态行：协议 状态码 描述
* 消息报头（或响应头）
* 响应实体段

![http响应](./imgs/httpmessage.jpg)
在消息报头和响应实体段之间也有一个空行。

## Socket和ServerSocket

## 应用程序
本章Web服务器代码在ex01.pyrmont包下，包含三个类
* HttpServer
* Request
* Response

### HttpServer
HttpServer类中包含`main`方法，因为http是请求-响应模式，所以服务器所做的就是等待请求，然后回复响应数据，因此在HttpServer类有一个`await()`方法，作用是
* 在指定的端口上等待HTTP请求
* 处理请求
* 返回响应信息给客户端

web通信涉及到文件资源的请求，所以在HttpServer中定义一个静态不可变类型的WEB_ROOT变量，用于指定存放静态资源文件的位置。
```java
// Web服务器静态资源存放的位置
public static final String WEB_ROOT =
        System.getProperty("user.dir") + File.separator + "webroot";
```
创建`ServerSocket`对象，并在循环中监听端口的Socket连接。
```java
private void await() {
       ServerSocket serverSocket = null;
       try {
           serverSocket = new ServerSocket(NetConfig.PORT,
                   NetConfig.DEFAULT_BACKLOG_COUNT,
                   InetAddress.getByName(NetConfig.LOACAL_HOST));
           // 循环，等待监听
           while (!shutdown){
               Socket socket = null;

               try {
                   socket = serverSocket.accept();
                   // 对于每个连接解析数据
                   ...
               }catch (Exception e){
                   e.printStackTrace();
                   continue;
               }
           }

       } catch (IOException e) {
           e.printStackTrace();
       } finally {
         // 清理资源
       }
```
在循环中，对于每个新建立的连接，从连接的数据中，解析出`Request`对象，并生成`Response`对象
```java
Socket socket = null;
InputStream inputStream = null;
OutputStream outputStream = null;

try {
  socket = serverSocket.accept();
  inputStream = socket.getInputStream();
  outputStream = socket.getOutputStream();

  // 通过inputStream接收的数据，生成Request对象，并解析
  Request request =  new Request(inputStream);
  request.parse();

  // 创建Response对象
  Response response = new Response(outputStream);
  response.setRequest(request);
  response.sendStaticResource();

  shutdown = request.getUril().equals(SHUTDOWN_COMMAND);
  }catch (Exception e){
  e.printStackTrace();
  continue;
  } finally {
  if(socket != null){
  // 关闭套接字
  socket.close();
  }
}
```

### Request
一个Http请求对应一个Request对象，可以通过传入一个Socket.InputStream对象。

### Response
