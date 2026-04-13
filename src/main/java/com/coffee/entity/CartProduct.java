package com.coffee.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "cart_products")
public class CartProduct {
    @Id // 기본키(pk) 설정
    @GeneratedValue(strategy = GenerationType.AUTO) // 숫자 자동 생성
    @Column(name = "cart_product_id") // 실제 데이터 베이스 컬럼명은 cart_product_id
    private Long id;

    // 부모자식 참조관계에서 일대일, 일대다, 다대일은 중요하지 않음
    // 어떤 관계든 자식이 부모의 PK를 FK로 가진다
    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn은 자식 테이블에 적어야 하는 어노테이션임 (조인 어노테이션)
    @JoinColumn(name = "cart_id") // carts테이블의 pk 컬럼명과 동일하게 적어야 함 (관례임)
    private Cart cart; // cart_products테이블에 cart_id라는 이름의 carts테이블의 fk인 컬럼이 생성됨

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product ;

    // 카트에 담길 상품들의 갯수
    @Column(nullable = false)
    private int quantity ;
}
