package ex01.pyrmont;

import javax.servlet.Servlet;
import java.io.IOException;
import java.io.InputStream;

public class Request{
    private InputStream inputStream;
    private String uri;

    public Request(InputStream inputStream){
        this.inputStream = inputStream;
    }

    /*
    解析HTTP请求中的原始数据，调用私有的parseUri来执行实际的工作
     */
    public void parse(){
        StringBuffer request = new StringBuffer(2048);
        int i = 0;
        byte[] buffer = new byte[2048];
        try {
            i = inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }

        System.out.println("-----------------------");
        System.out.print(request.toString()); // 这里没有使用自动回车换行的println()
        System.out.println("-----------------------");
        uri = parseUri(request.toString());
    }

    // 请求头的格式是： method 空格 uri 空格 协议版本，所以可以通过空格定位uri
    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(" ");
        if(index1 != -1){
            index2 = requestString.indexOf(" ", index1 + 1);
            if(index2 > index1){
                return requestString.substring(index1 + 1, index2);
            }
        }
        return null;
    }

    public String getUri(){
        return uri;
    }
}
