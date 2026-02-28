package com.example.common.util.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressUtil {

    public static String compress(String str) throws IOException {
        if (str == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        byte[] compressedBytes = out.toByteArray();
        return Base64.getEncoder().encodeToString(compressedBytes);
    }

    public static String decompress(String compressedStr) throws IOException {
        if (compressedStr == null) {
            return null;
        }
        byte[] compressedBytes = Base64.getDecoder().decode(compressedStr);
        ByteArrayInputStream in = new ByteArrayInputStream(compressedBytes);
        GZIPInputStream gzip = new GZIPInputStream(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = gzip.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        gzip.close();
        return out.toString();
    }
}
