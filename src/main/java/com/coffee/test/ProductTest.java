package com.coffee.test;

import com.coffee.common.GenerateData;
import com.coffee.entity.Product;
import com.coffee.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProductTest {
    @Autowired
    private ProductRepository productRepository ;

    @Test
    @DisplayName("이미지를 이용한 데이터 추가")
    public void createProductMany(){
        // 특정한 폴더 내에 들어 있는 상품 이미지들을 이용하여 상품 테이블에 추가합니다.
        GenerateData gendata = new GenerateData();

        List<String> imageNameList = gendata.getImageFileNames();
        System.out.println("총 이미지 갯수 : " + imageNameList.size());

        for (int i = 0; i < imageNameList.size(); i++) {
            // bean 객체에 imageNameList의 요소들을 가져와서 넣음
            Product bean = gendata.createProduct(i, imageNameList.get(i));
            // System.out.println(bean);
            this.productRepository.save(bean);
        }


    }
}
