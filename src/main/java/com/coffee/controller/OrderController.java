package com.coffee.controller;

import com.coffee.constant.OrderStatus;
import com.coffee.constant.Role;
import com.coffee.dto.OrderDetailDto;
import com.coffee.dto.OrderDto;
import com.coffee.entity.Order;
import com.coffee.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/list") // 리액트의 OrderList.js 파일 내의 useEffect 참조
    public ResponseEntity<List<OrderDetailDto>> getOrderList(@RequestParam Long memberId,
                                                             @RequestParam Role role){
        System.out.println("로그인 한 사람의 아이디" + memberId);
        System.out.println("로그인 한 사람의 역할" + role);

        List<OrderDetailDto> responseDto = orderService.getOrderListByRole(memberId, role);

        System.out.println("주문 건수 : " + responseDto.size());

        return ResponseEntity.ok(responseDto) ;
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<String> statusChange(@PathVariable Long orderId,
                                               @RequestParam OrderStatus status){
        System.out.println("수정할 항목의 아이디 : " + orderId);
        System.out.println("변경하고자 하는 주문 상태 : " + status);

        String message = orderService.updateOrderStatus(orderId, status);

        return ResponseEntity.ok(message) ;
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId){
        try {
            String message = orderService.cancelOrder(orderId);
            return ResponseEntity.ok(message);

        }catch(IllegalArgumentException err){
            return ResponseEntity.notFound().build() ;
        }
    }



}
