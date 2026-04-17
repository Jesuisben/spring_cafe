package com.coffee.repository;

import com.coffee.constant.OrderStatus;
import com.coffee.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// extends JpaRepository<entity이름, entity의_PK의 타입>
public interface OrderRepository extends JpaRepository<Order, Long> {
    // User꺼
    List<Order> findByMemberIdAndOrderStatusOrderByIdDesc(Long memberId, OrderStatus Status);

    // Admin꺼
    List<Order> findByOrderStatusOrderByIdDesc(OrderStatus Status);

}
