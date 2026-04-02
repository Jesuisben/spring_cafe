package com.coffee.repository;

import com.coffee.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// 인터페이스들끼리의 참조는 extends임 (implements가 아님)
// JpaRepository<관리하고자하는엔터티이름, 해당엔터티의기본키의타입> (Member의 기본키인 id의 타입은 Long)
public interface MemberRepository extends JpaRepository<Member, Long> {
}
