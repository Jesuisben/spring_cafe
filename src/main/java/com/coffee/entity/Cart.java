package com.coffee.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
@Entity
@Table(name = "carts")
public class Cart {
    @Id // 기본키(pk) 설정
    @GeneratedValue(strategy = GenerationType.AUTO) // 숫자 자동 생성
    @Column(name = "cart_id") // 실제 데이터 베이스 컬럼명은 cart_id
    private Long id ;

    // 연관 관계 매핑 (Entity간의 연관 관계 서술)
    // fetch : 데이터 베이스에서 데이터를 가져온다는 뜻
    // (우리는 axios를 사용하지만 자바의 기본 기능이 fetch)
    // FetchType.LAZY : 즉시로딩 말고 지연로딩을 의미함 (진짜 필요할때 가져오기)
    // 부모자식 참조관계에서 일대일, 일대다, 다대일은 중요하지 않음
    // 어떤 관계든 자식이 부모의 PK를 FK로 가진다
    @OneToOne(fetch = FetchType.LAZY)
    // @JoinColumn은 자식 테이블에 적어야 하는 어노테이션임 (조인 어노테이션)
    @JoinColumn(name = "member_id") // members테이블의 pk 컬럼명과 동일하게 적어야 함 (관례임)
    private Member member ; // carts테이블에 member_id라는 이름의 members테이블의 fk인 컬럼이 생성됨

    // 여기에는 @JoinColumn을 적지 않음. 왜?
    // 카트안에는 카트상품이 여러개 담길 수 있어서 컬렉션(중에서도 List)로 변수 생성해야 함
    // 카트에는 여러 개의 '카트 상품'들이 담겨야 하므로 List가 좋습니다
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartProduct> cartProducts ;
}
