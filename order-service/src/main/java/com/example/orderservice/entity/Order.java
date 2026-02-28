package com.example.orderservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单实体类
 * 对应数据库中的orders表
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_number", unique = true, nullable = false, length = 32)
    private String orderNumber;  // 订单号

    @NotNull(message = "订单总金额不能为空")
    @DecimalMin(value = "0.0", message = "订单金额不能小于0")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "status", nullable = false)
    private Integer status = 0;  // 订单状态：0-待支付, 1-已支付, 2-已发货, 3-已完成, 4-已取消

    @Column(name = "payment_status", nullable = false)
    private Integer paymentStatus = 0;  // 支付状态：0-未支付, 1-已支付

    @Column(name = "shipping_address", length = 500)
    private String shippingAddress;  // 收货地址

    @Column(name = "receiver_name", length = 50)
    private String receiverName;  // 收货人姓名

    @Column(name = "receiver_phone", length = 20)
    private String receiverPhone;  // 收货人电话

    @Column(name = "remark", length = 500)
    private String remark;  // 订单备注

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "paid_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidAt;  // 支付时间

    // 订单项列表（一对多关系）
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
        // 生成订单号
        if (orderNumber == null) {
            orderNumber = "ORD" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}