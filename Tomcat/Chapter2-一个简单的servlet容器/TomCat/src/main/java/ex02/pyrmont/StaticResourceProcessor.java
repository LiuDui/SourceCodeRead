package ex02.pyrmont;

public class StaticResourceProcessor {
    // 这里直接调用了response的方法
    public void process(Request request, Response response) {
        response.sendStaticResource();
    }
}
