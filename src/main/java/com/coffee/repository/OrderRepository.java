package com.coffee.repository;

import com.coffee.constant.OrderStatus;
import com.coffee.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// extends JpaRepository<entity이름, entity의_PK의 타입>
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 쿼리 메소드를 사용하여 특정 회원의 송장 번호가 큰 것(최신 주문) 것부터 조회합니다.
    // 주문의 상태가 PENDING인것만 조회합니다.
    // cf. 좀더 복잡한 쿼리를 사용하시려면 @Query 또는 querydsl을 사용하세요.

    // User꺼
    List<Order> findByMemberIdAndOrderStatusOrderByIdDesc(Long memberId, OrderStatus Status);

    // Admin꺼
    List<Order> findByOrderStatusOrderByIdDesc(OrderStatus Status);

    // 특정 주문에 대하여 주문의 상태를 '주문 완료(COMPLETED)'로 변경합니다.
    // 쿼리 메소드대신 @Query 어노테이션 사용 예시 : sql 대신 JPQL
    // 주의 사항
    // 1. 테이블 이름 대신 Entity 이름을 명시 (중요!!!)
    // 2. 대소문자 구분합니다. (중요!!!)
    @Modifying // 이 쿼리는 select 구문이 아니고, 데이터 변경을 위한 쿼리입니다.
    @Transactional // import jakarta.transaction.Transactional; // 롤백할 수도 있으니까
    @Query("update Order o set o.orderStatus = :status where o.id = :orderId")
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("status") OrderStatus status);


}
