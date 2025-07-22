
package com.example.json.types;

import com.example.json.Json;
import com.example.json.JsonReader;

import java.io.IOException;

public  class JsonString implements Json<String> {
    private final StringBuilder builder;
    public JsonString(JsonReader reader) throws IOException {
        builder = buildString(reader);
    }


    public static StringBuilder buildString(final JsonReader reader) throws IOException {
        final StringBuilder sb = new StringBuilder();
        char a;
        while ((a = (char) reader.read()) != '\\' && a != '"') {
            sb.append(a);
        }
        if (a == '\\') {
            a = ((char) reader.read());
            if (a == '"' || a == '\\' || a == '\'' || a == '/') {
                sb.append(a);
            } else if (a == 'n') {
                sb.append('\n');
            } else if (a == 'b') {
                sb.append('\b');
            } else if (a == 'r') {
                sb.append('\r');
            } else if (a == 't') {
                sb.append('\t');
            } else if (a == 'f') {
                sb.append('\f');
            } else if (a == 'u') {
                char[] value = new char[4];
                for (int i = 0; i < 4; i++) {
                    int next = reader.read();
                    value[i] = (char) next;
                }
                String hexstr = new String(value);
                int value1 = Integer.parseInt(hexstr, 16);
                sb.append((char) value1);
            }
            sb.append(buildString(reader));
        }
        return sb;
    }

    @Override
    public String getValue() {
        return builder.toString();
    }

    @Override
    public String toString() {
        return  builder.toString().replace("\"", "\\\"") ;
    }

}
