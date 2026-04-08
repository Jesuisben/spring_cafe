package com.coffee.entity;

import com.coffee.constant.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name= "products")
@Getter @Setter @ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long id; // 정수 int 보다 더 큰 타입은 Long으로 지정함

    @Column(nullable = false)
    @NotBlank(message = "상품 이름은 필수 입력 사항입니다.")
    private String name;

    @Column(nullable = false)
    // 정수형의 크기 제한
    @Min(value = 100, message = "가격은 100원이상이어야 합니다.")
    @Max(value = 100000, message = "가격은 100000원이하이어야 합니다.")
    private int price;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "카테고리는 반드시 선택해야 합니다.")
    private Category category;

    @Column(nullable = false)
    @Min(value = 10, message = "재고 수량은 10개 이상이어야 합니다.")
    @Max(value = 1000, message = "재고 수량은 1000개 이하이어야 합니다.")
    private int stock ;

    @Column(nullable = false)
    @NotBlank(message = "이미지는 필수 입력 사항입니다.")
    private String image;

    @Column(nullable = false)
    @NotBlank(message = "상품 설명은 필수 입력 사항입니다.")
    // 문자열의 크기 제한
    @Size(max = 1000, message = "상품에 대한 설명은 최대 1,000 자리 이하로만 입력해 주세요.")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate inputdate;
}
