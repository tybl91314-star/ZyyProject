package com.example.productservice.service.impl;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import com.example.common.response.Result;
import com.example.common.enums.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 商品服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Result<Product> createProduct(Product product) {
        try {
            log.info("创建商品: {}", product.getName());

            // 检查商品名称是否已存在
            if (productRepository.existsByName(product.getName())) {
                return Result.error(ResultCode.BAD_REQUEST.getCode(), "商品名称已存在");
            }

            // 设置默认状态
            if (product.getStatus() == null) {
                product.setStatus(1);
            }

            Product savedProduct = productRepository.save(product);
            log.info("商品创建成功, ID: {}", savedProduct.getId());

            return Result.success("商品创建成功", savedProduct);

        } catch (Exception e) {
            log.error("创建商品失败: {}", e.getMessage());
            return Result.error("商品创建失败");
        }
    }

    @Override
    public Result<Product> getProductById(Long id) {
        try {
            Optional<Product> productOpt = productRepository.findById(id);
            if (!productOpt.isPresent()) {
                return Result.error(ResultCode.NOT_FOUND.getCode(), "商品不存在");
            }

            Product product = productOpt.get();
            return Result.success(product);

        } catch (Exception e) {
            log.error("获取商品信息失败: {}", e.getMessage());
            return Result.error("获取商品信息失败");
        }
    }

    @Override
    public Result<List<Product>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return Result.success(products);
        } catch (Exception e) {
            log.error("获取商品列表失败: {}", e.getMessage());
            return Result.error("获取商品列表失败");
        }
    }

    @Override
    public Result<List<Product>> getProductsByCategory(Long categoryId) {
        try {
            List<Product> products = productRepository.findByCategoryId(categoryId);
            return Result.success(products);
        } catch (Exception e) {
            log.error("根据分类获取商品失败: {}", e.getMessage());
            return Result.error("根据分类获取商品失败");
        }
    }

    @Override
    public Result<List<Product>> searchProducts(String keyword) {
        try {
            List<Product> products = productRepository.searchByName(keyword);
            return Result.success(products);
        } catch (Exception e) {
            log.error("搜索商品失败: {}", e.getMessage());
            return Result.error("搜索商品失败");
        }
    }

    @Override
    @Transactional
    public Result<Product> updateProduct(Long id, Product productDetails) {
        try {
            Optional<Product> productOpt = productRepository.findById(id);
            if (!productOpt.isPresent()) {
                return Result.error(ResultCode.NOT_FOUND.getCode(), "商品不存在");
            }

            Product existingProduct = productOpt.get();

            // 更新非空字段
            if (productDetails.getName() != null) {
                // 检查新名称是否与其他商品冲突
                if (!existingProduct.getName().equals(productDetails.getName()) &&
                        productRepository.existsByName(productDetails.getName())) {
                    return Result.error(ResultCode.BAD_REQUEST.getCode(), "商品名称已存在");
                }
                existingProduct.setName(productDetails.getName());
            }

            if (productDetails.getDescription() != null) {
                existingProduct.setDescription(productDetails.getDescription());
            }

            if (productDetails.getPrice() != null) {
                existingProduct.setPrice(productDetails.getPrice());
            }

            if (productDetails.getCategoryId() != null) {
                existingProduct.setCategoryId(productDetails.getCategoryId());
            }

            if (productDetails.getCategoryName() != null) {
                existingProduct.setCategoryName(productDetails.getCategoryName());
            }

            if (productDetails.getImageUrl() != null) {
                existingProduct.setImageUrl(productDetails.getImageUrl());
            }

            Product updatedProduct = productRepository.save(existingProduct);
            return Result.success("商品更新成功", updatedProduct);

        } catch (Exception e) {
            log.error("更新商品信息失败: {}", e.getMessage());
            return Result.error("更新商品信息失败");
        }
    }

    @Override
    @Transactional
    public Result<Product> updateStock(Long id, Integer stock) {
        try {
            Optional<Product> productOpt = productRepository.findById(id);
            if(!productOpt.isPresent()) {
                return Result.error(ResultCode.NOT_FOUND.getCode(), "商品不存在");
            }

            Product product = productOpt.get();
            product.setStock(stock);

            Product updatedProduct = productRepository.save(product);
            return Result.success("库存更新成功", updatedProduct);

        } catch (Exception e) {
            log.error("更新商品库存失败: {}", e.getMessage());
            return Result.error("更新商品库存失败");
        }
    }

    @Override
    @Transactional
    public Result<Void> deleteProduct(Long id) {
        try {
            Optional<Product> productOpt = productRepository.findById(id);
            if (!productOpt.isPresent()) {
                return Result.error(ResultCode.NOT_FOUND.getCode(), "商品不存在");
            }

            Product product = productOpt.get();
            product.setStatus(0);  // 软删除，状态设为0（下架）
            productRepository.save(product);

            return Result.success();
        } catch (Exception e) {
            log.error("删除商品失败: {}", e.getMessage());
            return Result.error("删除商品失败");
        }
    }

    @Override
    public Result<Boolean> checkProductExists(String name) {
        try {
            boolean exists = productRepository.existsByName(name);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查商品存在性失败: {}", e.getMessage());
            return Result.error("检查商品存在性失败");
        }
    }
}