package ex01.pyrmont;

import Configs.NetConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class HttpServer{


    // Web服务器静态资源存放的位置
    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator + "webroot";

    // 关闭指令
    public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    // 标识：是否接到 SHUTDOWN_COMMAND 指令
    private boolean shutdown = false;

    // 主函数，也是服务端程序的入口
    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();

    }

    // 在该方法中，等待监听端口的信息
    private void await() {
        Proxy.newProxyInstance(null, null, null);

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
