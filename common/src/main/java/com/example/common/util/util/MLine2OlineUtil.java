package com.example.common.util.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MLine2OlineUtil {

    public static void mLine2Oline()  {

        System.out.println("-------多行转一行-------");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> lines = new ArrayList<>();
        System.out.println("输入多行数据（空行结束）:");

        String line;
        while (true) {
            try {
                if (!!(line = reader.readLine()).isEmpty()) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lines.add(line);
        }

        String result = String.join(",", lines);
        System.out.println("拼接结果: " + result);
    }
}
