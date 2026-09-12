package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Reads the data-driven employee payload from testdata.json.
 */
public class JsonDataReader {

    private static JsonNode rootNode;
    private static final String DATA_PATH = "src/test/resources/testdata.json";

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            rootNode = mapper.readTree(new File(DATA_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read test data from " + DATA_PATH, e);
        }
    }

    public static String get(String key) {
        JsonNode employeeNode = rootNode.get("employee");
        if (employeeNode == null || employeeNode.get(key) == null) {
            throw new RuntimeException("Key '" + key + "' not found under 'employee' in testdata.json");
        }
        return employeeNode.get(key).asText();
    }
}
