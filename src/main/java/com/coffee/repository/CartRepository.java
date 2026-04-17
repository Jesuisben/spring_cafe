package com.coffee.repository;

import com.coffee.entity.Cart;
import com.coffee.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// extends JpaRepository<entity이름, entity의_PK의 타입>
public interface CartRepository extends JpaRepository<Cart, Long> {
    // 이 member가 가진 카트 정보가 있는지 조회하는 메소드 (카트를 가지고 있는지 없는지)
    Optional<Cart> findByMember(Member member);
}
