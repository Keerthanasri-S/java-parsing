package com.example.json.types;

import com.example.json.Json;
import com.example.json.JsonReader;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonArray implements Json<List<Object>> {

    private final List<Json<?>> elements;


    public JsonArray(JsonReader reader, Json.ParseMode mode) throws IOException {
        this.elements = parseArray(reader, mode);
    }

    private List<Json<?>> parseArray(JsonReader reader, Json.ParseMode mode) throws IOException {
        List<Json<?>> list = new ArrayList<>();

        reader.skipWhitespace();
        int ch = reader.read();
        if (ch == ']') {
            return list;
        }

        reader.unread(ch); // put back for parsing
        list.add(Json.read(reader, mode));

        while (true) {
            reader.skipWhitespace();
            ch = reader.read();

            if (ch == ',') {
                reader.skipWhitespace();
                list.add(Json.read(reader, mode));
            } else if (ch == ']') {
                break;
            } else {
                throw new IOException("Expected ',' or ']' but found: " + (char) ch);
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
