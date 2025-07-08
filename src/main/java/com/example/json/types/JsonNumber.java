package com.example.json.types;

import com.example.json.Json;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonNumber implements Json<Number> {

    private final StringBuilder builder;

    public JsonNumber(char firstChar, JsonReader reader) throws IOException {
        builder = new StringBuilder().append(firstChar).append(buildString(reader));

    }

    private StringBuilder buildString(JsonReader reader) throws IOException {
        final StringBuilder sb = new StringBuilder();

        int a;
        char c = (char) reader.peek();
        while ((a = reader.read()) != -1) {
            sb.append((char) a);

        }
        return sb;
    }


    //private static final int DEFAULT_MAX_NUM_LEN = 100;

    public Number buildNumber(StringBuilder numberBuilder) {
        String numberStr = numberBuilder.toString();
        try {
            // Try to parse as different types based on the range
            if (numberStr.contains(".")) {
                //Check if the number is a decimal
//                double doubleValue = Double.parseDouble(numberStr);
//                if (doubleValue >= -Float.MAX_VALUE
//                        && doubleValue <= Float.MAX_VALUE) {
//                    return (float) doubleValue;
//                } else {
//                    return doubleValue;
//             //   }
                return new BigDecimal(numberStr).stripTrailingZeros();

            } else {
                // Check if the number is an integer
//                if (numberBuilder.length() > DEFAULT_MAX_NUM_LEN) {
//                    throw new IllegalArgumentException(
//                            "Number value length exceeds the maximum allowed ("
//                                    + DEFAULT_MAX_NUM_LEN + ")");
//                }
                long longValue = Long.parseLong(numberStr);
                if (longValue >= Byte.MIN_VALUE
                        && longValue <= Byte.MAX_VALUE) {
                    return (byte) longValue;
                } else if (longValue >= Short.MIN_VALUE
                        && longValue <= Short.MAX_VALUE) {
                    return (short) longValue;
                } else if (longValue >= Integer.MIN_VALUE
                        && longValue <= Integer.MAX_VALUE) {
                    return (int) longValue;
                } else {
                    return longValue;
                }
            }
        } catch (NumberFormatException e) {
            return parseBigNumber(numberStr);
        }
    }

        private Number parseBigNumber(final String numberStr) {

        try {
            return new BigInteger(numberStr); // Try BigInteger first
        } catch (NumberFormatException e) {
            // Only create BigDecimal if BigInteger fails
            BigDecimal bd = new BigDecimal(numberStr);
            try {
                // Convert to BigInteger if there's no fraction
                return bd.toBigIntegerExact();
            } catch (ArithmeticException ex) {
                return bd; // If it's a decimal, return BigDecimal
            }
        }
    }

    @Override
    public Number getValue() {

        return buildNumber(builder);
    }


}

