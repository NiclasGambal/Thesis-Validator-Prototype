package prototype.model;

import java.util.*;;

/**
 * Used to represent the requests that are defined for a specific path.
 */
public class RequestSet {
    private String path;
    private POST post;
    private PUT put;
    private GET get;
    private DELETE delete;
    private Set<String> contained_methods = new HashSet<String>();
    
    public Set<String> getContained_methods() {
        return contained_methods;
    }

    public RequestSet(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public POST getPost() {
        return post;
    }

    public void setPost(POST post) {
        this.post = post;
        contained_methods.add("post");
    }

    public PUT getPut() {
        return put;
    }

    public void setPut(PUT put) {
        this.put = put;
        contained_methods.add("put");
    }

    public GET getGet() {
        return get;
    }

    public void setGet(GET get) {
        this.get = get;
        contained_methods.add("get");
    }

    public DELETE getDelete() {
        return delete;
    }

    public void setDelete(DELETE delete) {
        this.delete = delete;
        contained_methods.add("delete");
    }

}
