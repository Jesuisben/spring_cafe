package com.coffee.controller;

import com.coffee.entity.Fruit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FruitHtmlController {
    @GetMapping("/fruit01") // http://localhost:9000/fruit01
    public String test(Model model){
        // Model은 데이터 저장소 역할
        Fruit bean = new Fruit(); // bean이 저장될 데이터
        bean.setId("banana");
        bean.setName("바나나");
        bean.setPrice(1000);

        // 저장시 식별할 수 있도록 "fruit"라는 이름으로 저장
        model.addAttribute("fruit", bean);

        // html 문서에서 이를 접근할 수 있음
        return "fruit"; // fruit.html 문서로 이동
    }

    @GetMapping("/fruit01/list") // http://localhost:9000/fruit01/list
    public String test02(Model model){
        // 상품 여러개를 저장하기 위한 List 컬렉션
        List<Fruit> fruitList = new ArrayList<>();

        fruitList.add(new Fruit("apple", "사과", 1000));
        fruitList.add(new Fruit("pear", "배", 2000));
        fruitList.add(new Fruit("grape", "포도", 3000));

        model.addAttribute("fruitList", fruitList);

        return "fruitList";
    }




}

