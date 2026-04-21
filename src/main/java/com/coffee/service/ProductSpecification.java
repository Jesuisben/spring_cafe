package com.coffee.service;

import com.coffee.constant.Category;
import com.coffee.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// 리액트의 SearchCondition.ts를 위해서 만든 파일임
public class ProductSpecification {
    // 1.	날짜 범위 필터링
    public static Specification<Product> hasDateRange (String searchDateType){
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query,
             CriteriaBuilder criteriaBuilder) {
                LocalDateTime now = LocalDateTime.now() ; // 현재 시각

                LocalDateTime startDate = null ; // 검색 시작 일자

                // 선택한 콤보 박스의 값을 보고, 검색 시작 일자를 계산합니다.
                switch (searchDateType){
                    case "1d":
                        startDate = now.minus(1, ChronoUnit.DAYS) ;
                        break;
                    case "1w":
                        startDate = now.minus(1, ChronoUnit.WEEKS) ;
                        break;
                    case "1m":
                        startDate = now.minus(1, ChronoUnit.MONTHS) ;
                        break;
                    case "6m":
                        startDate = now.minus(6, ChronoUnit.MONTHS) ;
                        break;
                    case "all":
                    default: // 전체 기간 조회
                        return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }

                // 상품 입고 일자(inputdate)가 검색 시작 일자(startDate) 이후의 상품들만 검색합니다.
                // startDate보다 크거나 혹은 같거나
                return criteriaBuilder.greaterThanOrEqualTo(root.get("inputdate"), startDate);
            }
        };
    };

    // 2.	카테고리 필터링
    public static Specification<Product> hasCategory(Category category){
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query,
             CriteriaBuilder criteriaBuilder) {
                if (category == Category.ALL){ // DB로 따지면 where절이 없는 / 다 가져오는
                    return criteriaBuilder.conjunction();
                }else{ // category가 이것과 같은 것을 return함
                    return criteriaBuilder.equal(root.get("category"), category);
                }
            }
        };
    }

    // 3.	검색 컬럼 이름(mode)
    public static Specification<Product> hasNameLike(String keyword){
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query,
             CriteriaBuilder criteriaBuilder) {
                // DB의 like절로 특정 단어가 들어 있는 것을 찾기
                // like 연산자의 (select name from members where name like '%keyword%' ;) 느낌
                return criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
            }
        };
    }

    // 4.	상품 검색 키워드
    public static Specification<Product> hasDescriptionLike(String keyword){
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query,
             CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("description"), "%" + keyword + "%");
            }
        };
    }

}
