package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 订单数据访问接口
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据订单号查找订单
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * 根据用户ID查找订单
     */
    List<Order> findByUserId(Long userId);

    /**
     * 根据状态查找订单
     */
    List<Order> findByStatus(Integer status);

    /**
     * 根据用户ID和状态查找订单
     */
    List<Order> findByUserIdAndStatus(Long userId, Integer status);

    /**
     *数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);

    /**
     * 查找最近创建的订单
     */
    List<Order> findTop10ByOrderByCreatedAtDesc();
}