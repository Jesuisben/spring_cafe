package com.coffee.test;

import java.util.Random;

public class RandomTest {
    public static void main(String[] args) {
        Random rand = new Random();

        // rand.nextBoolean()는 50%의 확률로 true나 false 중 하나를 무작위로 뽑아주는 동작
        boolean bool = rand.nextBoolean();
        System.out.println(bool);

        // rand.nextInt()는 숫자의 범위가 0부터 시작함 6넣으면 0~5사이 나옴
        // 그래서 소괄호 바깥에 + 1 하면 됨
        int jusawee = rand.nextInt(6) + 1;
        System.out.println(jusawee);

        // 배열의 배열요소를 rand.nextInt()를 이용해서 랜덤으로 추출하기
        String[] menu = {"제육볶음", "돈까스", "오므라이스", "떡볶이", "마라탕"};
        String item = menu[rand.nextInt(menu.length)];
        String message = "오늘 점심 메뉴 : " + item;
        System.out.println(message);

        // 가격 설정 (범위 설정)
        // 3000원 <= 가격 <= 7000원
        int price = 1000 * (rand.nextInt(5) + 3) ;
        System.out.println("가격 : " + price);
    }
}
