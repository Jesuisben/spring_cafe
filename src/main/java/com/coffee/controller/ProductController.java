package com.coffee.controller;

import com.coffee.constant.Category;
import com.coffee.dto.SearchDto;
import com.coffee.entity.Product;
import com.coffee.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

//    @GetMapping("/list")
//    public List<Product> list() {
//        List<Product> products = this.productService.getProductList();
//        return products;
//    }

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

//    @GetMapping("/list") // 같은 매핑된 주소가 있으면 안됨 -> 예전 매핑된 list주소는 삭제하거나 주석처리
//    public ResponseEntity<Page<Product>> listProducts(
//            // 프론트에서 요구하는 파라미터와 대응되게 변수명을 작성해야 함
//            // ProductList.tsx에 있는 해당 url에 있는 parameters를 똑같이 적음
//            // defaultValue는 해당 파라미터가 속한 것의 .ts에서 찾아서 설정
//            @RequestParam(defaultValue = "0") int pageNumber,
//            @RequestParam(defaultValue = "6") int pageSize
//    ){
//        System.out.println("pageNumber : " + pageNumber + ", pageSize : " + pageSize) ;
//
//        // mysort는 정렬 방식임
//        // by라는 static 메소드 이용
//        Sort mysort = Sort.by(Sort.Direction.DESC, "id");
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, mysort);
//
//        Page<Product> productPage = productService.listProducts(pageable) ;
//
//        System.out.println(productPage.getContent());
//
//        // return하면 해당 url로 요청했던 프론트엔드에 해당 정보를 보내줌
//        return ResponseEntity.ok(productPage) ;
//    }

    @GetMapping("/list") // 같은 매핑된 주소가 있으면 안됨 -> 예전 매핑된 list주소는 삭제하거나 주석처리
    public ResponseEntity<Page<Product>> listProducts(
            // 프론트에서 요구하는 파라미터와 대응되게 변수명을 작성해야 함
            // ProductList.tsx에 있는 해당 url에 있는 parameters를 똑같이 적음
            // defaultValue는 해당 파라미터가 속한 것의 .ts에서 찾아서 설정 (paging.ts)
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "6") int pageSize,

            // defaultValue는 해당 파라미터가 속한 것의 .ts에서 찾아서 설정 (SearchCondition.ts)
            @RequestParam(defaultValue = "all") String searchDateType,
            @RequestParam(defaultValue = "") Category category,
            @RequestParam(defaultValue = "") String searchMode,
            @RequestParam(defaultValue = "") String searchKeyword
    ){
        // 모든 매개변수를 가진 생성자 (@AllArgsConstructor에 의해서)
        SearchDto searchDto = new SearchDto(searchDateType, category, searchMode, searchKeyword);

        Page<Product> productPage = productService.listProducts(searchDto, pageNumber, pageSize) ;

        System.out.println("검색 조건 : " + searchDto);
        System.out.println("총 상품 갯수 : " + productPage.getTotalElements());
        System.out.println("총 페이지 번호 : " + productPage.getTotalPages());
        System.out.println("현재 페이지 번호 : " + productPage.getNumber());

        System.out.println(productPage.getContent());

        // return하면 해당 url로 요청했던 프론트엔드에 해당 정보를 보내줌
        return ResponseEntity.ok(productPage) ;
    }

}
