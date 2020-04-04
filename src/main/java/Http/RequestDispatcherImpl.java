package Http;

public class RequestDispatcherImpl implements RequestDispatcher {

    private String path;

    RequestDispatcherImpl(String path) {
        this.path = path;
    }

    @Override
    public void forward(HttpRequest request, HttpResponse response) {

    }

    @Override
    public void include(HttpRequest request, HttpResponse response) {

    }
}
