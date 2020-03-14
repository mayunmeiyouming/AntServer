package Http;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public void setFile(FileContent content) {
        set.add(content);
    }

    public FileContent getFile(String name){
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            FileContent fileContent = (FileContent) iterator.next();
            if (fileContent.getName().equals(name))
                return fileContent;
        }
        return null;
    }


}
