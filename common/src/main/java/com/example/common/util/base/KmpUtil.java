package com.example.common.util.base;

public class KmpUtil {

    public static int[] getNext(String pattern) {
        int m = pattern.length();
        int[] next = new int[m];
        next[0] = 0;
        int i = 1;
        int j = 0;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
                next[i] = j;
                i++;
            } else {
                if (j!= 0) {
                    j = next[j - 1];
                } else {
                    next[i] = 0;
                    i++;
                }
            }
        }
        return next;
    }

    // 使用KMP算法统计出现次数
    public static int countOccurrences(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        int[] next = getNext(pattern);
        int count = 0;
        int i = 0;
        int j = 0;
        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    count++;
                    j = next[j - 1];
                }
            } else {
                if (j!= 0) {
                    j = next[j - 1];
                } else {
                    i++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        String text = "ABABABABCABABABABCABAB";
        String pattern = "ABAB";
        int result = countOccurrences(text, pattern);
        System.out.println("模式字符串在主字符串中出现的次数为: " + result);
    }

}
