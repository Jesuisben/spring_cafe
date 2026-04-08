package com.coffee.service;

import com.coffee.entity.Product;
import com.coffee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository ;

    public List<Product> getProductList(){
        return this.productRepository.findProductByOrderByIdDesc();
    }
}
