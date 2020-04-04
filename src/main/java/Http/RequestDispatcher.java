package Http;

public interface RequestDispatcher {

    public void forward(HttpRequest request, HttpResponse response);

    public void include(HttpRequest request, HttpResponse response);

}
