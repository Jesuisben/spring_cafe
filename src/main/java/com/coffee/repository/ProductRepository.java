package com.coffee.repository;

import com.coffee.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 쿼리 메소드 findProductByOrderByIdDesc()
    // 상품(Product)들을 가져오는데, ID 번호가 큰 것부터(최신순으로) 정렬해서 다 가져와라!
    List<Product> findProductByOrderByIdDesc();

    List<Product> findByImageContaining(String keyword);
}
