package com.coffee.service;

import com.coffee.entity.Product;
import com.coffee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository ;

    public List<Product> getProductList(){
        return this.productRepository.findProductByOrderByIdDesc();
    }

    // application.properties에서 이미지가 있는 실제 위치 가져오기
    @Value("${productImageLocation}")
    private String productImageLocation ;

    // 상품 id를 이용한 삭제
    // 1) 삭제할 상품이 데이터 베이스에 실제로 존재하는지 id를 통해 확인
    // 2) 운영체제에서 상품 삭제
    // 3) 상품 자체를 삭제

    // 1) 상품이 존재 하는지 확인
    public boolean deleteProduct(Long id){
        // 상품 id가져오는데 없으면 없네~하고싶은데 오류가 나니까 orElse넣기
        Product product = productRepository.findById(id).orElse(null) ;

        // 상품이 무의미하면, 없다면
        if(product == null){
            return false ;
        }

        // 상품이 유의미하면, 있다면
        // 2) 이미지가 있는 c드라이브 폴더(운영체제 내)에 가서 이미지 지우기
        String fileName = product.getImage() ;
        if(fileName != null && !fileName.isEmpty()){
            // application.properties에서 가져온 이미지 위치 연결하기
            File file = new File(productImageLocation + fileName) ;

            System.out.println("삭제될 파일 이름");
            System.out.println(file.getAbsolutePath()); // 절대 경로 보여주기

            if(file.exists()){
              boolean deleted = file.delete() ;

              if(!deleted){
                  System.out.println("이미지 삭제 실패");
              }
            }
        }
        // 3) 데이터 베이스에서 상품 삭제하기
        productRepository.deleteById(id);
        return true;
    }
}
