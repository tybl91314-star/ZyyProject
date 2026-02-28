package com.example.common.util.util;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class FileCopyUtil {

    public static void main(String[] args) {
        fileCopy();
    }

    /**
     * 将指定目录下的所有sql文件和txt文件，按原目录结构拷贝到新目录中
     */
    public static void fileCopy(){

        System.out.println("-------将指定目录下的所有sql文件和txt文件，按原目录结构拷贝到新目录中-------");

        Scanner scanner = new Scanner(System.in);

        // 获取用户输入的源目录和目标目录
        System.out.print("请输入源目录路径: ");
        String sourceDirPath = scanner.nextLine().trim();

        System.out.print("请输入目标目录路径: ");
        String targetDirPath = scanner.nextLine().trim();

        scanner.close();

        // 验证目录是否存在
        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            System.err.println("错误: 源目录不存在或不是目录!");
            return;
        }

        File targetDir = new File(targetDirPath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
            System.out.println("目标目录已创建: " + targetDirPath);
        }

        try {
            // 执行文件复制
            copySpecificFiles(sourceDir, targetDir, new String[]{"txt", "sql"});
            System.out.println("文件复制完成!");
        } catch (IOException e) {
            System.err.println("复制过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 复制特定类型的文件，保持目录结构，并转换编码为UTF-8
     * @param sourceDir
     * @param targetDir
     * @param fileExtensions 要拷贝文件的后缀
     * @throws IOException
     */
    public static void copySpecificFiles(File sourceDir, File targetDir, String[] fileExtensions)
            throws IOException {

        // 遍历源目录
        Files.walk(sourceDir.toPath())
                .filter(path -> {
                    File file = path.toFile();
                    if (file.isFile()) {
                        String fileName = file.getName();
                        int dotIndex = fileName.lastIndexOf('.');
                        if (dotIndex > 0) {
                            String extension = fileName.substring(dotIndex + 1).toLowerCase();
                            for (String ext : fileExtensions) {
                                if (ext.equalsIgnoreCase(extension)) {
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                })
                .forEach(sourcePath -> {
                    try {
                        // 计算相对路径
                        Path relativePath = sourceDir.toPath().relativize(sourcePath);
                        Path targetPath = targetDir.toPath().resolve(relativePath);

                        // 创建目标目录结构
                        Files.createDirectories(targetPath.getParent());

                        // 复制并转换文件编码
                        copyFileWithEncodingConversion(sourcePath.toFile(), targetPath.toFile());

                        System.out.println("已复制: " + relativePath);
                    } catch (IOException e) {
                        System.err.println("复制文件失败: " + sourcePath + " - " + e.getMessage());
                    }
                });
    }

    /**
     * 复制文件并进行编码转换（GBK → UTF-8）
     */
    private static void copyFileWithEncodingConversion(File sourceFile, File targetFile)
            throws IOException {

        // 检测文件编码
        String encoding = detectFileEncoding(sourceFile);

        if ("UTF-8".equalsIgnoreCase(encoding)) {
            // 如果已经是UTF-8，直接复制
            Files.copy(sourceFile.toPath(), targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } else {
            // 进行编码转换（主要处理GBK到UTF-8）
            convertFileEncoding(sourceFile, targetFile, encoding, "UTF-8");
        }
    }

    /**
     * 检测文件编码
     */
    private static String detectFileEncoding(File file) throws IOException {
        // 简单的编码检测逻辑
        // 优先尝试UTF-8，如果失败则尝试GBK
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)) {

            char[] buffer = new char[1024];
            while (isr.read(buffer) != -1) {
                // 成功读取，可能是UTF-8
            }
            return "UTF-8";
        } catch (MalformedInputException e) {
            // 如果不是UTF-8，尝试GBK
            try (FileInputStream fis = new FileInputStream(file);
                 InputStreamReader isr = new InputStreamReader(fis, "GBK")) {

                char[] buffer = new char[1024];
                while (isr.read(buffer) != -1) {
                    // 成功读取，可能是GBK
                }
                return "GBK";
            } catch (MalformedInputException e2) {
                // 如果GBK也失败，默认使用UTF-8
                return "UTF-8";
            }
        }
    }

    /**
     * 转换文件编码
     */
    private static void convertFileEncoding(File sourceFile, File targetFile,
                                            String sourceEncoding, String targetEncoding)
            throws IOException {

        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(sourceFile), sourceEncoding);
             OutputStreamWriter writer = new OutputStreamWriter(
                     new FileOutputStream(targetFile), targetEncoding)) {

            char[] buffer = new char[8192];
            int length;
            while ((length = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, length);
            }
        }
    }

    /**
     * 备用方法：使用传统的递归方式复制文件（如果需要更细粒度的控制）
     */
    public static void copyFilesRecursive(File source, File target, String[] extensions)
            throws IOException {

        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdirs();
            }

            File[] files = source.listFiles();
            if (files != null) {
                for (File file : files) {
                    File newTarget = new File(target, file.getName());
                    copyFilesRecursive(file, newTarget, extensions);
                }
            }
        } else {
            // 检查文件扩展名
            String fileName = source.getName();
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                String extension = fileName.substring(dotIndex + 1).toLowerCase();
                for (String ext : extensions) {
                    if (ext.equalsIgnoreCase(extension)) {
                        // 创建目标目录
                        target.getParentFile().mkdirs();
                        // 复制并转换编码
                        copyFileWithEncodingConversion(source, target);
                        break;
                    }
                }
            }
        }
    }
}