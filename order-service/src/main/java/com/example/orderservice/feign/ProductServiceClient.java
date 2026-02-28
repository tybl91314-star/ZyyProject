package com.example.orderservice.feign;

import com.example.common.dto.ProductDTO;
import com.example.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品服务Feign客户端
 * 用于调用product-service的商品相关接口
 *
 *ignClient参数说明：
 * - name: 服务名称，对应Nacos中的服务名
 * - path: 统一路径前缀
 * - fallback: 服务降级处理类
 */
@FeignClient(
        name = "product-service",
        path = "/api/products",
        fallback = ProductServiceClientFallback.class
)
public interface ProductServiceClient {

    /**
     * 根据商品ID获取商品信息
     *
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/{id}")
    Result<ProductDTO> getProductById(@PathVariable("id") Long id);

    /**
    *检查商品是否存在
     *
     * @param name 商品名称
     * @return 是否存在
     */
    @GetMapping("/check-exists")
    Result<Boolean> checkProductExists(@RequestParam("name") String name);

    /**
     * 扣减商品库存
     *
     * @param productId 商品ID
     * @param quantity 扣减数量
     * @return 操作结果
     */
    @PutMapping("/{id}/deduct-stock")
    Result<ProductDTO> deductStock(
            @PathVariable("id") Long productId,
            @RequestParam("quantity") Integer quantity
    );

    /**
     * 恢复商品库存
     *
     * @Id 商品ID
     * @param quantity 恢复数量
     * @return 操作结果
     */
    @PutMapping("/{id}/restore-stock")
    Result<ProductDTO> restoreStock(
            @PathVariable("id") Long productId,
            @RequestParam("quantity") Integer quantity
    );
}