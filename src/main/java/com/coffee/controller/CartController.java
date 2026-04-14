package com.coffee.controller;

import com.coffee.dto.CartItemDto;
import com.coffee.dto.CartProductDto;
import com.coffee.entity.Member;
import com.coffee.service.CartService;
import com.coffee.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService ;

    @PostMapping("/insert") // /cart/insert
    public ResponseEntity<String> addToCart(@RequestBody CartProductDto dto,
                                            Authentication authentication){
        String email = authentication.getName() ;
        String message = cartService.addProductToCart(dto, email);

        return ResponseEntity.ok(message) ;
    }

    private final MemberService memberService ;

    @GetMapping("/list")
    public ResponseEntity<List<CartItemDto>> getCartProducts(Authentication authentication){
        String email = authentication.getName();

        Member member = memberService.findByEmail(email) ;

        if (member == null){
            new RuntimeException("사용자가 존재하지 않습니다.");
        }

        return ResponseEntity.ok(cartService.getCartItemsByMemberId(member.getId())) ;
    }
}
