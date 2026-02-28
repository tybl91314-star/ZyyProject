package com.example.common.model;

import lombok.Data;
import com.example.common.constant.CommonConstants;

/**
 * 分页请求对象
 */
@Data
public class PageRequest {
    /**
     * 当前页码
     */
    private Integer pageNum = CommonConstants.DEFAULT_PAGE_NUM;

    /**
     * 每页数量
     */
    private Integer pageSize = CommonConstants.DEFAULT_PAGE_SIZE;

    /**
     * 计算偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}