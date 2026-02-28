package com.example.common.util;

import com.example.common.util.util.*;

import java.util.Scanner;

public class UtilMain {

    public static void main(String[] args) {
        System.out.println("1、密码生成 ");
        System.out.println("11、excel2sql(create) ");
        System.out.println("12、excel2sql(insert) ");
        System.out.println("13、field2json ");
        System.out.println("14、多行转一行 ");
        System.out.println("15、表头转COL列表 ");
        System.out.println("21、文件拷贝（拷贝sql文件和txt文件） ");
        System.out.println("22、文件内容检索（检索sql文件和txt文件） ");
        System.out.println("23、文件名批量替换 ");
        System.out.println("31、压缩文件生成excel ");
        System.out.println("32、csv拆分（每份最大30M） ");

    
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入需要调用的工具类编号: ");
        String utilNumb = scanner.nextLine().trim();
        System.out.println();

        if("1".equals(utilNumb)){
            PwdGeneratorUtil.generatePassword();
        }
        else if("11".equals(utilNumb)){
            Excel2SqlUtil.excel2Sql2();
        }
        else if("12".equals(utilNumb)){
            Excel2SqlUtil.excel2Sql();
        }
        else if("13".equals(utilNumb)){
            Field2JsonUtil.field2Json();
        }
        else if("14".equals(utilNumb)){
            MLine2OlineUtil.mLine2Oline();
        }
        else if("15".equals(utilNumb)){
            Head2ColUtil.head2Col();
        }
        else if("21".equals(utilNumb)){
            FileCopyUtil.fileCopy();
        }
        else if("22".equals(utilNumb)){
            FileSearchUtil.fileSearch();
        }
        else if("23".equals(utilNumb)){
            FileRenameUtil.fileRename();
        }
        else if("31".equals(utilNumb)){
            JsonZip2ExcelUtil.jsonZip2File();
        }
        else if("32".equals(utilNumb)){
            CsvSplitterUtil.csvSplitter();
        }


    }

}
