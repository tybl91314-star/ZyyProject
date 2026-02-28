package com.example.orderservice.service.impl;

import com.example.common.dto.OrderDTO;
import com.example.common.dto.OrderItemDTO;
import com.example.common.dto.ProductDTO;
import com.example.common.dto.UserDTO;
import com.example.common.response.Result;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.feign.ProductServiceClient;
import com.example.orderservice.feign.UserServiceClient;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 * 实现订单相关的业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserServiceClient userServiceClient;
    private final ProductServiceClient productServiceClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderDTO> createOrder(Order order) {
        try {
            log.info("开始创建订单，用户ID: {}", order.getUserId());

            // 1. 验证用户是否存在
            Result<UserDTO> userResult = userServiceClient.getUserById(order.getUserId());
            if (!userResult.getCode().equals(200)) {
                return Result.error("用户不存在或用户服务不可用");
            }

            // 2. 验证订单项
            if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
                return Result.error("订单项不能为空");
            }

            // 3. 验证商品和计算总金额
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderItem item : order.getOrderItems()) {
                // 验证商品是否存在
                Result<ProductDTO> productResult = productServiceClient.getProductById(item.getProductId());
                if (!productResult.getCode().equals(200)) {
                    return Result.error("商品不存在: " + item.getProductId());
                }

                ProductDTO product = productResult.getData();

                // 验证库存
                if (product.getStock() < item.getQuantity()) {
                    return Result.error("商品库存不足: " + product.getName());
                }

                // 设置商品信息
                item.setProductName(product.getName());
                item.setProductImage(product.getImageUrl());
                item.setPrice(product.getPrice());

                // 计算商品总价
                BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                item.setTotalPrice(itemTotal);
                totalAmount = totalAmount.add(itemTotal);
            }

            // 4. 设置订单信息
            order.setTotalAmount(totalAmount);
            order.setStatus(0); // 待支付

            // 5. 保存订单
            Order savedOrder = orderRepository.save(order);

            // 6. 保存订单项
            for (OrderItem item : order.getOrderItems()) {
                item.setOrderId(savedOrder.getId());
                orderItemRepository.save(item);
            }

            // 7. 转换为DTO返回
            OrderDTO orderDTO = convertToDTO(savedOrder);

            log.info("订单创建成功，订单号: {}, 总金额: {}", savedOrder.getOrderNumber(), totalAmount);
            return Result.success("订单创建成功", orderDTO);

        } catch (Exception e) {
            // 手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("订单创建失败: {}", e.getMessage(), e);
            return Result.error("订单创建失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderDTO> payOrder(Long id) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (!orderOpt.isPresent()) {
                return Result.error("订单不存在");
            }

            Order order = orderOpt.get();

            // 检查订单状态
            if (order.getStatus() != 0) {
                return Result.error("订单状态不正确，无法支付");
            }

            // 扣减库存
            List<OrderItem> items = orderItemRepository.findByOrderId(id);
            for (OrderItem item : items) {
                Result<ProductDTO> stockResult = productServiceClient.deductStock(
                        item.getProductId(), item.getQuantity()
                );
                if (!stockResult.getCode().equals(200)) {
                    return Result.error("商品库存不足: " + item.getProductName());
                }
            }

            // 更新订单状态
            order.setStatus(1); // 已支付
            order.setPaymentStatus(1);
            order.setPaidAt(new java.util.Date());

            Order updatedOrder = orderRepository.save(order);
            OrderDTO orderDTO = convertToDTO(updatedOrder);

            log.info("订单支付成功，订单号: {}, 订单ID: {}", order.getOrderNumber(), id);
            return Result.success("订单支付成功", orderDTO);

        } catch (Exception e) {
            log.error("订单支付失败: {}", e.getMessage(), e);
            return Result.error("订单支付失败: " + e.getMessage());
        }
    }

    @Override
    public Result<OrderDTO> getOrderById(Long id) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (!orderOpt.isPresent()) {
                return Result.error("订单不存在");
            }

            Order order = orderOpt.get();
            OrderDTO orderDTO = convertToDTO(order);

            return Result.success(orderDTO);
        } catch (Exception e) {
            log.error("获取订单失败: {}", e.getMessage(), e);
            return Result.error("获取订单失败");
        }
    }

    @Override
    public Result<OrderDTO> getOrderByNumber(String orderNumber) {
        try {
            Optional<Order> orderOpt = orderRepository.findByOrderNumber(orderNumber);
            if (!orderOpt.isPresent()) {
                return Result.error("订单不存在");
            }

            Order order = orderOpt.get();
            OrderDTO orderDTO = convertToDTO(order);

            return Result.success(orderDTO);
        } catch (Exception e) {
            log.error("获取订单失败: {}", e.getMessage(), e);
            return Result.error("获取订单失败");
        }
    }

    @Override
    public Result<List<OrderDTO>> getOrdersByUserId(Long userId) {
        try {
            List<Order> orders = orderRepository.findByUserId(userId);
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return Result.success(orderDTOs);
        } catch (Exception e) {
            log.error("获取用户订单列表失败: {}", e.getMessage(), e);
            return Result.error("获取订单列表失败");
        }
    }

    @Override
    public Result<List<OrderDTO>> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return Result.success(orderDTOs);
        } catch (Exception e) {
            log.error("获取所有订单失败: {}", e.getMessage(), e);
            return Result.error("获取订单列表失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderDTO> cancelOrder(Long id) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (!orderOpt.isPresent()) {
                return Result.error("订单不存在");
            }

            Order order = orderOpt.get();

            // 检查订单状态
            if (order.getStatus() != 0) {
                return Result.error("订单状态不正确，无法取消");
            }

            // 更新订单状态
            order.setStatus(4); // 已取消
            Order updatedOrder = orderRepository.save(order);
            OrderDTO orderDTO = convertToDTO(updatedOrder);

            log.info("订单取消成功，订单号: {}", order.getOrderNumber());
            return Result.success("订单取消成功", orderDTO);

        } catch (Exception e) {
            log.error("取消订单失败: {}", e.getMessage(), e);
            return Result.error("取消订单失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderDTO> updateOrderStatus(Long id, Integer status) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (!orderOpt.isPresent()) {
                return Result.error("订单不存在");
            }

            Order order = orderOpt.get();
            order.setStatus(status);

            Order updatedOrder = orderRepository.save(order);
            OrderDTO orderDTO = convertToDTO(updatedOrder);

            return Result.success("订单状态更新成功", orderDTO);

        } catch (Exception e) {
            log.error("更新订单状态失败: {}", e.getMessage(), e);
            return Result.error("更新订单状态失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteOrder(Long id) {
        try {
            if (!orderRepository.existsById(id)) {
                return Result.error("订单不存在");
            }

            // 先删除订单项
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(id);
            orderItemRepository.deleteAll(orderItems);

            // 再删除订单
            orderRepository.deleteById(id);

            log.info("订单删除成功，订单ID: {}", id);
            return Result.success();

        } catch (Exception e) {
            log.error("删除订单失败: {}", e.getMessage(), e);
            return Result.error("删除订单失败");
        }
    }

    /**
     * 将Order实体转换为OrderDTO
     */
    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);

        // 转换订单项
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        List<OrderItemDTO> itemDTOs = items.stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            BeanUtils.copyProperties(item, itemDTO);
            return itemDTO;
        }).collect(Collectors.toList());

        orderDTO.setOrderItems(itemDTOs);
        return orderDTO;
    }
}