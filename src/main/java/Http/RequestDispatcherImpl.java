package Http;

import java.io.IOException;

public class RequestDispatcherImpl implements RequestDispatcher {

    private String path;

    RequestDispatcherImpl(String path) {
        this.path = path;
    }

    @Override
    public void forward(HttpRequest request, HttpResponse response) throws IOException {
        String u = "WebContent/" + path;
        response.write(u);
    }

    @Override
    public void include(HttpRequest request, HttpResponse response) {

    }
}
