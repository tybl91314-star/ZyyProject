package com.example.common.util.base;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;


public class TxtUtil {


    public static void writeTxt(String content, String filePath) {

        //Charset charset = Charset.forName("UTF-8"); // 指定编码格式，这里以UTF-8为例，可按需更换
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write(content);
            writer.newLine(); // 如果需要换行，可添加这一行
            System.out.println("字符串已成功写入到文件中");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readTxt( String filePath) {
        try {
            String longString = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            return longString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
