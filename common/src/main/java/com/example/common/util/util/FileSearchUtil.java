package com.example.common.util.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class FileSearchUtil {
    /**
     * 根据文件内容搜索文件
     */
    public static void fileSearch() {
        System.out.println("-------文件搜索：在指定目录中搜索指定内容的sql和txt文件-------");

        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入要搜索的目录路径（默认D:/工作任务）: ");
        String directoryPath = scanner.nextLine().trim();
        if(StringUtils.isEmpty(directoryPath)){
            directoryPath = "D:/工作任务";
        }

        System.out.print("请输入要搜索的内容: ");
        String searchContent = scanner.nextLine().trim();

        scanner.close();

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("错误：指定的目录不存在或不是一个有效目录！");
            return;
        }

        System.out.println("开始搜索...");
        System.out.println("目录: " + directoryPath);
        System.out.println("搜索内容: " + searchContent);
        System.out.println("=========================================");

        long startTime = System.currentTimeMillis();
        List<String> resultFiles = new ArrayList<>();

        // 使用迭代方式搜索，避免递归深度问题
        searchFilesWithIteration(directory, searchContent, resultFiles);

        long endTime = System.currentTimeMillis();

        System.out.println("\n" + "=========================================");
        System.out.println("搜索完成！耗时: " + (endTime - startTime) + " 毫秒");

        if (resultFiles.isEmpty()) {
            System.out.println("未找到包含指定内容的文件。");
        } else {
            System.out.println("找到 " + resultFiles.size() + " 个包含指定内容的文件：");
            for (int i = 0; i < resultFiles.size(); i++) {
                System.out.println((i + 1) + ". " + resultFiles.get(i));
            }
        }
    }

    /**
     * 使用迭代方式搜索文件（避免递归深度问题）
     */
    private static void searchFilesWithIteration(File rootDir, String searchContent, List<String> resultFiles) {
        Queue<File> queue = new LinkedList<>();
        queue.add(rootDir);

        while (!queue.isEmpty()) {
            File current = queue.poll();

            if (current.isDirectory()) {
                File[] files = current.listFiles();
                if (files == null) {
                    System.out.println("无法读取目录: " + current.getAbsolutePath());
                    continue;
                }
                Collections.addAll(queue, files);
            } else {
                processFile(current, searchContent, resultFiles);
            }
        }
    }

    /**
     * 处理单个文件：检查文件类型和内容
     */
    private static void processFile(File file, String searchContent, List<String> resultFiles) {
        String fileName = file.getName().toLowerCase();

        // 只处理.sql和.txt文件
        if (!fileName.endsWith(".sql") && !fileName.endsWith(".txt")) {
            return;
        }

        System.out.println("检查文件: " + file.getAbsolutePath());

        // 检查文件内容
        if (fileContainsContent(file, searchContent)) {
            resultFiles.add(file.getAbsolutePath());
            System.out.println(">>> 找到匹配文件: " + file.getAbsolutePath());
        }
    }

    /**
     * 检查文件内容是否包含指定文本（支持多编码）
     */
    private static boolean fileContainsContent(File file, String searchContent) {
        // 尝试多种编码格式
        String[] encodings = {"UTF-8", "GBK", "GB2312", "ISO-8859-1"};

        for (String encoding : encodings) {
            if (tryReadingWithEncoding(file, searchContent, encoding)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 使用特定编码尝试读取文件并搜索内容
     */
    private static boolean tryReadingWithEncoding(File file, String searchContent, String encoding) {
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, encoding);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(searchContent.toLowerCase())) {
                    return true;
                }
            }

        } catch (UnsupportedEncodingException e) {
            System.out.println("不支持的编码格式: " + encoding + " - " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("读取文件错误: " + file.getAbsolutePath() + " - " + e.getMessage());
        }

        return false;
    }

    /**
     * 自动检测文件编码（简化版）
     */
    private static String detectFileEncoding(File file) {
        // 尝试通过BOM标记检测UTF-8
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bom = new byte[3];
            int bytesRead = fis.read(bom);

            if (bytesRead >= 3 &&
                    bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF) {
                return "UTF-8";
            }
        } catch (IOException e) {
            // 忽略错误，继续尝试其他编码
        }

        // 默认返回系统编码，在实际应用中可扩展更复杂的检测逻辑
        return Charset.defaultCharset().name();
    }
}
