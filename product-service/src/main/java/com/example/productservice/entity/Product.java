package com.example.productservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品实体类
 * 对应数据库中的products表
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "商品名称不能为空")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "商品描述不能为空")
    @Column(columnDefinition = "TEXT")  // 长文本描述
    private String description;

    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.0", message = "商品价格不能小于0")
    @Column(nullable = false, precision = 10, scale = 2)  // 10位精度，2位小数
    private BigDecimal price;

    @NotNull(message = "库存数量不能为空")
    @Column(nullable = false)
    private Integer stock;  // 库存数量

    @Column(name = "category_id")
    private Long categoryId;  // 分类ID

    @Column(name = "category_name", length = 50)
    private String categoryName;  // 分类名称

    @Column(name = "image_url")
    private String imageUrl;  // 商品图片URL

    @Column(name = "status", nullable = false)
    private Integer status = 1;  // 商品状态：1-上架, 0-下架

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}