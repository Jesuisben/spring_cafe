package com.coffee.test;

import java.util.HashMap;
import java.util.Map;

public class MapTest {
    public static void main(String[] args) {
        // Map<키, 값>
        Map<String, String> errors = new HashMap<>();

        errors.put("password", "비밀 번호 누락"); // error.put(String key, String value);
        errors.put("email", "이메일 잘못 들어옴");

        System.out.println(errors);
        // 출력 : {password=비밀 번호 누락, email=이메일 잘못 들어옴}

        Map<String, String> colors // Map.of(key, value, key, value ......);
                = Map.of("red", "빨강", "blue", "파랑", "yellow", "노랑");
        System.out.println(colors);
        // 출력 : {yellow=노랑, blue=파랑, red=빨강}
    }
}
