package ex02.pyrmont;

import Configs.NetConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer1 {

    // Web服务器静态资源存放的位置
    // 关闭指令
    public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    // 标识：是否接到 SHUTDOWN_COMMAND 指令
    private boolean shutdown = false;

    // 主函数，也是服务端程序的入口
    public static void main(String[] args) {
        HttpServer1 server = new HttpServer1();
        server.await();
    }

    // 在该方法中，等待监听端口的信息
    private void await() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(NetConfig.PORT,
                    NetConfig.DEFAULT_BACKLOG_COUNT,
                    InetAddress.getByName(NetConfig.LOACAL_HOST));
            // 等待监听
            while (!shutdown){
                Socket socket = null;
                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    System.out.println("开始侦听...");
                    socket = serverSocket.accept();
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();

                    // 通过inputStream接收的数据，生成Request对象，并解析
                    Request request =  new ex02.pyrmont.Request(inputStream);
                    request.parse();

                    // 创建Response对象
                    Response response = new Response(outputStream);
                    response.setRequest(request);

                    // 判断请求的是静态资源还是servlet
                    if(request.getUri().startsWith("/servlet")) {
                        ServletProcessor1 processor = new ServletProcessor1();
                        processor.process(request, response);
                    }else{
                        StaticResourceProcessor processor = new StaticResourceProcessor();
                        processor.process(request, response);
                    }

                    socket.close(); // http请求就这样，每次请求都要建立一次，关闭一次

                    shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                } finally {
                    if(socket != null){
                        // 关闭套接字
                        socket.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
