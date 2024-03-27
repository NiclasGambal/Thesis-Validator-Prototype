package prototype;
    
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App {

    public static void main(String[] args) throws IOException, URISyntaxException {
        
        App app = new App();

        // Specification File
        String fileName = "specification.json";

        // Interface address
        String url = "https://petstore3.swagger.io/api/v3";

        InputStream is = app.getFileFromResourceAsStream(fileName);
        JSONObject specification = parseAsJSON(is);
        
        if(isValid(specification)){
            Validator validator = new Validator();
            validator.validateInterface(specification , url);
        }
    }

    /**
     * Get a file from the resource folder of this project.
     * @param fileName Name of the file.
     * @return File as stream.
     */
    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    /**
     *  Parse stream to as JSON object.
     * @param is Input stream to parse the JSON object from.
     * @return Parsed JSON object.
     */
    private static JSONObject parseAsJSON(InputStream is) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jObject = (JSONObject)parser.parse(
                new InputStreamReader(is, StandardCharsets.UTF_8)
            );
            return jObject;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Checks certain conditions of the specification.
     * @param jObject The specification to check.
     * @return If the checks were successful.
     */
    private static boolean isValid(JSONObject jObject){
        JSONObject interface_info = (JSONObject) jObject.get("interface_info");
        if(interface_info == null || interface_info.isEmpty()){
            System.out.println("The interface identity is missing or empty.");
            return false;
        }
        if(interface_info.get("title") == null || interface_info.get("version") == null){
            System.out.println("The title or version attributes are missing");
            return false;
        }
        if(((String)interface_info.get("title")).isEmpty() || ((String)interface_info.get("version")).isEmpty()){
            System.out.println("The title or version attributes are empty");
            return false;
        }
        JSONObject paths = (JSONObject) jObject.get("paths");
        if(paths == null || paths.isEmpty()){
            System.out.println("The resources section is missing or empty.");
            return false;
        }
        return true;
    }
}
