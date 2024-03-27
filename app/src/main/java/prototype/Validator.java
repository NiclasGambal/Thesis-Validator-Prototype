package prototype;

import org.json.simple.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import prototype.model.DELETE;
import prototype.model.GET;
import prototype.model.POST;
import prototype.model.PUT;
import prototype.model.RequestSet;
import prototype.model.Response;
import java.io.*;

import java.util.*;

public class Validator {
    private List<RequestSet> testSet = new ArrayList<RequestSet>();

    /**
     * In this method the generation of the test set and the validation are done.
     * @param specification The JSON specification of the IDL discussed in the thesis.
     * @param interface_address The address of the interface that needs to be validated.
     */
    public void validateInterface(JSONObject specification, String interface_address){
        JSONObject interface_info = (JSONObject) specification.get("interface_info");
        
        String title = (String) interface_info.get("title");
        String version = (String) interface_info.get("version");

        String base_url = interface_address;
        
        JSONObject paths = (JSONObject) specification.get("paths");
        GenerateTestSet(paths, base_url);

        for (RequestSet requestSet : testSet){
            System.out.println(requestSet.getPath());
            HttpClient client = new HttpClient();
            
            // The existent methods are tested on the endpoint.
            if(requestSet.getContained_methods().contains("get")){
                GetMethod method = new GetMethod(requestSet.getPath());

                GET get = requestSet.getGet();
                
                // Provide of custom retry handler is necessary.
                method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
                    new DefaultHttpMethodRetryHandler(3, false));
                
                // All responses identified in the specification are analyzed.
                for (Response response : get.getResponses()){
                    try {
                        int statusCode = client.executeMethod(method);

                        // Here we could compare with the response code from the specification.
                        // In our test implementation we just can see that we get an 404 not found for the 
                        // not exitent endpoint and a 405 for the existing endpoint. 
                        if (statusCode != HttpStatus.SC_OK) {
                            System.err.println("Method failed: " + method.getStatusLine());
                        }
                      
                        // Read the response body.
                        byte[] responseBody = method.getResponseBody();

                        // Response body then would be parsed to an object and compared with
                        // the response body related to the reponse object of the GET object.
                        // Here also the content-type of the response could be compared and validated.
                      
                    } 
                    catch (HttpException e) {
                        System.err.println("Fatal protocol violation: " + e.getMessage());
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        System.err.println("Fatal transport error: " + e.getMessage());
                        e.printStackTrace();
                    } 
                    finally {
                        // Release the connection.
                        method.releaseConnection();
                    }  
                }
            }
        }
    }

    /**
     * Recursive method to identify the absolute paths and their methods.
     * @param jsonObject The path section of the specification JSON object.
     * @param absolutePath The absolute path of the resouces.
     */
    private void GenerateTestSet(JSONObject jsonObject, String absolutePath){
        @SuppressWarnings("unchecked")
        Set<String> paths_keys = jsonObject.keySet();
        for (String path : paths_keys) {
            String new_absolutePath = absolutePath + "/" + path;
            JSONObject jsonObjectToCheck = (JSONObject) jsonObject.get(path);
            @SuppressWarnings("unchecked")
            Set<String> toCheckSet = jsonObjectToCheck.keySet();
            if(toCheckSet.contains("PUT") || toCheckSet.contains("POST") || toCheckSet.contains("GET") || toCheckSet.contains("DELETE")){
                testSet.add(getRequests((JSONObject) jsonObject.get(path), new_absolutePath, toCheckSet));
            } else {
                GenerateTestSet((JSONObject) jsonObject.get(path), new_absolutePath);
            }
        }
    }

    /**
     * The requests defined on the current path are parsed and grouped together with the respecitve path in an RequestSet object.
     * 
     * @param requests The whole JSON object with all further information about the requests.
     * @param path The current path of the resources.
     * @param methods The HTTP methods that are defined on this path.
     */
    private RequestSet getRequests(JSONObject requests, String path, Set<String> methods){
        RequestSet requestSet = new RequestSet(path);
        // GET Case
        if(methods.contains("GET")){
            JSONObject getJsonObject = (JSONObject) requests.get("GET");
            GET get  = new GET();
            JSONObject responsesJsonObject = (JSONObject) getJsonObject.get("responses");
            @SuppressWarnings("unchecked")
            Set<String> responses = responsesJsonObject.keySet(); 
            List<Response> responseList = new ArrayList<Response>();
            // Responses are parsed.
            for (String response : responses){
                JSONObject responseJsonObject = (JSONObject) responsesJsonObject.get(response);
                String content_type = (String) responseJsonObject.get("content-type");
                @SuppressWarnings("unchecked")
                Map<String, Object> body = (Map<String, Object>) responseJsonObject.get("body");
                Response parsedResponse = new Response();
                parsedResponse.setContentType(content_type);
                parsedResponse.setBody(body);
                parsedResponse.setStatusCode(response);
                responseList.add(parsedResponse);
            }
            get.setResponses(responseList);
            @SuppressWarnings("unchecked")
            Map<String, String> params = (Map<String, String>) getJsonObject.get("params");
            if(params == null || params.isEmpty()){
                requestSet.setGet(get);
            } else {
                get.setParams(params);
                requestSet.setGet(get);
            }
        }
        // PUT Case
        if(methods.contains("PUT")){
            JSONObject getJsonObject = (JSONObject) requests.get("PUT");
            PUT put  = new PUT();
            JSONObject responsesJsonObject = (JSONObject) getJsonObject.get("responses");
            @SuppressWarnings("unchecked")
            Set<String> responses = responsesJsonObject.keySet(); 
            List<Response> responseList = new ArrayList<Response>();
            // Responses are parsed.
            for (String response : responses){
                JSONObject responseJsonObject = (JSONObject) responsesJsonObject.get(response);
                String content_type = (String) responseJsonObject.get("content-type");
                @SuppressWarnings("unchecked")
                Map<String, Object> body = (Map<String, Object>) responseJsonObject.get("body");
                Response parsedResponse = new Response();
                parsedResponse.setContentType(content_type);
                parsedResponse.setBody(body);
                parsedResponse.setStatusCode(response);
                responseList.add(parsedResponse);
            }
            put.setResponses(responseList);
            @SuppressWarnings("unchecked")
            Map<String, Object> body = (Map<String, Object>) getJsonObject.get("body");
            put.setBody(body);
            @SuppressWarnings("unchecked")
            Map<String, String> params = (Map<String, String>) getJsonObject.get("params");
            if(params == null || params.isEmpty()){
                requestSet.setPut(put);
            } else {
                put.setParams(params);
                requestSet.setPut(put);
            }
        }
        // POST Case
        if(methods.contains("POST")){
            JSONObject getJsonObject = (JSONObject) requests.get("POST");
            POST post  = new POST();
            JSONObject responsesJsonObject = (JSONObject) getJsonObject.get("responses");
            @SuppressWarnings("unchecked")
            Set<String> responses = responsesJsonObject.keySet(); 
            List<Response> responseList = new ArrayList<Response>();
            // Responses are parsed.
            for (String response : responses){
                JSONObject responseJsonObject = (JSONObject) responsesJsonObject.get(response);
                String content_type = (String) responseJsonObject.get("content-type");
                @SuppressWarnings("unchecked")
                Map<String, Object> body = (Map<String, Object>) responseJsonObject.get("body");
                Response parsedResponse = new Response();
                parsedResponse.setContentType(content_type);
                parsedResponse.setBody(body);
                parsedResponse.setStatusCode(response);
                responseList.add(parsedResponse);
            }
            post.setResponses(responseList);
            @SuppressWarnings("unchecked")
            Map<String, Object> body = (Map<String, Object>) getJsonObject.get("body");
            post.setBody(body);
            requestSet.setPost(post);
        }
        // DELETE Case
        if(methods.contains("DELETE")){
            JSONObject getJsonObject = (JSONObject) requests.get("DELETE");
            DELETE delete  = new DELETE();
            JSONObject responsesJsonObject = (JSONObject) getJsonObject.get("responses");
            @SuppressWarnings("unchecked")
            Set<String> responses = responsesJsonObject.keySet(); 
            List<Response> responseList = new ArrayList<Response>();
            // Responses are parsed.
            for (String response : responses){
                JSONObject responseJsonObject = (JSONObject) responsesJsonObject.get(response);
                String content_type = (String) responseJsonObject.get("content-type");
                @SuppressWarnings("unchecked")
                Map<String, Object> body = (Map<String, Object>) responseJsonObject.get("body");
                Response parsedResponse = new Response();
                parsedResponse.setContentType(content_type);
                parsedResponse.setBody(body);
                parsedResponse.setStatusCode(response);
                responseList.add(parsedResponse);
            }
            delete.setResponses(responseList);
            @SuppressWarnings("unchecked")
            Map<String, String> params = (Map<String, String>) getJsonObject.get("params");
            if(params == null || params.isEmpty()){
                requestSet.setDelete(delete);
            } else {
                delete.setParams(params);
                requestSet.setDelete(delete);
            }
        }
        return requestSet;
    }

}
