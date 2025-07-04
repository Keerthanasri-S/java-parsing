package com.example.json;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.List;

import static com.example.json.Json.parse;
import static org.junit.jupiter.api.Assertions.*;

class JsonTest {
    @Test
    void testNull() throws IOException {
        Assertions.assertNull(parse(Reader.of("  null")),"not a value");
    }

    @Test
    void testNullWithSpace() throws IOException {
        Assertions.assertNull(parse(Reader.of("    null")),"not a value");
    }

    @Test
    void testBoolean() throws IOException {
        Assertions.assertEquals(true,parse(Reader.of("true")),"not a value");
        Assertions.assertEquals(false,parse(Reader.of("false")),"not a value");
    }
    @Test
    void testBooleanWithSpace() throws IOException {
        Assertions.assertEquals(true,parse(Reader.of("   true")),"not a value");
        Assertions.assertEquals(false,parse(Reader.of("  false")),"not a value");
    }
    @Test
    void testString() throws IOException {
        Assertions.assertEquals("Sampletext",parse(Reader.of("\"Sampletext\"")));
    }
    @Test
    void testStringUsingFile() throws IOException {
        Files.readAllLines(new File("src/test/resourses/json-string.txt").toPath())
                .forEach(line->{
                    String ParsedWithJsonText = JsonParser.parseString(line).getAsString();
                    try {
                        Assertions.assertEquals(ParsedWithJsonText, parse(Reader.of(line)),"String fetch failed");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    @Test
    void testNumberUsingFile() throws IOException {
        Files.readAllLines(new File("src/test/resourses/json-number.txt").toPath())
                .forEach(line -> {
                    try {
                        Number parsedFromGson = JsonParser.parseString(line).getAsNumber();
                        Number parsedFromCustom = (Number) parse(Reader.of(line)); // Now this is safe


                        assertEquals(
                                parsedFromGson.doubleValue(),
                                ((Number) parsedFromCustom).doubleValue(),
                                "Number fetch failed"
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }




    @Test
    void testArrayUsingFile() throws IOException {
        Files.readAllLines(new File("src/test/resourses/json-array.txt").toPath().toAbsolutePath()).
                forEach(line->{
                    List parsedWithJsonArray = JsonParser.parseString(line).getAsJsonArray().asList();

                    try {
                        Assertions.assertEquals(parsedWithJsonArray,parse(Reader.of(line)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });


    }





}