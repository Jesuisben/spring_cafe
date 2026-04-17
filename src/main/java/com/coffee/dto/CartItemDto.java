package com.coffee.dto;

/* 리액트의 CartProduct.ts에 맞게 작성
export interface CartProduct {
    cartProductId: number;
    productId: number;
    image: string;
    name: string;
    quantity: number;
    price: number;
    checked: boolean;
};
*/

import com.coffee.entity.CartProduct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long cartProductId ;
    private Long productId ;
    private String name ;
    private String image ;
    private int price ;
    private int stock ;
    private int quantity ;
    private boolean checked ;

    public CartItemDto(CartProduct cartProduct) {
        this.cartProductId = cartProduct.getId();
        this.productId = cartProduct.getProduct().getId() ;
        this.name = cartProduct.getProduct().getName() ;
        this.image = cartProduct.getProduct().getImage() ;
        this.price = cartProduct.getProduct().getPrice() ;
        this.stock = cartProduct.getProduct().getStock() ;
        this.quantity = cartProduct.getQuantity() ;
    }
}
