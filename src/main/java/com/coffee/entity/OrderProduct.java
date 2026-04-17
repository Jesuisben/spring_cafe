package com.coffee.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "order_products")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_product_id")
    // 송장번호
    private Long id;

    // 주문과 주문 상품은 일대다 관계
    // 하나의 주문에는 여러가지 상품이 들어감
    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn이 있으면 무조건 fk
    // name은 참조하는 pk의 이름과 동일하게 하면 됨
    @JoinColumn(name = "order_id")
    private Order order ;

    // 모든 상품은 여러개의 주문 상품에 담길 수가 있음
    // 상품(product)을 물질적인 것이라고 생각하지말고 상품이라는 개념, 종류라고
    // 생각하면 이해하기 쉬움
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product ;

    @Column(nullable = false)
    private int quantity ;
}
