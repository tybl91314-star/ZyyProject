package com.example.orderservice.controller;

import com.example.common.dto.OrderDTO;
import com.example.common.response.Result;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * 订单控制器
 * 提供订单相关的REST API接口
 *
 * @RestController: 标识为REST控制器
 * @RequestMapping: 统一API路径前缀
 * @RequiredArgsConstructor: Lombok注解，自动生成构造函数
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 创建订单
     *
     * @param order 订单信息
     * @return 创建结果
     *
     * @PostMapping: 处理HTTP POST请求
     * @Valid: 启用参数校验
     * @RequestBody: 从请求体中获取数据
     */
    @PostMapping
    public Result<OrderDTO> createOrder(@Valid @RequestBody Order order) {
        log.info("收到创建订单请求，用户ID: {}", order.getUserId());
        return orderService.createOrder(order);
    }

    /**
     * 根据ID获取订单
     *
     * @param id 订单ID
     * @return 订单信息
     *
     * @GetMapping: 处理HTTP GET请求
     * @PathVariable: 从URL路径中获取参数
     */
    @GetMapping("/{id}")
    public Result<OrderDTO> getOrderById(@PathVariable Long id) {
        log.info("查询订单详情，订单ID: {}", id);
        return orderService.getOrderById(id);
    }

    /**
     * 根据订单号获取订单
     *
     * @param orderNumber 订单号
     * @return 订单信息
     */
    @GetMapping("/number/{orderNumber}")
    public Result<OrderDTO> getOrderByNumber(@PathVariable String orderNumber) {
        log.info("查询订单详情，订单号: {}", orderNumber);
        return orderService.getOrderByNumber(orderNumber);
    }

    /**
     * 根据用户ID获取订单列表
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        log.info("查询用户订单列表，用户ID: {}", userId);
        return orderService.getOrdersByUserId(userId);
    }

    /**
     * 获取所有订单
     *
     * @return 订单列表
     */
    @GetMapping
    public Result<List<OrderDTO>> getAllOrders() {
        log.info("查询所有订单列表");
        return orderService.getAllOrders();
    }

    /**
     * 支付订单
     *
     * @param id 订单ID
     * @return 支付结果
     *
     * @PutMapping: 处理HTTP PUT请求
     */
    @PutMapping("/{id}/pay")
    public Result<OrderDTO> payOrder(@PathVariable Long id) {
        log.info("支付订单，订单ID: {}", id);
        return orderService.payOrder(id);
    }

    /**
     * 取消订单
     *
     * @param id 订单ID
     * @return 取消结果
     */
    @PutMapping("/{id}/cancel")
    public Result<OrderDTO> cancelOrder(@PathVariable Long id) {
        log.info("取消订单，订单ID: {}", id);
        return orderService.cancelOrder(id);
    }

    /**
     * 更新订单状态
     *
     * @param id 订单ID
     * @param status 状态码
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public Result<OrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Integer status
    ) {
        log.info("更新订单状态，订单ID: {}, 新状态: {}", id, status);
        return orderService.updateOrderStatus(id, status);
    }

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return 删除结果
     *
     * @DeleteMapping: 处理HTTP DELETE请求
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id){
        log.info("删除订单，订单ID: {}", id);
        return orderService.deleteOrder(id);
    }
}