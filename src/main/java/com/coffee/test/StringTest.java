package com.coffee.test;

/*startsWith(), endsWith(), substring(), length(), toLowerCase()
toUpperCase(), lastIndexOf(), replace(), contains()
        String.format("%02d", count);*/

public class StringTest {
    public static void main(String[] args) {
        String sample = "Bearer hello world" ;

        String result ;
        // IT에서 Case는 무조건 대소문자와 연관된 것
        // String인 sample의 데이터 값을 소문자로 만들기
        result = sample.toLowerCase() ;
        System.out.println("소문자 : " + result);

        // String인 sample의 데이터 값을 대문자로 만들기
        result = sample.toUpperCase() ;
        System.out.println("대문자 : " + result);

        // String인 sample의 데이터 값에서 특정 문자가 들어가있는지 확인하기
        // 어떠한 문장이 들어가있으면 ~~하시오 같이 나중에 if나 다중if문장에서 활용
        boolean bool = sample.contains("hello");
        System.out.println("hello 존재 여부 : " + bool);

        // String인 sample의 데이터 값이 특정 문자로 시작하는지 확인하기
        // if문장에 조건식에서 활용
        bool = sample.startsWith("hello");
        System.out.println("hello로 시작 여부 : " + bool);

        // String인 sample의 데이터 값이 특정 문자로 끝나는지 확인하기
        // if문장에 조건식에서 활용
        bool = sample.endsWith("world");
        System.out.println("world로 끝이 납니까? : " + bool);

        // String인 데이터 그 자체를 가지고 길이를 측정하기
        int size = "Bearer ".length() ;
        System.out.println("문자열 길이 : " + size);

        // String은 타입이기도 하지만 기본으로 제공되는 클래스이기도 함
        // format() 메소드는 static메소드여서 객체없이 바로 사용 가능
        result = String.format("%03d", size);
        System.out.println("서식 지정 : " + result);

        // String인 sample의 데이터 값에서 특정 인덱스부분부터 추출하기
        // 전체 긴 문자열에서 일부분을 가져올때 (데이터 분석시)
        result = sample.substring(7);
        System.out.println("추출 결과 01 : " + result);

        // String인 sample의 데이터 값에서 특정 인덱스부분부터 추출하기 (변수 이용)
        // 전체 긴 문자열에서 일부분을 가져올때 (데이터 분석시)
        result = sample.substring(size);
        System.out.println("추출 결과 01-1 : " + result);

        // String인 sample의 데이터 값에서 특정 인덱스부분부터 특정 인덱스부분까지 추출하기
        // 전체 긴 문자열에서 일부분을 가져올때 (데이터 분석시)
        // Java에서 숫자가 2개가 나오면 앞에 숫자는 포함되지만 뒤에 숫자는 포함안됨
        // "hello"에서 h의 인덱스가 7인데 앞에꺼는 포함됨 -> 따라서 7을 써줘야 함
        // "hello"에서 o의 인덱스가 11인데 뒤에꺼 포함안됨 -> 따라서 12를 써줘야 함
        result = sample.substring(7, 12);
        System.out.println("추출 결과 02 : " + result);

        // String인 result의 데이터 값에서 특정 문자열을 다른 문자열로 대체하기
        result = sample.replace("hello", "bluesky");
        System.out.println("치환 : " + result);
    }
}
