package com.coffee.entity;

import com.coffee.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter @ToString
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    // 송장번호
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 일대다 관계
    // @JoinColumn이 있으면 무조건 fk
    // name은 참조하는 pk의 이름과 동일하게 하면 됨
    @JoinColumn(name = "member_id")
    // 주문한 사람
    private Member member ;

    // 이걸 적지 않으면 단방향관계인데
    // 양방향관계를 만들어주기 위해서 이렇게 적음
    // 주문안에는 주문상품이 여러개 담길 수 있어서 컬렉션(중에서도 List)로 변수 생성해야 함
    // 통상적으로 우리가 주문을 할 때 여러 개의 '주문 상품'을 동시에 주문합니다.
    // mappedBy = "order" : 연결된 OrderProduct entity에 있는 order(fk)가 관리의 주체임 (fk의 변수명)
    // 따라서 @OneToMany는 주체에 종속되는 데이터임
    // (DB에서는 pk가 데이터를 관리하지만, JPA관점에서는 fk가 데이터를 관리함)
    // orphanRemoval = true : 부모와의 연결이 끊어진 자식 엔티티를 자동으로 삭제하는 기능 (DB에서도 데이터 삭제됨)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts;

    // 주문한 날짜
    private LocalDate orderdate ;

    @Enumerated(EnumType.STRING)
    // 주문의 상태 정보
    private OrderStatus orderStatus ;

}
