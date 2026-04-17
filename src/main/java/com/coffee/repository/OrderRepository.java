package com.coffee.repository;

import com.coffee.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

// extends JpaRepository<entity이름, entity의_PK의 타입>
public interface OrderRepository extends JpaRepository<Order, Long> {

}
