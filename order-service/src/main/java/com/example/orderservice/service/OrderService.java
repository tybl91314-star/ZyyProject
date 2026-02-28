package com.example.orderservice.service;

import com.example.common.dto.OrderDTO;
import com.example.common.response.Result;
import com.example.orderservice.entity.Order;
import java.util.List;

/**
 * 订单服务接口
 * 定义订单相关的业务操作
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param order 订单信息
     * @return 创建结果
     */
    Result createOrder(Order order);

    /**
     * 根据ID获取订单
     *
     * @param id 订单ID
     * @return 订单信息
     */
    Result<OrderDTO> getOrderById(Long id);

    /**
     * 根据订单号获取订单
     *
     * @param orderNumber 订单号
     * @return 订单信息
     */
    Result<OrderDTO> getOrderByNumber(String orderNumber);

    /**
     * 根据用户ID获取订单列表
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    Result<List<OrderDTO>> getOrdersByUserId(Long userId);

    /**
     * 获取所有订单
     *
     * @return 订单列表
     */
    Result<List<OrderDTO>> getAllOrders();

    /**
     * 支付订单
     *
     * @param id 订单ID
     * @return 支付结果
     */
    Result<OrderDTO> payOrder(Long id);

    /**
     * 取消订单
     *
     * @param id 订单ID
     * @取消结果
     */
    Result<OrderDTO> cancelOrder(Long id);

    /**
     * 更新订单状态
     *
     * @param id 订单ID
     * @param status 状态码
     * @return 更新结果
     */
    Result<OrderDTO> updateOrderStatus(Long id, Integer status);

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return 删除结果
     */
    Result<Void> deleteOrder(Long id);
}