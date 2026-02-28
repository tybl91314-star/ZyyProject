package com.example.common.constant;

/**
 * 通用常量
 */
public interface CommonConstants {
    /**
     * 请求头常量
     */
    String HEADER_AUTHORIZATION = "Authorization";
    String HEADER_USER_ID = "User-Id";
    String HEADER_TRACE_ID = "Trace-Id";

    /**
     * 日期时间格式
     */
    String DATE_FORMAT = "yyyy-MM-dd";
    String TIME_FORMAT = "HH:mm:ss";
    String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 分页常量
     */
    Integer DEFAULT_PAGE_SIZE = 10;
    Integer DEFAULT_PAGE_NUM = 1;
}