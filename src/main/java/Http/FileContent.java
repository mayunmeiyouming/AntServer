package Http;

import java.io.InputStream;

public class FileContent {

    private String name;
    private String filename;
    private String contentType;
    private InputStream inputStream;

    @Override
    public String toString() {
        return "name: " + name + "\n"
                + "filename: " + filename + "\n"
                + "contentType: " + contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
