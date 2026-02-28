package com.example.productservice.controller;

import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import com.example.common.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 商品控制器
 * 提供商品相关的REST API
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 创建商品
     */
    @PostMapping
    public Result<Product> createProduct(@Valid @RequestBody Product product) {
        log.info("创建商品请求: {}", product.getName());
        return productService.createProduct(product);
    }

    /**
     * 根据ID获取商品
     */
    @GetMapping("/{id}")
    public Result<Product> getProductById(@PathVariable Long id) {
        log.info("查询商品信息: {}", id);
        return productService.getProductById(id);
    }

    /**
     * 获取所有商品
     */
    @GetMapping
    public Result<List<Product>> getAllProducts() {
        log.info("查询所有商品");
        return productService.getAllProducts();
    }

    /**
     * 根据分类获取商品
     */
    @GetMapping("/category/{categoryId}")
    public Result<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        log.info("根据分类查询商品: {}", categoryId);
        return productService.getProductsByCategory(categoryId);
    }

    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public Result<List<Product>> searchProducts(@RequestParam String keyword) {
        log.info("搜索商品: {}", keyword);
        return productService.searchProducts(keyword);
    }

    /**
     * 更新商品信息
     */
    @PutMapping("/{id}")
    public Result<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        log.info("更新商品信息: {}", id);
        return productService.updateProduct(id, productDetails);
    }

    /**
     * 更新商品库存
     */
    @PatchMapping("/{id}/stock")
    public Result<Product> updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        log.info("更新商品库存: {}, 新库存: {}", id, stock);
        return productService.updateStock(id, stock);
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        log.info("删除商品: {}", id);
        return productService.deleteProduct(id);
    }

    /**
     * 检查商品是否存在
     */
    @GetMapping("/check")
    public Result<Boolean> checkProductExists(@RequestParam String name) {
        log.info("检查商品是否存在: {}", name);
        return productService.checkProductExists(name);
    }
}