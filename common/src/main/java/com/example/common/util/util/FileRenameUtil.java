package com.example.common.util.util;

import java.io.File;
import java.util.Scanner;

public class FileRenameUtil
{
    /**
     * 将指定目录下excel文件名称批量替换
     */
    public static void fileRename() {

        System.out.println("-------将指定目录下excel文件名称批量替换-------");

        Scanner scanner = new Scanner(System.in);

        // 获取用户输入的源目录和目标目录
        System.out.print("请输入文件目录: ");
        String directoryPath = scanner.nextLine().trim();

        System.out.print("请输入要被替换的字符串: ");
        String toReplace = scanner.nextLine().trim();

        System.out.print("请输入要要替换的字符串: ");
        String replaceWith = scanner.nextLine().trim();

        scanner.close();

        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx"))) {
                        String oldFileName = file.getName();
                        String newFileName = oldFileName.replace(toReplace, replaceWith);
                        File newFile = new File(directoryPath + File.separator + newFileName);
                        if (file.renameTo(newFile)) {
                            System.out.println("Renamed " + oldFileName + " to " + newFileName);
                        } else {
                            System.out.println("Failed to rename " + oldFileName);
                        }
                    }
                }
            }
        } else {
            System.out.println("The specified directory does not exist or is not a directory.");
        }
    }
}
