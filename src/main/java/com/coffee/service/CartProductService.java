package com.coffee.service;

import com.coffee.entity.CartProduct;
import com.coffee.repository.CartProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service // Service 객체 의미
@RequiredArgsConstructor
public class CartProductService {
    private final CartProductRepository cartProductRepository ;

    // 메소드의 3요소 (반환타입, 메소드명, 매개변수)를 만드는데 시간이 많이 걸림 (연습필요)
    public void saveCartProduct(CartProduct cp){ // CartProduct 하나 입력 받아서 저장
        this.cartProductRepository.save(cp);
    }
}
