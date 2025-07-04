package com.example.json.types;

import com.example.json.Json;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

public class JsonReader extends Reader {

    private final PushbackReader reader;

    public JsonReader(Reader reader) {
        this.reader = new PushbackReader(reader);
    }

    public Json<?> readElement() throws IOException {
        int ch;
        do {
            ch = reader.read();
        } while (Character.isWhitespace(ch));

        if (ch == -1) return null;

        char firstChar = (char) ch;

        if (firstChar == 'n') {
            return new JsonNull(this); // use current JsonReader
        } else if (firstChar == 't') {
            return new JsonBoolean(this, true);
        } else if (firstChar == 'f') {
            return new JsonBoolean(this, false);
        } else if (firstChar == '"') {
            return new JsonString(this);
        } else if (Character.isDigit(firstChar) || firstChar == '-') {
            return new JsonNumber(firstChar, this);
        }
//        else if (firstChar == '[') {
//            return new JsonArray(this);
//        }

        throw new IOException("Unexpected character in JSON: " + firstChar);
    }

    public void skipWhitespace() throws IOException {
        int ch;
        while ((ch = reader.read()) != -1) {
            if (!Character.isWhitespace(ch)) {
                reader.unread(ch);
                break;
            }
        }
    }

//    public int peek() throws IOException {
//        int ch = reader.read();
//        if (ch != -1) {
//            reader.unread(ch);
//        }
//        return ch;
//    }

    public void unread(int ch) throws IOException {
        reader.unread(ch);
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        return reader.read(target);
    }

    @Override
    public int read() throws IOException {
        return reader.read();
    }

    @Override
    public int read(char[] cbuf) throws IOException {
        return reader.read(cbuf);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return reader.skip(n);
    }

    @Override
    public boolean ready() throws IOException {
        return reader.ready();
    }

    @Override
    public boolean markSupported() {
        return reader.markSupported();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        reader.mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        reader.reset();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public long transferTo(Writer out) throws IOException {
        return reader.transferTo(out);
    }

}
