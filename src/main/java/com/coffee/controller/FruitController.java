package com.coffee.controller;


import com.coffee.entity.Fruit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController // Rest API 요청에 대하여 처리를 해주는 컨트롤러입니다.
public class FruitController {
    @GetMapping("/fruit") // http://localhost:9000/fruit
    public Fruit test(){
        Fruit bean = new Fruit(); // bean이 저장될 데이터
        bean.setId("banana");
        bean.setName("바나나");
        bean.setPrice(1000);
        return bean;
    }

    @GetMapping("/fruit/list") // http://localhost:9000/fruit/list
    public List<Fruit> test02(){
        List<Fruit> fruitList = new ArrayList<>();

        fruitList.add(new Fruit("apple", "사과", 1000));
        fruitList.add(new Fruit("pear", "배", 2000));
        fruitList.add(new Fruit("grape", "포도", 3000));
        return fruitList;
    }
}
