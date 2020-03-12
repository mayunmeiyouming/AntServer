package Http;

import java.util.HashMap;
import java.util.HashSet;

public class MultipartContent {
    private HashMap<String, String> parameters;
    private HashSet<FileContent> set;

    public MultipartContent() {
        this.parameters = new HashMap<>();
        this.set = new HashSet<>();
    }

    public void setParameter(String key, String value) {
        parameters.put(key, value);
    }

    public void setFile(FileContent content) {
        set.add(content);
    }


}
