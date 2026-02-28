package com.example.common.util;

import cn.hutool.json.JSONUtil;
import com.example.common.util.base.ExcelUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        int[] mxsz = {0,1};  //有无冥想
        int[] sssz = {0,1};  //有无四圣
        int[] pgsz = {0,1};  //普攻次数
        int[] adsz = {0,1,2,3,4,5,6,7}; //挨打次数
        List list = new ArrayList<>();
        for (int mx = 0; mx < mxsz.length; mx++) {
            for (int ss = 0; ss < sssz.length; ss++) {
                for (int pg = 0; pg < pgsz.length; pg++) {
                    for (int ad = 0; ad < adsz.length; ad++) {
                        if(pg>0||ad>0){
                            // 1. 创建 BigDecimal 对象（推荐使用字符串构造，避免直接传double的精度问题）
                            BigDecimal dividend = new BigDecimal(1);
                            BigDecimal divisor = new BigDecimal(pg*0.5 + ad*0.13);
                            if(divisor.compareTo(new BigDecimal(1))>=1){
                                Map<String, Object> map = new HashMap<>();
                                map.put("冥想", mx); //冥想
                                map.put("四圣", ss);  //四圣
                                map.put("普攻次数", pg); //普攻次数
                                map.put("挨打次数", ad); //挨打次数
                                map.put("怒速", 0); //怒速

                                BigDecimal pgB = BigDecimal.valueOf(pg).multiply(BigDecimal.valueOf(0.5));
                                BigDecimal adB = BigDecimal.valueOf(ad).multiply(BigDecimal.valueOf(0.13));
                                BigDecimal mxB = BigDecimal.valueOf(mx).multiply(BigDecimal.valueOf(0.25));
                                BigDecimal ssB = BigDecimal.valueOf(ss).multiply(BigDecimal.valueOf(0.2));

                                BigDecimal nq1 = pgB.add(adB);
                                BigDecimal ns1 = mxB.add(ssB).add(BigDecimal.valueOf(1));

                                map.put("总怒气", nq1.multiply(ns1));

                                list.add(map);
                                continue;
                            }

                            // 2. 进行除法运算，设置精度为2位小数，舍入模式为四舍五入
                            BigDecimal zns = dividend.divide(divisor, 2, RoundingMode.HALF_UP);
                            BigDecimal ns = zns.subtract(new BigDecimal(1 + mx*0.25+ss*0.2)).setScale(2, RoundingMode.HALF_UP);;

                            if(ns.compareTo(new BigDecimal(0.2))<=0 ){
                                Map<String, Object> map = new HashMap<>();
                                map.put("冥想", mx); //冥想
                                map.put("四圣", ss);  //四圣
                                map.put("普攻次数", pg); //普攻次数
                                map.put("挨打次数", ad); //挨打次数
                                map.put("怒速", ns); //怒速

                                BigDecimal pgB = BigDecimal.valueOf(pg).multiply(BigDecimal.valueOf(0.5));
                                BigDecimal adB = BigDecimal.valueOf(ad).multiply(BigDecimal.valueOf(0.13));
                                BigDecimal mxB = BigDecimal.valueOf(mx).multiply(BigDecimal.valueOf(0.25));
                                BigDecimal ssB = BigDecimal.valueOf(ss).multiply(BigDecimal.valueOf(0.2));

                                BigDecimal nq1 = pgB.add(adB);
                                BigDecimal ns1 = mxB.add(ssB).add(BigDecimal.valueOf(1)).add(ns);

                                map.put("总怒气", nq1.multiply(ns1));

                                list.add(map);

                            }

                        }

                    }
                }
            }
        }

        ExcelUtil.createExcelFromJson(JSONUtil.parseArray(list), "D:\\test.xlsx");
    }
}
