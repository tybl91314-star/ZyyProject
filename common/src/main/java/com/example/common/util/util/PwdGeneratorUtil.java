package com.example.common.util.util;

import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.Scanner;

public class PwdGeneratorUtil {


    public static void generatePassword() {

        System.out.println("-------密码生成-------");

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入密码长度（为空默认16位长度）: ");
        String lengthin = scanner.nextLine().trim();
        int length = 16;
        if(StringUtils.hasText(lengthin)){
            length = Integer.parseInt(lengthin);
        }

        scanner.close();

        // 定义字符集，包含大小写字母、数字
       // String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        // 去除易混淆字符，如数字0和字母O，数字1和字母I、l
        String charset = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 生成一个在字符集范围内的随机索引
            int randomIndex = random.nextInt(charset.length());
            // 将随机字符添加到密码中
            password.append(charset.charAt(randomIndex));
        }

        System.out.println(password);
    }
}