package com.coffee.controller;

import com.coffee.entity.Product;
import com.coffee.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public List<Product> list() {
        List<Product> products = this.productService.getProductList();
        return products;
    }

    // {id}를 경로 변수라고 부르며, 가변 매개 변수라고 부름
    // 상품 50번 누르면 id가 50으로 바뀌듯 그때그때 상황에 따라 바뀜
    // 경로변수를 delete() 안에 넣고 @PathVariable이라는 경로변수 어노테이션도 작성하면
    // 매핑에 있는 경로 변수 id가 delete의 id로 들어옴
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            boolean isDeleted = this.productService.deleteProduct(id);

            if (isDeleted) {
                // 여기까지 왔다는 것은 에러없이 성공해서 내가 원하는 동작인 삭제가 된 것임
                return ResponseEntity.ok(id + "번 상품이 삭제되었습니다.");
            } else { // 삭제를 할 상품 자체가 없는 상태
                return ResponseEntity.badRequest().body(id + "번 상품이 존재하지 않습니다.");
            }

        } catch (DataIntegrityViolationException err) {// 데이터 베이스 무결성 위배되는 에러가 발생할때
            String message = "해당 상품은 장바구니에 포함이 되어 있거나, 이미 매출이 발생한 상품입니다. \n확인해 주세요.";
            return ResponseEntity.internalServerError().body(message);

        } catch (Exception err) { // 두루뭉실한 예외처리
            return ResponseEntity.internalServerError().body("오류 발생 : " + err.getMessage());

        }


    }

    @PostMapping("/insert")
    public ResponseEntity<?> insert(@Valid @RequestBody Product product, BindingResult bindingResult) {
        // 1. 유효성 검사 실패시
        if (bindingResult.hasErrors()) { // bindingResult에 문제가 있으면
            Map<String, String> errors = new HashMap<>();
            for (FieldError xx : bindingResult.getFieldErrors()) {
                errors.put(xx.getField(), xx.getDefaultMessage());
            }

            // 404 Bad Request + 에러 메시지
            return new ResponseEntity<>(
                    Map.of(
                            "message", "상품 등록 유효성 검사에 문제가 있습니다.",
                            "errors", errors
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        try { // 성공하면
            Product savedProduct = this.productService.insertProduct(product);

            if (savedProduct == null) {
                return ResponseEntity
                        .status(500)
                        .body(
                                Map.of(
                                        "message", "상품 등록에 실패하였습니다.",
                                        "error", "bad image file format"
                                )
                        );
            }

            return ResponseEntity.ok(
                    Map.of(
                            "message", "상품이 성공적으로 등록되었습니다.",
                            "image", savedProduct.getImage()
                    )
            );

        } catch (IllegalStateException err) { // 경로 또는 이미지 저장 문제
            return ResponseEntity
                    .status(500)
                    .body(
                            Map.of(
                                    "message", err.getMessage(),
                                    "error", "File Save Error"
                            )
                    );

        } catch (Exception err) { // 데이터 베이스 오류
            return ResponseEntity
                    .status(500)
                    .body(
                            Map.of(
                                    "message", err.getMessage(),
                                    "error", "Internet Server Error"
                            )
                    );

        }
    }

    // ProductUpdateForm.tsx에 customAxios.get()때문에 GetMapping해야함
    @GetMapping("/update/{id}")
    public ResponseEntity<Product> getUpdate(@PathVariable Long id) {
        System.out.println("수정할 상품 번호 : " + id);

        Product product = this.productService.getProductById(id);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else { // 200번대여서 ok
            return ResponseEntity.ok(product);
        }
    }

    // 상품 수정
    @PutMapping("/update/{id}")
    // @PathVariable Long id는 유일한 id가 있음을 표현 - 상품 등록과 수정의 차이점
    public ResponseEntity<?> putUpdate(@PathVariable Long id,
                                       @Valid @RequestBody Product updatedProduct,
                                       BindingResult bindingResult) {
        // 유효성 검사
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError xx : bindingResult.getFieldErrors()) {
                errors.put(xx.getField(), xx.getDefaultMessage());
            }
            return new ResponseEntity<>(
                    Map.of(
                            "message", "상품 수정 유효성 검사에 문제가 있습니다.",
                            "errors", errors
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }

        // 상품 정보 수정
        Optional<Product> findProduct = productService.findById(id);

        if (findProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Product saveProduct = findProduct.get();
            this.productService.updateProduct(saveProduct, updatedProduct);

            return ResponseEntity.ok(Map.of("message", "상품 수정 성공"));

        } catch (Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    Map.of(
                            "message", err.getMessage(),
                            "error", "상품 수정 실패"
                    )
                );
        }


    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Product> detail(@PathVariable Long id){
        Product product = productService.getProductById(id) ;

        if (product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            return ResponseEntity.ok(product);
        }
    }

    @GetMapping("")
    public List<Product> getBigsizeProducts(@RequestParam(required = false) String filter){
        return productService.getProductsByFilter(filter) ;
    }

}
