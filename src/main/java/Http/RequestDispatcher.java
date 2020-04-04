package Http;

import java.io.IOException;

public interface RequestDispatcher {

    public void forward(HttpRequest request, HttpResponse response) throws IOException;

    public void include(HttpRequest request, HttpResponse response);

}
