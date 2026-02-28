package com.example.common.util.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private static final int COLUMN_WIDTH_FACTOR = 256;
    private static final int WIDTH_ADJUSTMENT = 512;
    private static final int MAX_COLUMN_WIDTH = 5120; //65280;
    private static final int ASCII_WIDTH = 1; // 单字节字符宽度
    private static final int NON_ASCII_WIDTH = 2; // 多字节字符宽度（比如中文）

    public static void createExcelFromJson(String jsonList, String excelPath) {
        // 将JSON字符串解析为JSONArray
        JSONArray jsonArray = JSONUtil.parseArray(jsonList);

        try {
            // 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");

            // 处理表头行（即JSON的key作为列标题）
            if (CollUtil.isNotEmpty(jsonArray)) {
                JSONObject firstJsonObject = jsonArray.getJSONObject(0);
                int columnIndex = 0;
                Row headerRow = sheet.createRow(0);
                for (String key : firstJsonObject.keySet()) {
                    Cell cell = headerRow.createCell(columnIndex);
                    cell.setCellValue(key);
                    columnIndex++;
                }
            }

            // 用于记录每列的最大长度
            List<Integer> maxLengthsPerColumn = new ArrayList<>();
            for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
                maxLengthsPerColumn.add(0);
            }

            // 填充数据行并同时记录每列最大长度
            for (int rowIndex = 0; rowIndex < jsonArray.size(); rowIndex++) {
                JSONObject jsonObject = jsonArray.getJSONObject(rowIndex);
                Row row = sheet.createRow(rowIndex + 1);
                int columnIndex = 0;
                for (String key : jsonObject.keySet()) {
                    Cell cell = row.createCell(columnIndex);
                    Object value = jsonObject.get(key);
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }

                    // 区分字符类型计算长度并更新该列最大长度
                    int length = 0;
                    if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getStringCellValue();
                        for (int i = 0; i < cellValue.length(); i++) {
                            char c = cellValue.charAt(i);
                            if (c <= 0x7F) {
                                length += ASCII_WIDTH;
                            } else {
                                length += NON_ASCII_WIDTH;
                            }
                        }
                    } else {
                        String cellValue = cell.toString();
                        for (int i = 0; i < cellValue.length(); i++) {
                            char c = cellValue.charAt(i);
                            if (c <= 0x7F) {
                                length += ASCII_WIDTH;
                            } else {
                                length += NON_ASCII_WIDTH;
                            }
                        }
                    }
                    maxLengthsPerColumn.set(columnIndex, Math.max(maxLengthsPerColumn.get(columnIndex), length));

                    columnIndex++;
                }
            }

            // 根据每列实际最大长度调整列宽
            for (int columnIndex = 0; columnIndex < maxLengthsPerColumn.size(); columnIndex++) {
                int width = (maxLengthsPerColumn.get(columnIndex) * COLUMN_WIDTH_FACTOR) + WIDTH_ADJUSTMENT;
                width = Math.min(width, MAX_COLUMN_WIDTH);
                sheet.setColumnWidth(columnIndex, width);
            }

            // 输出Excel文件
            FileOutputStream fileOut = new FileOutputStream(excelPath);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            System.out.println("Excel文件已成功生成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createExcelFromJson(JSONArray jsonArray, String excelPath) {


        try {
            // 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");

            // 处理表头行（即JSON的key作为列标题）
            if (CollUtil.isNotEmpty(jsonArray)) {
                JSONObject firstJsonObject = jsonArray.getJSONObject(0);
                int columnIndex = 0;
                Row headerRow = sheet.createRow(0);
                for (String key : firstJsonObject.keySet()) {
                    Cell cell = headerRow.createCell(columnIndex);
                    cell.setCellValue(key);
                    columnIndex++;
                }
            }

            // 用于记录每列的最大长度
            List<Integer> maxLengthsPerColumn = new ArrayList<>();
            for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
                maxLengthsPerColumn.add(0);
            }

            // 填充数据行并同时记录每列最大长度
            for (int rowIndex = 0; rowIndex < jsonArray.size(); rowIndex++) {
                JSONObject jsonObject = jsonArray.getJSONObject(rowIndex);
                Row row = sheet.createRow(rowIndex + 1);
                int columnIndex = 0;
                for (String key : jsonObject.keySet()) {
                    Cell cell = row.createCell(columnIndex);
                    Object value = jsonObject.get(key);
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }

                    // 区分字符类型计算长度并更新该列最大长度
                    int length = 0;
                    if (cell.getCellType() == CellType.STRING) {
                        String cellValue = cell.getStringCellValue();
                        for (int i = 0; i < cellValue.length(); i++) {
                            char c = cellValue.charAt(i);
                            if (c <= 0x7F) {
                                length += ASCII_WIDTH;
                            } else {
                                length += NON_ASCII_WIDTH;
                            }
                        }
                    } else {
                        String cellValue = cell.toString();
                        for (int i = 0; i < cellValue.length(); i++) {
                            char c = cellValue.charAt(i);
                            if (c <= 0x7F) {
                                length += ASCII_WIDTH;
                            } else {
                                length += NON_ASCII_WIDTH;
                            }
                        }
                    }
                    maxLengthsPerColumn.set(columnIndex, Math.max(maxLengthsPerColumn.get(columnIndex), length));

                    columnIndex++;
                }
            }

            // 根据每列实际最大长度调整列宽
            for (int columnIndex = 0; columnIndex < maxLengthsPerColumn.size(); columnIndex++) {
                int width = (maxLengthsPerColumn.get(columnIndex) * COLUMN_WIDTH_FACTOR) + WIDTH_ADJUSTMENT;
                width = Math.min(width, MAX_COLUMN_WIDTH);
                sheet.setColumnWidth(columnIndex, width);
            }

            // 输出Excel文件
            FileOutputStream fileOut = new FileOutputStream(excelPath);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            System.out.println("Excel文件已成功生成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
