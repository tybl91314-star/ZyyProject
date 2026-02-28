package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.common.response.Result;
import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService {

    /**
     * 创建商品
     */
    Result<Product> createProduct(Product product);

    /**
     * 根据ID获取商品
     */
    Result<Product> getProductById(Long id);

    /**
     * 获取所有商品
     */
    Result<List<Product>> getAllProducts();

    /**
     * 根据分类获取商品
     */
    Result<List<Product>> getProductsByCategory(Long categoryId);

    /**
     * 搜索商品
     */
    Result<List<Product>> searchProducts(String keyword);

    /**
     * 更新商品信息
     */
    Result<Product> updateProduct(Long id, Product productDetails);

    /**
     * 更新商品库存
     */
    Result<Product> updateStock(Long id, Integer stock);

    /**
     * 删除商品（软删除）
     */
    Result<Void> deleteProduct(Long id);

    /**
     * 检查商品是否存在
     */
    Result<Boolean> checkProductExists(String name);
}