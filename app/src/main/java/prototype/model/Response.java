package prototype.model;

import java.util.*;

public class Response {
    
    private String statusCode;
    private String contentType;
    private Map<String, Object> body;

    public String getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(String response) {
        this.statusCode = response;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public Map<String, Object> getBody() {
        return body;
    }
    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
}
