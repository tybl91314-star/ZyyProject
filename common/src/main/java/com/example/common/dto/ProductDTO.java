package com.example.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品数据传输对象
 * 用于服务间通信
 */
@Data
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String categoryName;
    private String imageUrl;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
}