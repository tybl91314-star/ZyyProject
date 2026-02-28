package com.example.common.util.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Field2JsonUtil {



    public static void field2Json(){


        System.out.println("-------将输入的多行字段转成JSON-------");

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入字段名（每行一个，可带逗号，空行结束）:");

        List<String> fields = new ArrayList<>();

        // 读取多行输入直到空行
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) break;

            // 移除行末逗号并清理格式
            String field = line.replaceAll(",$", "").trim();
            if (!field.isEmpty()) {
                fields.add(field);
            }
        }

        scanner.close();

        // 处理空字段情况
        if (fields.isEmpty()) {
            System.out.println("concat('{}')");
            return;
        }

        // 构建参数列表
        List<String> params = new ArrayList<>();

        // 处理第一个字段
        String firstField = fields.get(0);
        params.add("'{\"" + firstField + "\":\"'");
        params.add("nvl(replace(" + firstField + ",'\"',''),'')");

        // 处理后续字段
        for (int i = 1; i < fields.size(); i++) {
            String field = fields.get(i);
            params.add("'\",\"" + field + "\":\"'");
            params.add("nvl(replace(" + field + ",'\"',''),'')");
        }

        // 添加JSON闭合标签
        params.add("'\"}'");

        // 生成最终结果
        System.out.println("concat(" + String.join(", ", params) + ")");

    }
}