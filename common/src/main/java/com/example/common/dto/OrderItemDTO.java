package com.example.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * 订单项数据传输对象
 */
@Data
@NoArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
}