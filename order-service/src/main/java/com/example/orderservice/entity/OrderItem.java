package com.example.orderservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单项实体类
 * 对应数据库中的order_items表
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "订单ID不能为空")
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @NotNull(message = "商品ID不能为空")
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "product_image")
    private String productImage;

    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.0", message = "商品价格不能小于0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "商品数量不能为空")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "商品总价不能为空")
    @DecimalMin(value = "0.0", message = "商品总价不能小于0")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    // 关联订单
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @PrePersist
    protected void onPrePersist() {
        // 计算总价
        if (price != null && quantity != null) {
            totalPrice = price.multiply(BigDecimal.valueOf(quantity));
        }
    }
}