package com.coffee.controller;

import com.coffee.entity.Product;
import com.coffee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService ;

    @GetMapping("/list")
    public List<Product> list(){
        List<Product> products = this.productService.getProductList() ;
        return products ;
    }

    // {id}를 경로 변수라고 부르며, 가변 매개 변수라고 부름
    // 상품 50번 누르면 id가 50으로 바뀌듯 그때그때 상황에 따라 바뀜
    // 경로변수를 delete() 안에 넣고 @PathVariable이라는 경로변수 어노테이션도 작성하면
    // 매핑에 있는 경로 변수 id가 delete의 id로 들어옴
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        try {
            boolean isDeleted = this.productService.deleteProduct(id);

            if(isDeleted){
                // 여기까지 왔다는 것은 에러없이 성공해서 내가 원하는 동작인 삭제가 된 것임
                return ResponseEntity.ok(id + "번 상품이 삭제되었습니다.");
            }else{ // 삭제를 할 상품 자체가 없는 상태
                return ResponseEntity.badRequest().body(id + "번 상품이 존재하지 않습니다.");
            }

        }catch (DataIntegrityViolationException err){// 데이터 베이스 무결성 위배되는 에러가 발생할때
            String message = "해당 상품은 장바구니에 포함이 되어 있거나, 이미 매출이 발생한 상품입니다. \n확인해 주세요." ;
            return ResponseEntity.internalServerError().body(message);

        }catch (Exception err){ // 두루뭉실한 예외처리
            return ResponseEntity.internalServerError().body("오류 발생 : " + err.getMessage());

        }
    }
}
