package ex02.pyrmont;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class ServletResponseFace implements ServletResponse {
    private Response response = null;

    public ServletResponseFace(Response response) {
        this.response = response;
    }

    public String getCharacterEncoding() {
        return this.response.getCharacterEncoding();
    }

    public String getContentType() {
        return this.response.getContentType();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return this.response.getOutputStream();
    }

    public PrintWriter getWriter() throws IOException {
        return this.response.getWriter();
    }

    public void setCharacterEncoding(String s) {
        this.response.setCharacterEncoding(s);
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
}
