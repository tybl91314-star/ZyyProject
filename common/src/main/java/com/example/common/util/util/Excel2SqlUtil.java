package com.example.common.util.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Excel2SqlUtil {

    /**
     * 将excel转成sql的insert语句
     */
    public static void excel2Sql() {

        System.out.println("-------将excel转成sql的insert语句-------");
        System.out.println("请将Excel数据粘贴到此处（第一行为标题行，空格分隔列）：");
        System.out.println("粘贴完成后，在新的一行输入空行结束");

        Scanner scanner = new Scanner(System.in);

        List<String[]> dataRows = new ArrayList<>();
        String line;

        // 读取控制台输入
        while (!(line = scanner.nextLine()).isEmpty()) {
            // 按空格分割行数据（假设Excel列以空格分隔）
            String[] columns = line.split("\\s+");
            dataRows.add(columns);
        }

        if (dataRows.isEmpty()) {
            System.out.println("未输入任何数据！");
            return;
        }

        // 获取标题行（第一行）作为列名
        String[] headers = dataRows.get(0);
        int columnCount = headers.length;

        // 生成动态表名：ZYYTMP+当前年月日时分
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String tableName = "ZYYTMP_DR_" + sdf.format(new Date());

        // 1. 生成创建表的SQL语句
        StringBuilder createTableSql = new StringBuilder();
        createTableSql.append("CREATE TABLE ").append(tableName).append(" (");

        for (int i = 0; i < columnCount; i++) {
            // 使用标题行作为列名，列类型默认为VARCHAR(255)
            createTableSql.append(headers[i]).append(" VARCHAR(255)");
            if (i < columnCount - 1) {
                createTableSql.append(", ");
            }
        }
        createTableSql.append(");");

        System.out.println("\n生成的建表SQL语句:");
        System.out.println(createTableSql.toString());

        // 2. 生成插入数据的SQL语句（跳过标题行）
//        System.out.println("\n生成的插入数据SQL语句:");

        for (int i = 1; i < dataRows.size(); i++) {
            String[] row = dataRows.get(i);
            StringBuilder insertSql = new StringBuilder();

            insertSql.append("INSERT INTO ").append(tableName).append(" VALUES (");
            for (int j = 0; j < columnCount; j++) {
                if (j < row.length) {
                    // 添加单引号处理字符串值
                    insertSql.append("'").append(row[j]).append("'");
                } else {
                    insertSql.append("NULL");
                }
                if (j < columnCount - 1) {
                    insertSql.append(", ");
                }
            }
            insertSql.append(");");

            System.out.println(insertSql.toString());
        }

        System.out.println("\n共生成 " + (dataRows.size() - 1) + " 条插入SQL语句");
        scanner.close();
    }

    public static void excel2Sql2() {

        System.out.println("-------将excel转成sql的create table as select 语句-------");
        System.out.println("请将Excel数据粘贴到此处（第一行为标题行）：");
        System.out.println("粘贴完成后，在新的一行输入空行结束");


        Scanner scanner = new Scanner(System.in);

        // 1. 读取控制台输入
        List<String> inputLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                // 遇到空行，停止读取
                break;
            }
            inputLines.add(line);
        }
        scanner.close();

        if (inputLines.isEmpty()) {
            System.out.println("未检测到输入内容。");
            return;
        }

        // 2. 解析数据
        // 2.1 解析表头（第一行）
        String headerLine = inputLines.get(0);
        String[] columnNames = headerLine.split("\t"); // 假设列以制表符分隔

        // 2.2 构建SELECT语句中的字段列表 (字段1, 字段2, ...)
        StringBuilder columnList = new StringBuilder();
        for (String columnName : columnNames) {
            // 处理列名，例如去除首尾空格，如果有空格或关键字可用反引号包裹
            String processedName = columnName.trim();
            if (processedName.isEmpty() || processedName.contains(" ")) {
                processedName = "`" + processedName + "`"; // 使用反引号避免关键字或空格问题
            }
            columnList.append(processedName).append(", ");
        }
        // 删除最后多余的逗号和空格
        if (columnList.length() > 0) {
            columnList.setLength(columnList.length() - 2);
        }

        // 2.3 构建SELECT语句中的值部分
        StringBuilder sqlBuilder = new StringBuilder();
        String tableName = "tb_XXX"; // 请根据实际情况修改表名

        sqlBuilder.append("CREATE TABLE ").append(tableName).append(" AS \n");
        sqlBuilder.append("SELECT ");

        // 处理数据行（从第二行开始）
        for (int i = 1; i < inputLines.size(); i++) {
            String dataLine = inputLines.get(i);
            String[] values = dataLine.split("\t");

            if (i == 1) {
                // 第一条数据行，开始构建SELECT的每一列值
                for (int j = 0; j < values.length; j++) {
                    if (j > 0) {
                        sqlBuilder.append("        "); // 对齐
                    }
                    // 对每个值进行格式化 '值'
                    String value = (j < values.length) ? values[j].trim().replace("'", "''") : ""; // 处理单引号
                    sqlBuilder.append("'").append(value).append("'");
                    // 指定别名（即字段名）
                    String colName = (j < columnNames.length) ? columnNames[j].trim() : "col" + (j + 1);
                    if (colName.contains(" ")) {
                        colName = "`" + colName + "`";
                    }
                    sqlBuilder.append(" AS ").append(colName);
                    if (j < values.length - 1) {
                        sqlBuilder.append(", \n");
                    }
                }
            } else {
                // 后续数据行，使用 UNION ALL 连接
                sqlBuilder.append("\nUNION ALL\nSELECT ");
                for (int j = 0; j < values.length; j++) {
                    if (j > 0) {
                        sqlBuilder.append("        ");
                    }
                    String value = (j < values.length) ? values[j].trim().replace("'", "''") : "";
                    sqlBuilder.append("'").append(value).append("'");
                    if (j < values.length - 1) {
                        sqlBuilder.append(", \n");
                    }
                }
            }
        }
        sqlBuilder.append(";");

        // 3. 输出SQL
        System.out.println("\n生成的SQL语句：");
        System.out.println(sqlBuilder.toString());
    }
}