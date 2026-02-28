package com.example.common.util.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Head2ColUtil
{
    public static void head2Col()
    {
        System.out.println("-------将输入的字段转成字段列表-------");

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入字段（用空格分隔）:");
        String input = scanner.nextLine();
        scanner.close();

        String[] fields = input.split("\\s+");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nyr = sdf.format(new Date());

        StringBuilder result = new StringBuilder();
        result.append("create table zyytmp").append(nyr).append(" as select ");

        for (int i = 0; i < fields.length; i++) {
            if (i > 0) result.append(", ");
            result.append("col").append(i + 1).append(" ").append(fields[i]);
        }

        result.append(" from zyytb_comm_dr where pch = '").append(nyr).append("01';");
        System.out.println("=====================结果=========================");
        System.out.println(result);
    }
}
