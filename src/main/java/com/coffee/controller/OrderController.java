package com.coffee.controller;

import com.coffee.dto.OrderDto;
import com.coffee.entity.Order;
import com.coffee.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> order(@RequestBody OrderDto dto){
        System.out.println("주문 요청 dto : " + dto);

        Order saverOrder = orderService.createOrder(dto);

        String message = "송장 번호 " + saverOrder.getId() + "에 대한 주문이 완료 되었습니다.";
        return ResponseEntity.ok(message) ;
    }
}
