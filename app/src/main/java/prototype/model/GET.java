package prototype.model;
import java.util.*;

public class GET {

    private Map<String, String> params = new HashMap<String, String>();
    private List<Response> responses = new ArrayList<Response>();
    
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
    
}
