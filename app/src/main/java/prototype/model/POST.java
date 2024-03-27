package prototype.model;

import java.util.*;

public class POST {
    
    private Map<String, Object> body = new HashMap<String, Object>();
    private List<Response> responses = new ArrayList<Response>();

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
}
