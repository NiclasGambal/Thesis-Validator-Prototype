package prototype.model;

import java.util.*;

public class PUT {
    
    private Map<String, Object> body = new HashMap<String, Object>();
    private List<Response> responses = new ArrayList<Response>();
    private Map<String, String> params = new HashMap<String, String>();

    public Map<String, String> getParams() {
        return params;
    }
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

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
