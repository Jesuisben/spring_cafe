package com.coffee.dto;

import lombok.Getter;
import lombok.Setter;

// 장바구니에 담기 위하여 리액트가 넘겨 주는 파라미터의 값을 저장하기 위한 클래스
@Getter @Setter
public class CartProductDto {
    private Long memberId ;
    private Long productId ;
    private int quantity ;
}
