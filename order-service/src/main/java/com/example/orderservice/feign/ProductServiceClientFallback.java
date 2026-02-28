package com.example.orderservice.feign;

import com.example.common.dto.ProductDTO;
import com.example.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 商品服务Feign客户端降级处理
 * 当product-service不可用时提供默认响应
 */
@Slf4j
@Component
public class ProductServiceClientFallback implements ProductServiceClient {

    @Override
    public Result<ProductDTO> getProductById(Long id) {
        log.warn("商品服务不可用，getProductById降级处理，商品ID: {}", id);
        return Result.error(503, "商品服务暂时不可用");
    }

    @Override
    public Result<Boolean> checkProductExists(String name) {
        log.warn("商品服务不可用，checkProductExists降级处理，商品名称: {}", name);
        return Result.error(503, "商品服务暂时不可用");
    }

    @Override
    public Result<ProductDTO> deductStock(Long productId, Integer quantity) {
        log.warn("商品服务不可用，deductStock降级处理，商品ID: {}，数量: {}", productId, quantity);
        return Result.error(503, "商品服务暂时不可用");
    }

    @Override
    public Result<ProductDTO> restoreStock(Long productId, Integer quantity) {
        log.warn("商品服务不可用，restoreStock降级处理，商品ID: {}，数量: {}", productId, quantity);
        return Result.error(503, "商品服务暂时不可用");
    }
}