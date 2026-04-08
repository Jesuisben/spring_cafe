package com.coffee.test;

import java.io.File;

public class FileTest {
    public static void main(String[] args) {
        // 운영체제의 폴더 구분자는 역슬래쉬 (\)
        // 역슬래쉬는 특수한 문자여서 2개를 적어야 함
        // 원래 역슬래쉬하고 n이나 다른 문자를 써서 특수한 문자를 만들어서
        // 사실상 역슬래쉬 하나는 특수한 문자를 만드는 문법이고 진짜 문자?인 역슬래쉬는 두번째 역슬래쉬

        // 리눅스의 폴더 구분자는 슬래쉬 (/)

        // 지역 변수에는 접근지정자를 붙일 수 없어서 private을 못 넣음
        final String imageFolder = "c:\\shop\\images";

        // 이미지가 들어간 파일이 들어간 폴더를 지칭하는 folder 객체
        File folder = new File(imageFolder);

        // folder의 존재 여부 조건식
        // File 클래스로 객체를 만들면 객체는 무조건 생성되는데 실제로 존재하는지는 확인해야함
        if(folder.exists()){
            // is로 시작하는 메소드들의 반환타입은 대부분 boolean 타입임
            if(folder.isDirectory()){
                System.out.println("폴더");

                File[] imageList = folder.listFiles();

                // 확장 for => for(타입 단수 : 복수){}
                for(File one : imageList){
                    if (one.isFile()){
                        // String 클래스 이용하기 (04.07(화).txt확인)
                        if (one.getName().endsWith(".jpg")){
                            // 이렇게 하면 확장자도 다 출력됨
                            System.out.println(one.getName());

                            // 확장자 제거하고 파일 이름만 출력하기
                            // indexof는 특정 문자가 있는 인덱스의 위치를 알려줌
                            // int end_index = one.getName().indexOf(".") ;

                            // 만약 파일명에 "."이 있을때 lastindexof() 사용하면 됨
                            int end_index = one.getName().lastIndexOf(".") ;
                            String filename = one.getName().substring(0, end_index) ;
                            System.out.println(filename);

                            // 확장자만 가져올때
                            String extension = one.getName().substring(end_index + 1) ;
                            System.out.println(extension);
                        }
                    }
                }
            }else{
                System.out.println("파일");
            }
        }else{
            System.out.println("존재하지 않는 항목입니다.");
        }
    }
}
