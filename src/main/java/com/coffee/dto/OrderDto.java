package com.coffee.dto;

import com.coffee.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderDto {
    private Long memberId ; // 주문자 정보
    private OrderStatus status ;

    // 주문에 주문 상품이 여러개 있을 수 있으니까
    // 그 상품들의 DTO를 같이 가져오는 것
    // orderItems: 주문한 것
    // List<OrderProductDto>: 주문한 것들의 속성
    private List<OrderProductDto> orderItems ; // 주문 상품 목록

}
