package com.example.json.types;

import com.example.json.Json;
import com.example.json.JsonReader;

import java.io.IOException;

public class JsonBoolean implements Json<Boolean> {
    private final boolean value;

    public JsonBoolean(final JsonReader reader, boolean value) throws IOException {
        this.value = value;
        if(value){
            char[] values = new char[3];
            reader.read(values);
            if(values[0] != 'r' || values[1] != 'u' || values[2] != 'e'){
                throw new IllegalArgumentException("not a true value");
            }
        }else{
            char[] values = new char[4];
            reader.read(values);
            if(values[0] != 'a' || values[1] != 'l' || values[2] != 's' || values[3] != 'e'){
                throw new IllegalArgumentException("not a false value");
            }
        }


    }

    @Override
    public Boolean getValue() {
        return this.value;
    }
    @Override
    public String toString() {
        return Boolean.toString(value);  // <- THIS is the fix
    }
}




