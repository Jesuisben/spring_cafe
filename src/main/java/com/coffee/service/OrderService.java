package com.coffee.service;

import com.coffee.dto.OrderDto;
import com.coffee.dto.OrderProductDto;
import com.coffee.entity.Member;
import com.coffee.entity.Order;
import com.coffee.entity.OrderProduct;
import com.coffee.entity.Product;
import com.coffee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberService memberService;
    private final ProductService productService;
    private final CartProductService cartProductService;
    private final OrderRepository orderRepository;

    public Order createOrder(OrderDto dto){
        Optional<Member> optionalMember = memberService.findMemberById(dto.getMemberId());
        // 그런 맴버가 없다면
        // Optional 추가 공부하기
        if (!optionalMember.isPresent()){
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        // 주문 객체 생성
        Order order = new Order();

        // 주문 객체에 맴버의 데이터를 넣음
        order.setMember(member);
        order.setOrderdate(LocalDate.now());
        order.setOrderStatus(dto.getStatus());

        List<OrderProduct> orderProductList = new ArrayList<>();

        for(OrderProductDto item : dto.getOrderItems()){
            Long productId = item.getProductId();
            System.out.println("상품 아이디 : " + productId);

            Optional<Product> optionalProduct = productService.findProductById((productId));

            if (!optionalProduct.isPresent()){
                throw new RuntimeException("해당 상품이 존재하지 않습니다.");
            }

            Product product = optionalProduct.get() ;

            if (product.getStock() < item.getQuantity()){
                throw new RuntimeException("재고 수량이 부족합니다.");
            }

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(item.getQuantity());

            orderProductList.add(orderProduct);

            product.setStock(product.getStock() - item.getQuantity());

            Long cartProductId = item.getCartProductId();
            if (cartProductId != null){// 장바구니에서 주문하기 클릭하면
                cartProductService.deleteCartProductById(cartProductId);
            }else{ // 상품 상세 보기 페이지에서 주문하기 클릭하면
                System.out.println("상품 상세 보기 페이지에서 클릭하셨군요.");
            }
        }

        order.setOrderProducts(orderProductList);

        return orderRepository.save(order) ;
    }
}
