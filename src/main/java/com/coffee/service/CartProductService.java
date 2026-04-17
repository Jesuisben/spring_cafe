package com.coffee.service;

import com.coffee.entity.CartProduct;
import com.coffee.entity.Product;
import com.coffee.repository.CartProductRepository;
import com.coffee.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Service 객체 의미 // 서비스는 로직을 처리함
@RequiredArgsConstructor
public class CartProductService {
    private final CartProductRepository cartProductRepository ;

    // 메소드의 3요소 (반환타입, 메소드명, 매개변수)를 만드는데 시간이 많이 걸림 (연습필요)
    public void saveCartProduct(CartProduct cp){ // CartProduct 하나 입력 받아서 저장
        this.cartProductRepository.save(cp);
    }

    private final ProductRepository productRepository ;

    public String editCartProductQuantity(Long cartProductId, Integer quantity){
        // 해당 카트 상품 찾기
        // Optional이 java.util이라는 것을 무조건 외우기
        // service 코딩 중이니까 Repository한테 물어봐야 함
        // findById는 CrudRepository에 있음 (상속 느껴보기)
        Optional<CartProduct> cartProductOptional = cartProductRepository.findById(cartProductId);

        // Optional 클래스도 나중에 한 번 공부 해오기
        if (cartProductOptional.isEmpty()){
            return "오류 : 카트 품목을 찾을 수 없습니다.";
        }

        // 재고 수량 점검 및 수량 변경
        CartProduct cartProduct = cartProductOptional.get();

        int stock = cartProduct.getProduct().getStock() ;
        if (quantity > stock){ // 손님이 입력한 수보다 재고가 부족할 경우
            return "오류 : 재고 수량이 부족합니다.";
        }

        // 덮어쓰기하는 경우
        cartProduct.setQuantity(quantity);
        // 만약에 누적을 할 경우 (누적 변경시 다음과 같이 코딩합니다.)
        // cartProduct.setQuantity(cartProduct.getQuantity() + quantity);


        // 데이터 베이스에 저장
        cartProductRepository.save(cartProduct);

        // 성공 메시지 반환
        String message = "카트 상품 아이디 " + cartProductId + "번이 " + quantity + "개로 수정이 되었습니다." ;
        return message ;
    }

    public void deleteCartProductById(Long cartProductId){
        cartProductRepository.deleteById(cartProductId);
    }

}
