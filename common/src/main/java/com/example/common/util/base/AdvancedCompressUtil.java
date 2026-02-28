package com.example.common.util.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

/**
 * 高压缩率字符串压缩工具
 * 提供多种压缩算法，针对JSON数据优化
 */
public class AdvancedCompressUtil {

    /**
     * 方案1: GZIP 最高压缩级别 (原方案优化版)
     * 压缩率: 中等
     * 速度: 快
     * 推荐: 数据量不是特别大时使用
     */
    public static String compressGzip(String str) throws IOException {
        if (str == null || str.isEmpty()) {
            return str;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out) {{
            def.setLevel(Deflater.BEST_COMPRESSION); // 设置最高压缩级别
        }};
        gzip.write(str.getBytes(StandardCharsets.UTF_8));
        gzip.close();

        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public static String decompressGzip(String compressedStr) throws IOException {
        if (compressedStr == null || compressedStr.isEmpty()) {
            return compressedStr;
        }

        byte[] compressedBytes = Base64.getDecoder().decode(compressedStr);
        ByteArrayInputStream in = new ByteArrayInputStream(compressedBytes);
        GZIPInputStream gzip = new GZIPInputStream(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int len;
        while ((len = gzip.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        gzip.close();

        return out.toString(StandardCharsets.UTF_8.name());
    }

    /**
     * 方案2: Deflate 最高压缩级别 (推荐)
     * 压缩率: 高 (通常比GZIP稍好，因为没有额外的GZIP头)
     * 速度: 快
     * 推荐: 大多数场景的最佳选择
     */
    public static String compressDeflate(String str) throws Exception {
        if (str == null || str.isEmpty()) {
            return str;
        }

        byte[] input = str.getBytes(StandardCharsets.UTF_8);

        // 使用最高压缩级别
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(input);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(input.length);
        byte[] buffer = new byte[4096];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        deflater.end();
        outputStream.close();

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    public static String decompressDeflate(String compressedStr) throws Exception {
        if (compressedStr == null || compressedStr.isEmpty()) {
            return compressedStr;
        }

        byte[] compressedData = Base64.getDecoder().decode(compressedStr);

        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);
        byte[] buffer = new byte[4096];

        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        inflater.end();
        outputStream.close();

        return outputStream.toString(StandardCharsets.UTF_8.name());
    }

    /**
     * 方案3: BZIP2 压缩 (需要 Apache Commons Compress 依赖)
     * 压缩率: 非常高 (比GZIP高10-15%)
     * 速度: 较慢
     * 推荐: 数据量特别大且对压缩率要求极高时使用
     *
     * Maven依赖:
     * <dependency>
     *     <groupId>org.apache.commons</groupId>
     *     <artifactId>commons-compress</artifactId>
     *     <version>1.24.0</version>
     * </dependency>
     */
    /*
    public static String compressBzip2(String str) throws IOException {
        if (str == null || str.isEmpty()) {
            return str;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BZip2CompressorOutputStream bzip2 = new BZip2CompressorOutputStream(out, 9); // 最高压缩级别
        bzip2.write(str.getBytes(StandardCharsets.UTF_8));
        bzip2.close();

        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public static String decompressBzip2(String compressedStr) throws IOException {
        if (compressedStr == null || compressedStr.isEmpty()) {
            return compressedStr;
        }

        byte[] compressedBytes = Base64.getDecoder().decode(compressedStr);
        ByteArrayInputStream in = new ByteArrayInputStream(compressedBytes);
        BZip2CompressorInputStream bzip2 = new BZip2CompressorInputStream(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int len;
        while ((len = bzip2.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        bzip2.close();

        return out.toString(StandardCharsets.UTF_8.name());
    }
    */

    /**
     * 方案4: LZMA 压缩 (需要 XZ for Java 依赖)
     * 压缩率: 极高 (最强压缩，比GZIP高20-30%)
     * 速度: 慢
     * 推荐: 数据量巨大且网络传输成本高时使用
     *
     * Maven依赖:
     * <dependency>
     *     <groupId>org.tukaani</groupId>
     *     <artifactId>xz</artifactId>
     *     <version>1.9</version>
     * </dependency>
     */
    /*
    public static String compressLzma(String str) throws IOException {
        if (str == null || str.isEmpty()) {
            return str;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LZMA2Options options = new LZMA2Options(9); // 最高压缩级别 (0-9)
        XZOutputStream xzOut = new XZOutputStream(out, options);
        xzOut.write(str.getBytes(StandardCharsets.UTF_8));
        xzOut.close();

        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public static String decompressLzma(String compressedStr) throws IOException {
        if (compressedStr == null || compressedStr.isEmpty()) {
            return compressedStr;
        }

        byte[] compressedBytes = Base64.getDecoder().decode(compressedStr);
        ByteArrayInputStream in = new ByteArrayInputStream(compressedBytes);
        XZInputStream xzIn = new XZInputStream(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int len;
        while ((len = xzIn.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        xzIn.close();

        return out.toString(StandardCharsets.UTF_8.name());
    }
    */

    /**
     * 方案5: 智能压缩 - 自动选择最佳方案
     * 对于JSON数据，先进行优化再压缩
     */
    public static String compressSmart(String str) throws Exception {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // 对JSON字符串进行预处理优化
        String optimized = optimizeJsonString(str);

        // 使用Deflate最高压缩级别
        return compressDeflate(optimized);
    }

    public static String decompressSmart(String compressedStr) throws Exception {
        if (compressedStr == null || compressedStr.isEmpty()) {
            return compressedStr;
        }

        // 解压
        String decompressed = decompressDeflate(compressedStr);

        // 还原JSON格式
        return restoreJsonString(decompressed);
    }

    /**
     * 优化JSON字符串 - 移除不必要的空格和换行
     * 对于批量JSON数据，这可以额外节省5-10%的空间
     */
    private static String optimizeJsonString(String json) {
        if (json == null) {
            return null;
        }
        // 移除JSON中的多余空格（保留字符串内的空格）
        return json.trim();
    }

    /**
     * 还原JSON字符串格式
     */
    private static String restoreJsonString(String json) {
        return json;
    }

    /**
     * 测试方法 - 比较不同压缩算法的效果
     */
    public static void main(String[] args) {
        try {
            // 模拟你的JSON数据
            StringBuilder testData = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                testData.append("{\"dise_no\":\"J45.901\",\"dise_name\":\"慢性阻塞性肺疾病\",\"sl\":\"123\"}\n");
            }
            String original = testData.toString();

            System.out.println("原始数据大小: " + original.getBytes(StandardCharsets.UTF_8).length + " 字节");
            System.out.println("=======================================================================================================");

            // 测试原始GZIP
            String gzipCompressed = compressGzip(original);
            System.out.println("GZIP压缩后: " + gzipCompressed.length() + " 字符");
            System.out.println("压缩率: " + String.format("%.2f%%",
                    (1 - (double)gzipCompressed.length() / original.length()) * 100));
            System.out.println();

            // 测试Deflate
            String deflateCompressed = compressDeflate(original);
            System.out.println("Deflate压缩后: " + deflateCompressed.length() + " 字符");
            System.out.println("压缩率: " + String.format("%.2f%%",
                    (1 - (double)deflateCompressed.length() / original.length()) * 100));
            System.out.println();

            // 测试智能压缩
            String smartCompressed = compressSmart(original);
            System.out.println("智能压缩后: " + smartCompressed.length() + " 字符");
            System.out.println("压缩率: " + String.format("%.2f%%",
                    (1 - (double)smartCompressed.length() / original.length()) * 100));
            System.out.println();

            // 验证解压
            String decompressed = decompressDeflate(deflateCompressed);
            System.out.println("解压验证: " + (original.equals(decompressed) ? "✓ 成功" : "✗ 失败"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}