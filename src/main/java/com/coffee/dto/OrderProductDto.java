package com.coffee.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class OrderProductDto {
    private Long cartProductId ;
    private Long productId ;
    private int quantity ;
}
