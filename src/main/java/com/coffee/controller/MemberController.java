package com.coffee.controller;

import com.coffee.entity.Member;
import com.coffee.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService ;

    // @RequestBody : 넘어온 request정보가 JSON형식인데 그것을 Java 형식으로 바꿔주는 것
    // 원래 VSC(React-프론트앤드)에서 넘어온 데이터는 bean.getName같은거에 있음
    // Controller까지는 그대로의 데이터이고 그 이후인 Service에서 암호화를 하든 말든 함
    @PostMapping("/member/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody Member bean, BindingResult bindingResult){ // 회원 가입하기
        System.out.println("회원 가입 정보");
        System.out.println(bean);

        if(bindingResult.hasErrors()){ // 가입에 뭔가 문제 있음
            Map<String, String> errors = new HashMap<>();
            for(FieldError xx : bindingResult.getFieldErrors()){ // Field : Java에서의 변수 (name, password 등)
                errors.put(xx.getField(), xx.getDefaultMessage());
            }
            System.out.println(errors);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }else{
            System.out.println("ok");
        }

        // 이메일 중복 체크
        Member member = memberService.findByEmail(bean.getEmail());
        if (member != null){ // member가 null이 아니라는 것은 이미 존재하는 id(맴버)라는 것을 의미함
            // 이미 존재하는 이메일 주소
            return new ResponseEntity<>(Map.of("email", "이미 존재하는 이메일 주소입니다."),
                    HttpStatus.BAD_REQUEST);
        }

        // 회원 가입 처리
        memberService.insert(bean);
        return new ResponseEntity<>("회원 가입 성공", HttpStatus.OK) ; // 회원 가입 성공 (OK라는건 200번대라는 뜻)
    }
}
