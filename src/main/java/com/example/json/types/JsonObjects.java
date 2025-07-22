package com.example.json.types;

import com.example.json.Json;
import com.example.json.JsonReader;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonObjects implements Json<Map<String, Json<?>>> {

    private final Map<String, Json<?>> objectMap;

    public JsonObjects(JsonReader reader) throws IOException {
        reader.skipWhitespace();
        reader.read();
//        if (firstChar != '{') {
//            throw new IllegalArgumentException("Expected '{' to start JSON object");
//        }
        this.objectMap = parseObject(reader);
    }

    private Map<String, Json<?>> parseObject(JsonReader reader) throws IOException {
        Map<String, Json<?>> map = new LinkedHashMap<>();


        reader.skipWhitespace();
        int ch1 = reader.read();

        if (ch1 == '}') {
            reader.read();
            return map; // empty object
        }
        //reader.unread(ch1); // push back for first element
        // map.put(Json.read(reader));
        while(true){
            reader.skipWhitespace();
            JsonString jsonString = new JsonString(reader);
            reader.skipWhitespace();
            int actual = reader.read();
            if(actual != ':'){
                throw new IllegalStateException("Expected  ':' but found: " + (char) actual);
            }
            reader.skipWhitespace();
            Json<?>value = Json.read(reader);
            map.put(jsonString.getValue(),value);



//
            reader.skipWhitespace();
            int next = reader.read();
            if (next == ',') {
                continue;
            } else if (next == '}') {
                break;
            }
        }

        return map;
    }

    @Override
    public Map<String, Json<?>> getValue() {
        return objectMap;
    }
    @Override
    public String toString() {
        return objectMap.entrySet().stream()
                .map(e -> {
                    // Ensure the key is quoted properly
                    String key = e.getKey();
                    return "\"" + key + "\":" + e.getValue().toString();
                })
                .collect(Collectors.joining(",", "{", "}"));
    }


}