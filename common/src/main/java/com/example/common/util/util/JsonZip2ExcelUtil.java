package com.example.common.util.util;

import com.example.common.util.base.CompressUtil;
import com.example.common.util.base.ExcelUtil;
import com.example.common.util.base.KmpUtil;
import com.example.common.util.base.TxtUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class JsonZip2ExcelUtil {
    public static void jsonZip2File() {
        System.out.println("-------将指定的json压缩文件.txt转成excel或json.txt-------");

        Scanner scanner = new Scanner(System.in);

        // 获取用户输入的源目录和目标目录
        System.out.print("请输入txt文件全路径（包含文件名）: ");
        String jsonZipPath = scanner.nextLine().trim();


        scanner.close();

        Path filePath = Paths.get(jsonZipPath).getParent();
        String fileNameWithExt  = Paths.get(jsonZipPath).getFileName().toString();
        String fileName = fileNameWithExt.substring(0, fileNameWithExt.lastIndexOf("."));

        String txtString = TxtUtil.readTxt(jsonZipPath);
        String longString = "";
        try {
            longString =  CompressUtil.decompress(txtString);
            longString = longString.replace("\r\n", "\\\\n")  // 处理 Windows 换行
                    .replace("\n", "\\\\n")  // 处理 Unix 换行
                    //  .replaceAll("\\\\\\\\", "\\\\")
                    .replaceAll("\\\\([^\"\\\\/bfnrtu])", "$1");  //// 匹配无效转义符：\ 后跟非合法JSON转义字符（如 $$ $$ \*）
//                   .replaceAll("\\\\([^\"\\\\/bfnrtu]|\\\\\\\$", "$1");

            int count = KmpUtil.countOccurrences(longString, "rn");
            System.out.println("数量2："+count);

//            System.out.println(longString.substring(195,300));

            Path excelFilePath = filePath.resolve(fileName+".xlsx");
            ExcelUtil.createExcelFromJson(longString, excelFilePath.toString());

        } catch (Exception e) {

            Path txtFilePath = filePath.resolve(fileName+" - json.txt");
            TxtUtil.writeTxt(longString, txtFilePath.toString());
            throw new RuntimeException(e);
        }


    }
}
