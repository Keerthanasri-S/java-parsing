package com.example.json;

import com.example.json.types.*;

import java.io.IOException;
import java.io.Reader;

public interface Json<T> {

    static Object parse(Reader reader) throws IOException {
        try (reader) {
            return read(reader).getValue();
        }
    }

    T getValue();

    static Json<?> read(Reader reader) throws IOException {
        int ch;
        do {
            ch = reader.read();
        } while (Character.isWhitespace(ch));

        if (ch == -1) return null;

        char firstChar = (char) ch;
       // char firstChar = (char) reader.read();
        return getJson(new JsonReader(reader), firstChar);
    }

    static Json<?> getJson(JsonReader reader, char firstChar) throws IOException {
        if (firstChar == 'n') {
            return new JsonNull(reader);
        } else if (firstChar == 't') {
            return new JsonBoolean(reader, true);
        } else if (firstChar == 'f') {
            return new JsonBoolean(reader, false);
        } else if (firstChar == '"') {
            return new JsonString(reader);
        } else if (Character.isDigit(firstChar) || firstChar == '-') {
            return new JsonNumber(firstChar, reader);
        }
//        else if (firstChar == '[') {
//            return new JsonArray(reader);
//        }
        return null;
    }

}
