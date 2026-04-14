package com.coffee.service;

import com.coffee.dto.CartItemDto;
import com.coffee.dto.CartProductDto;
import com.coffee.entity.Cart;
import com.coffee.entity.CartProduct;
import com.coffee.entity.Member;
import com.coffee.entity.Product;
import com.coffee.repository.CartRepository;
import com.coffee.repository.MemberRepository;
import com.coffee.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 제일 먼저 Entity를 만들고 그에 해당하는 Repository를 만들고
// CartService를 코딩하기 위해서 CartRepository가 필요하다는걸 알면 순서를 정할 수 있음

@Service
@RequiredArgsConstructor
public class CartService { // 필요한 변수들 생성
    private final CartRepository cartRepository ;
    private final MemberService memberService ;
    private final ProductService productService ;
    private final CartProductService cartProductService ;
    private final MemberRepository memberRepository ;
    private final ProductRepository productRepository ;

    public Cart saveCart(Cart cart){
        return cartRepository.save(cart);
    }

    private CartProduct findExistingProduct(Cart cart, Product product){
        // 해당 상품이 카트 내에 들어 있으면, 해당 상품 객체를 반환해주는 메소드
        // 동일한 상품이 이미 카트 내에 들어 있으면 수량을 누적할 목적임
        if (cart.getCartProducts() == null) return null ;

        for(CartProduct cp : cart.getCartProducts()){
            if (cp.getProduct().getId().equals(product.getId())){
                return cp;
            }
        }
        return null ;
    }

    @Transactional
    public String addProductToCart(CartProductDto dto, String email){
        // 회원 조회
        // email을 이용해서 member를 찾을 예정
        Member member = memberRepository.findByEmail(email) ;

        if (member == null) {
            throw new RuntimeException("회원 없음");
        }

        // 상품 조회
        Product product = productRepository.findById(dto.getProductId())
                // 예외처리를 의도적으로 발생시키기
                .orElseThrow(() -> new RuntimeException("상품 없음")) ;


        // 재고 확인(주문 수량이 재고보다 많으면)
        if (product.getStock() < dto.getQuantity()){
            throw new RuntimeException("재고 수량이 부족합니다.");
        }

        // 장바구니 조회 또는 생성
        Cart cart = cartRepository.findByMember(member).orElse(null) ;

        if (cart == null){ // 카트가 구비 안된 고객
            Cart newCart = new Cart() ; // 새 카트 준비
            newCart.setMember(member); // 고객에게 할당
            cart = saveCart(newCart);
        }

        // 기존 상품이 존재하는 지 확인 후 수량 처리
        CartProduct existingCartProduct = findExistingProduct(cart, product) ;

        if (existingCartProduct != null){ // 장바구니에 해당 상품이 들어 있으면
            // 기존 수량에 장바구니에서 요청항 수량을 누적합니다.
            // existingCartProduct.getQuantity() : 이미 카트에 들어 있는 값
            // dto.getQuantity() : 새로 추가 하는 값
            existingCartProduct.setQuantity(existingCartProduct.getQuantity()
                    + dto.getQuantity());

            // 서비스의 저장 메소드를 요청하여 database에 저장합니다.
            // 서비스를 요청하는데 이것이 Repository에 가서 database에 저장함
            cartProductService.saveCartProduct(existingCartProduct);

        }else{ // 장바구니에 품목이 없는 경우
            CartProduct cp = new CartProduct();

            // 물건을 어떤 카트에 담을지 설정
            cp.setCart(cart);

            // 어떤 물건 종류를 담을지 설정
            cp.setProduct(product);

            // 웹페이지에서 입력한 수량 입력하기
            cp.setQuantity(dto.getQuantity());

            // Repository에 보내기
            cartProductService.saveCartProduct(cp);
        }

        return "요청하신 상품이 장바구니에 추가되었습니다." ;
    }

    public List<CartItemDto> getCartItemsByMemberId(Long memberId){
        // 회원 조회
        Member member = memberService.findMemberById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 회원입니다.")) ;

        // 회원이 소유한 카트 정보 조회
        // 없으면 빈 카트 생성
        Cart cart = cartRepository.findByMember(member).orElseGet(Cart::new);

        List<CartItemDto> cartItemDtoList = new ArrayList<>();
        for (CartProduct cp : cart.getCartProducts()){
            cartItemDtoList.add(new CartItemDto(cp));
        }
        return cartItemDtoList ;

        /* return cart.getCartProducts().stream()
                .map(CartItemDto::new).toList() ; */
    }
}
