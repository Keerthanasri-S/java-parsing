package com.example.json.types;

import com.example.json.Json;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonArray implements Json<List<Object>> {

    private final List<Json<?>> elements;

    public JsonArray(JsonReader reader) throws IOException {
        this.elements = parseArray(reader);
    }

    private List<Json<?>> parseArray(JsonReader reader) throws IOException {
        List<Json<?>> list = new ArrayList<>();

        reader.skipWhitespace();
        int ch = reader.read();

        if (ch == ']') {
            return list; // empty array
        }

        reader.unread(ch); // push back for first element
        list.add(Json.read(reader)); // read first element

        while (true) {
            reader.skipWhitespace();
            ch = reader.read();

            if (ch == ',') {
                reader.skipWhitespace();
                list.add(Json.read(reader));
            } else if (ch == ']') {
                break; // end of array
            } else {
                throw new IOException("Expected ',' or ']', but found: '" + (char) ch + "'");
            }
        }

        return list;
    }

    @Override
    public List<Object> getValue() {
        List<Object> values = new ArrayList<>();
        for (Json<?> json : elements) {
            values.add(json.getValue());
        }
        return values;
    }
}
