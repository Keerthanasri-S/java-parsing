//package com.example.json.type;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Collections;
//
//public class JsonArray implements Json<List<Object>> {
//
//    private final List<Json<?>> elements;
//
//    public JsonArray(JsonReader reader) throws IOException {
//        this.elements = parseArray(reader);
//    }
//
//    private List<Json<?>> parseArray(JsonReader reader) throws IOException {
//        List<Json<?>> list = new ArrayList<>();
//
//        reader.skipWhitespace();
//        int ch = reader.read();
//        if (ch == ']') {
//            return list; // empty array: []
//        }
//
//        reader.unread(ch); // put back the first character for value parsing
//
//        // Read first element
//        list.add(Json.read(reader));
//
//        // Check for more elements
//        while (true) {
//            reader.skipWhitespace();
//            ch = reader.read();
//
//            if (ch == ',') {
//                reader.skipWhitespace();
//                list.add(Json.read(reader));
//            } else if (ch == ']') {
//                break; // End of array
//            } else {
//                throw new IOException("Expected ',' or ']' but found: " + (char) ch);
//            }
//        }
//
//        return list;
//    }
//
//    @Override
//    public List<Object> getValue() {
//        List<Object> values = new ArrayList<>();
//        for (Json<?> json : elements) {
//            values.add(json.getValue());
//        }
//        return values;
//    }
//
//
//}
