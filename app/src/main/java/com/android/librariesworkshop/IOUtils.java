package com.android.librariesworkshop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class IOUtils {

    private static final ThreadLocal<char[]> sCharArrayBuffer = new ThreadLocal<char[]>() {
        @Override
        protected char[] initialValue() {
            return new char[8192];
        }
    };

    /**
     * Returns a String representation of the contents of an InputStream
     *
     * @param is
     * @return
     * @throws java.io.IOException
     */
    public static String toString(InputStream is) throws IOException {
        final char[] buffer = sCharArrayBuffer.get();
        final Writer writer = new StringWriter();

        try {
            final Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

            return writer.toString();
        } catch (UnsupportedEncodingException e) {
            throw new IOException(e);
        }
    }
}
