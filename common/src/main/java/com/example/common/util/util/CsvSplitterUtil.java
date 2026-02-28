package com.example.common.util.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReadConfig;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvSplitterUtil {

    private static final long MAX_SIZE = 29 * 1024 * 1024; // 30MB


    public static void csvSplitter() {

        System.out.println("-------将指定的csvw文件拆分成多份，每份最大30M-------");

        Scanner scanner = new Scanner(System.in);

        // 获取用户输入的源目录和目标目录
        System.out.print("请输入CSV文件全路径（包含文件名）: ");
        String inputPath = scanner.nextLine().trim();

        System.out.print("请输入拆分后保存文件的路径: ");
        String outputDir = scanner.nextLine().trim();

        scanner.close();


        List<String> resultFiles = splitCsvWithHeader(new File(inputPath), outputDir);

        System.out.println("拆分完成，生成文件:");
        resultFiles.forEach(System.out::println);
    }


    /**
     * 拆分CSV文件（保留标题行）
     * @param inputFile 输入文件
     * @param outputDir 输出目录
     * @return 生成的子文件路径列表
     */
    public static List<String> splitCsvWithHeader(File inputFile, String outputDir) {
        List<String> resultFiles = new ArrayList<>();

        // 1. 读取标题行
        CsvReadConfig readConfig = CsvReadConfig.defaultConfig();
        readConfig.setContainsHeader(true);

        try (CsvReader reader = CsvUtil.getReader(readConfig)) {
            CsvData headerData = reader.read(inputFile, StandardCharsets.UTF_8);
            if (headerData.getRowCount() == 0) {
                throw new IllegalArgumentException("CSV文件为空或格式错误");
            }

            String[] headers = headerData.getHeader().toArray(new String[0]);
            String headerLine = buildHeaderLine(headers);

            // 2. 流式处理数据行
            try (BufferedReader br = FileUtil.getReader(inputFile, CharsetUtil.CHARSET_UTF_8)) {
                String line;
                int fileCount = 1;
                long currentSize = 0;
                List<String> currentChunk = new ArrayList<>();
                currentChunk.add(headerLine); // 添加标题行
                currentSize += headerLine.getBytes(StandardCharsets.UTF_8).length;

                // 跳过原始标题行
                br.readLine();

                while ((line = br.readLine()) != null) {
                    long lineSize = line.getBytes(StandardCharsets.UTF_8).length;

                    // 3. 达到29MB时创建新文件
                    if (currentSize + lineSize > MAX_SIZE) {
                        String outputPath = writeChunk(outputDir, fileCount++, currentChunk);
                        resultFiles.add(outputPath);
                        currentChunk = new ArrayList<>();
                        currentChunk.add(headerLine); // 新文件添加标题行
                        currentSize = headerLine.getBytes(StandardCharsets.UTF_8).length;
                    }

                    currentChunk.add(line);
                    currentSize += lineSize;
                }

                // 4. 写入最后一块数据
                if (currentChunk.size() > 1) {
                    String outputPath = writeChunk(outputDir, fileCount, currentChunk);
                    resultFiles.add(outputPath);
                }
            }
        } catch (IORuntimeException e) {
            throw new RuntimeException("CSV文件读取失败: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IO异常: " + e.getMessage(), e);
        }

        return resultFiles;
    }

    // 构建标题行（处理包含特殊字符的字段）
    private static String buildHeaderLine(String[] headers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headers.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(escapeCsvField(headers[i]));
        }
        return sb.toString();
    }

    // CSV字段转义处理
    private static String escapeCsvField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    // 写入单个分块文件
    private static String writeChunk(String outputDir, int fileNum, List<String> lines) {
        String fileName = outputDir + File.separator + "part_" + fileNum + ".csv";
        File outputFile = FileUtil.touch(fileName);
        // Charset.forName("GBK")  StandardCharsets.UTF_8
        try (BufferedWriter writer = FileUtil.getWriter(outputFile, Charset.forName("GBK"), false)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("写入文件失败: " + fileName, e);
        }

        return fileName;
    }


}