package com.example.orderservice.repository;

import com.example.orderservice.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 订单项数据访问接口
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * 根据订单ID查找订单项
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * 根据商品ID查找订单项
     */
    List<OrderItem> findByProductId(Long productId);

    /**
     * 删除指定订单的订单项
     */
    void deleteByOrderId(Long orderId);
}