package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 商品数据访问接口
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 根据商品名称查找商品
    Optional<Product> findByName(String name);

    // 根据分类ID查找商品
    List<Product> findByCategoryId(Long categoryId);

    // 根据状态查找商品
    List<Product> findByStatus(Integer status);

    // 根据价格范围查找商品
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // 查找库存大于指定数量的商品
    List<Product> findByStockGreaterThan(Integer stock);

    // 根据名称模糊搜索
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.status = 1")
    List<Product> searchByName(@Param("keyword") String keyword);

    // 检查商品名称是否存在
    boolean existsByName(String name);
}