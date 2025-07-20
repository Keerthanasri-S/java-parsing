package com.example.json;

//import com.example.json.types.JsonObjects;
import com.example.json.types.JsonObjects;
import com.google.gson.*;
import com.tngtech.archunit.thirdparty.com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
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
        Files.readAllLines(new File("src/test/resources/json-string.txt").toPath())
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
        Files.readAllLines(new File("src/test/resources/json-number.txt").toPath())
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
        Files.readAllLines(new File("src/test/resources/json-array.txt").toPath())
                .forEach(line -> {
                    Gson gson = new GsonBuilder()
                            .setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
                            .create();

                    Type type = new TypeToken<List<Object>>() {}.getType();
                    List<Object> expected = gson.fromJson(line, type);

                    try {
                        List<Object> actual = (List<Object>) Json.parse(new StringReader(line));

                        Assertions.assertEquals(expected.size(), actual.size(), "Array size mismatch");
//
//                        for (int i = 0; i < expected.size(); i++) {
//                            Object exp = expected.get(i);
//                            Object act = actual.get(i);
//
//                            System.out.println("Index: " + i);
//                            System.out.println("Expected: " + exp + " (" + exp.getClass().getSimpleName() + ")");
//                            System.out.println("Actual  : " + act + " (" + act.getClass().getSimpleName() + ")");

                            if (expected instanceof Number && actual instanceof Number) {
                                try {
                                    BigInteger expInt = new BigDecimal(expected.toString()).toBigIntegerExact();
                                    BigInteger actInt = new BigDecimal(actual.toString()).toBigIntegerExact();

                                    Assertions.assertEquals(
                                            expInt,
                                            actInt,
                                            "Mismatch at index "
                                    );
                                } catch (ArithmeticException e) {
                                    // fallback to BigDecimal comparison for decimal numbers
                                    BigDecimal expDec = new BigDecimal(expected.toString());
                                    BigDecimal actDec = new BigDecimal(actual.toString());

                                    Assertions.assertEquals(
                                            0,
                                            expDec.compareTo(actDec),
                                            "Mismatch at index "
                                    );
                                }
                            } else {
                                Assertions.assertEquals(
                                        expected.toString(),
                                        actual.toString(),
                                        "Mismatch at index "
                                );
                            }

                            System.out.println(); // just a blank line for spacing


                    } catch (Exception e) {
                        e.printStackTrace();
//                        System.out.println("Invalid JSON format: " + line);
                    }
                });
    }

    @Test
    void testObject() throws IOException {
        List<String> lines = Files.readAllLines(new File("src/test/resources/json-objects.txt").toPath());

        for (String line : lines) {
            if(line.trim().isEmpty())
                continue;
            try {
                // Parse using Gson
                JsonObject parsedFromGson = JsonParser.parseString(line).getAsJsonObject();

                // Parse using your custom parser
                JsonReader reader = new JsonReader(new StringReader(line));
                JsonObjects customParsed = new JsonObjects( reader);
                System.out.println(line);
                // Convert both to string and compare
                assertEquals(
                        parsedFromGson.toString(),
                        customParsed.toString(),
                        "object fetch failed for input: " + line
                );

            } catch (IOException e) {
                System.out.println(line);
                throw new RuntimeException(e);
            }
        }
    }
}






