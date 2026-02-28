package com.example.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单数据传输对象
 * 用于服务间通信和API响应
 */
@Data
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private Long userId;
    private BigDecimal totalAmount;
    private Integer status;
    private Integer paymentStatus;
    private String shippingAddress;
    private String receiverName;
    private String receiverPhone;
    private String remark;
    private Date createdAt;
    private Date updatedAt;
    private Date paidAt;
    private List<OrderItemDTO> orderItems;

    // 状态描述
    public String getStatusText() {
        switch (status) {
            case 0: return "未支付";
            case 1: return "已支付";
            case 2: return "已发货";
            case 3: return "已完成";
            case 4: return "已取消";
            default: return "未知状态";
        }
    }
}