package com.coffee.service;

import com.coffee.entity.Product;
import com.coffee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProductList() {
        return this.productRepository.findProductByOrderByIdDesc();
    }

    // application.properties에서 이미지가 있는 실제 위치 가져오기
    @Value("${productImageLocation}")
    private String productImageLocation;

    // 상품 id를 이용한 삭제
    // 1) 삭제할 상품이 데이터 베이스에 실제로 존재하는지 id를 통해 확인
    // 2) 운영체제에서 상품 삭제
    // 3) 상품 자체를 삭제

    // 1) 상품이 존재 하는지 확인
    public boolean deleteProduct(Long id) {
        // 상품 id가져오는데 없으면 없네~하고싶은데 오류가 나니까 orElse넣기
        Product product = productRepository.findById(id).orElse(null);

        // 상품이 무의미하면, 없다면
        if (product == null) {
            return false;
        }

        // 상품이 유의미하면, 있다면
        // 2) 이미지가 있는 c드라이브 폴더(운영체제 내)에 가서 이미지 지우기
        String fileName = product.getImage();
        if (fileName != null && !fileName.isEmpty()) {
            // application.properties에서 가져온 이미지 위치 연결하기
            File file = new File(productImageLocation + fileName);

            System.out.println("삭제될 파일 이름");
            System.out.println(file.getAbsolutePath()); // 절대 경로 보여주기

            if (file.exists()) {
                boolean deleted = file.delete();

                if (!deleted) {
                    System.out.println("이미지 삭제 실패");
                }
            }
        }
        // 3) 데이터 베이스에서 상품 삭제하기
        productRepository.deleteById(id);
        return true;
    }

    // 리액트에서 const reader = new FileReader();로 이미지를 텍스트로 변환함
    // 리액트에서 이미지를 텍스트로 변환한 그 텍스트가 base64Image로 들어옴
    private String saveProductImage(String base64Image) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedNow = LocalDateTime.now().format(formatter);
        // UUID 클래스 공부 (Universal Unique ID)
        String imageFileName = "product_" + formattedNow + ".jpg";

        File imageFile = new File(productImageLocation + imageFileName);
        System.out.println("등록할 이미지 이름");
        System.out.println(imageFile.getAbsolutePath());

        // byte인 이미지 파일을 문자로 바꾸고 그 문자를 나누고
        byte[] decodedImage = Base64.getDecoder().decode(base64Image.split(",")[1]);

        try {
            // 실제 저장은 byte로 저장함
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(decodedImage); // c:\shop\images 폴더에 복사되는 것처럼 보이지만 write를 씀
            return imageFileName;

        } catch (Exception e) {
            String message = "이미지 파일 저장중에 오류가 발생했습니다.";
            throw new IllegalStateException(message);
        }

    }


    public Product insertProduct(Product product) {
        // product는 리액트에서 넘어온 상품 등록을 위항 정보입니다.
        if (product.getImage() != null && product.getImage().startsWith("data:image")) {
            String imageFileName = this.saveProductImage(product.getImage());

            // 데이터 베이스에는 product_년월일시분초.jpg 형식으로 저장되어야 합니다.
            product.setImage(imageFileName);

            product.setInputdate(LocalDate.now());
            System.out.println("서비스 클래스에서 상품 등록 정보 확인");
            System.out.println(product);

            // 메소드 명은 save인데 실제 데이터 베이스에 추가하니까 사실상 SQL의 insert임
            return this.productRepository.save(product); // 데이터 베이스에 추가하기
        } else {
            return null;
        }


    }

    // 상품 수정 버튼 클릭시 컨트롤러로 갔다가 여기로 옴
    public Product getProductById(Long id) {
        Optional<Product> product = this.productRepository.findById(id);

        return product.orElse(null);
    }

    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    private void deleteOldImage(String oldImageFileName) {
        // oldImageFileName가 무의미하면 return
        if (oldImageFileName == null || oldImageFileName.isBlank()) {
            return;
        }

        // 기존의 이미지 파일 경로 변수를 이용해서 해당 파일의 경로 설정해서 객체 생성
        File oldImageFile = new File(productImageLocation + oldImageFileName);

        // oldImageFile이 존재하면
        if (oldImageFile.exists()) {
            // 삭제하기 (삭제시 deleted는 true)
            boolean deleted = oldImageFile.delete();
            if (!deleted) { // 삭제 실패 (삭제 실패시 deleted는 false) - (!deleted는 true)
                System.out.println("기존 이미지 삭제 실패 : " + oldImageFileName);
            }
        }
    }

    // Product 수정 (읽고와서 쓰기 한 것)
    public Product updateProduct(Product savedProduct, Product updatedProduct) {
        savedProduct.setName(updatedProduct.getName());
        savedProduct.setPrice(updatedProduct.getPrice());
        savedProduct.setCategory(updatedProduct.getCategory());
        savedProduct.setStock(updatedProduct.getStock());
        savedProduct.setDescription(updatedProduct.getDescription());

        if (updatedProduct.getImage() != null && updatedProduct.getImage().startsWith("data:image")) {
            deleteOldImage(savedProduct.getImage());
            String imageFileName = saveProductImage(updatedProduct.getImage());
            savedProduct.setImage(imageFileName);
        }

        return productRepository.save(savedProduct);
    }
    public Optional<Product> findProductById(Long productId){
        return productRepository.findById(productId);
    }

}
