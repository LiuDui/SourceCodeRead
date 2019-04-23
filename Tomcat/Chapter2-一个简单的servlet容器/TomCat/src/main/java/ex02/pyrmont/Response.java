package ex02.pyrmont;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

public class Response implements ServletResponse {
    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream outputStream;
    PrintWriter writer;


    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void sendStaticResource() {
        System.out.println("uri:" + request.getUri());
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fileInputStream = null;

        try {
            File file = new File(Constants.WEB_ROOT, request.getUri());
            if(file != null && file.exists()){
                fileInputStream = new FileInputStream(file);
                int ch = fileInputStream.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1){
                    outputStream.write(bytes, 0, ch);
                    ch = fileInputStream.read(bytes, 0, BUFFER_SIZE);
                }
            }else {// 文件不存在
                String errormessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type:text/html\r\n" +
                        "Content-Length:23\r\n"+
                        "\r\n"+
                        "<h1>File Note Found</h1>";
                outputStream.write(errormessage.getBytes());
            }
        } catch (IOException e) {
            System.out.println("exception:" + e.toString());
        } finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    public String getCharacterEncoding() {
        return null;
    }

    public String getContentType() {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    // 第二个参数代表autoFlush()，选择true表示对println()方法的任何调用都会刷新输出，但是调用print()方法不会刷新输出
    // 因此，如果在servlet的sevice()方法最后一行调用print()方法，则内容不会被发送给浏览器。
    public PrintWriter getWriter() throws IOException {
        writer = new PrintWriter(outputStream, true);// 这里没有做判断就直接new一个，为什么呢?每个
        return writer;
    }

    public void setCharacterEncoding(String s) {

    }

    public void setContentLength(int i) {

    }

    public void setContentLengthLong(long l) {

    }

    public void setContentType(String s) {

    }

    public void setBufferSize(int i) {

    }

    public int getBufferSize() {
        return 0;
    }

    public void flushBuffer() throws IOException {

    }

    public void resetBuffer() {

    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {

    }

    public void setLocale(Locale locale) {

    }

    public Locale getLocale() {
        return null;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
