package com.example.json;

import com.example.json.types.*;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public interface Json<T> {

    enum ParseMode {
        NORMAL,
        PRIORITIZE_NUMBERS
    }

    T getValue();

    static Object parse(Reader reader) throws IOException {
        try (reader) {
            JsonReader jsonReader = new JsonReader(reader);
            return read(jsonReader, ParseMode.NORMAL).getValue();
        }
    }

    static Json<?> read(JsonReader reader, ParseMode mode) throws IOException {
        reader.skipWhitespace();
        int ch = reader.read();
        if (ch == -1) return null;

        char firstChar = (char) ch;
        return getJson(reader, firstChar, mode);
    }

    static Json<?> read(Reader reader) throws IOException {
        return read(new JsonReader(reader), ParseMode.NORMAL);
    }

    static Json<?> getJson(JsonReader reader, char firstChar, ParseMode mode) throws IOException {
        if (mode == ParseMode.PRIORITIZE_NUMBERS) {
            if (Character.isDigit(firstChar) || firstChar == '-') {
                return new JsonNumber(firstChar, reader);
            }
        }
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
        } else if (firstChar == '[') {
            return new JsonArray(reader, mode);
        }
        else if (firstChar == '{') {
            return new JsonObjects(reader);
        }
        throw new IOException("Unexpected character in JSON: " + firstChar);
    }


}
