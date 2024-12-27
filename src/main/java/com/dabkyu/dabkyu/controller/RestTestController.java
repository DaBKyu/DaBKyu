package com.dabkyu.dabkyu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import com.dabkyu.dabkyu.dto.Category1DTO;
import com.dabkyu.dabkyu.dto.Category2DTO;
import com.dabkyu.dabkyu.dto.Category3DTO;
import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.dto.DailySalesDTO;
import com.dabkyu.dabkyu.dto.DailyVisitorDTO;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.dto.ProductInfoFileDTO;
import com.dabkyu.dabkyu.dto.ProductOptionDTO;
import com.dabkyu.dabkyu.dto.RelatedProductDTO;
import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.CouponCategoryEntity;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.CouponTargetEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductFileEntity;
import com.dabkyu.dabkyu.entity.ProductInfoFileEntity;
import com.dabkyu.dabkyu.entity.ProductOptionEntity;
import com.dabkyu.dabkyu.service.MasterService;
import com.dabkyu.dabkyu.service.ProductService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") 
public class RestTestController{

    private final MasterService masterService; 
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    //관리자페이지 메인 오늘 방문자 수
    @GetMapping("/master/visitorCount")
    public ResponseEntity<Integer> getVisitorCount() {
        int visitorCount = masterService.getTodayVisitorCount();
        return ResponseEntity.ok(visitorCount);
    }

    // 관리자페이지 메인 오늘 리뷰 수
    @GetMapping("/master/reviewCount")
    public ResponseEntity<Integer> getReviewCount() {
        int reviewCount = masterService.getTodayReviewCount(); 
        return ResponseEntity.ok(reviewCount);
    }

    // 관리자페이지 메인 오늘 문의 수
    @GetMapping("/master/questionCount")
    public ResponseEntity<Integer> getQuestionCount() {
        int questionCount = masterService.getTodayQuestionCount(); 
        return ResponseEntity.ok(questionCount);
    }

    //관리자페이지 메인 답변 대기수 
    @GetMapping("/master/pendingQuestions")
    public ResponseEntity<Integer> getPendingQuestions() {
        int pendingQuestions = masterService.getNumberOfPendingQuestions(); 
        return ResponseEntity.ok(pendingQuestions);
    }

    //관리자페이지 메인 주문 접수 건 수
    //관리자페이지 메인 주문 취소요청 건 수
    //관리자페이지 메인 주문 환불요청 건 수
    //관리자페이지 메인 주문 배송완료 건 수

    //일별 매출 데이터
    @GetMapping("/master/salesByDailyData")
    public ResponseEntity<List<DailySalesDTO>> getDailySales(
            @RequestParam("startDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam("endDateTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {

        List<DailySalesDTO> dailySalesData = masterService.getDailySales(startDateTime, endDateTime);

        if (dailySalesData.isEmpty()) {
            // 데이터가 없을 경우, 상태 코드 404와 함께 빈 리스트를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dailySalesData);
        }

        // 데이터를 정상적으로 가져온 경우 200 OK와 함께 데이터를 반환
        return ResponseEntity.status(HttpStatus.OK).body(dailySalesData);
    }

    //일별 방문자 통계 페이지
    @GetMapping("/master/visitorsByDailyData")
    public ResponseEntity<List<DailyVisitorDTO>> getDailyVisitors(
        @RequestParam("startDateTime")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
        @RequestParam("endDateTime") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        
        List<DailyVisitorDTO> dailyVisitorsData = masterService.getDailyVisitors(startDateTime, endDateTime);

        if (dailyVisitorsData.isEmpty()) {
            // 데이터가 없을 경우, 상태 코드 404와 함께 빈 리스트를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dailyVisitorsData);
        }

        // 데이터를 정상적으로 가져온 경우 200 OK와 함께 데이터를 반환
        return ResponseEntity.status(HttpStatus.OK).body(dailyVisitorsData);
    }

    //상품 리스트 조회
    @GetMapping("/master/allProducts")
    public ResponseEntity<Map<String, Object>> getAllProducts(
        HttpSession session
    ) throws Exception {
        // 카테고리 목록 조회
        List<Category1Entity> list = productService.category1List();
        List<Category2Entity> list2 = productService.category2List();
        List<Category3Entity> list3 = productService.category3List();

        // 전체 상품 목록 조회
        List<ProductEntity> allProducts = productService.getAllProducts();

        // 상품 DTO로 변환
        List<ProductDTO> productList = new ArrayList<>();
        for (ProductEntity productEntity : allProducts) {
            ProductDTO productDTO = new ProductDTO(productEntity);
        String thumbnail = productService.getProductThumbnail(productEntity);
            productDTO.setThumbnail(thumbnail);
            //log.info("--------------------thumbnail: {}-----------------------", thumbnail);
            productList.add(productDTO);
        }

        // 응답을 위한 Map 구성
        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("list2", list2);
        response.put("list3", list3);
        response.put("product", productList);

        // ResponseEntity로 응답 반환
        return ResponseEntity.ok(response); // 200 OK 상태 코드로 응답
    }

    //상품 상세 조회
    @GetMapping("/master/productDetail/{productSeqno}")
    public ResponseEntity<Map<String, Object>> getProductDetail(@PathVariable Long productSeqno) throws Exception {
        // 응답 데이터를 담을 Map 생성
        Map<String, Object> response = new HashMap<>();

        // 상품 상세 정보를 가져와 Map에 추가
        ProductEntity productEntity = masterService.getProductBySeqno(productSeqno);
        response.put("productview", productEntity); // 상품 정보
        response.put("productfileview", masterService.getProductFiles(productSeqno)); // 상품 파일 정보
        response.put("productinfofileview", masterService.getProductInfoFiles(productSeqno)); // 상품설명 이미지 파일 정보
        response.put("optionview", masterService.getProductOptions(productSeqno)); // 상품 옵션 정보
        response.put("relatedproductview", masterService.getRelatedProducts(productSeqno)); // 추가 상품 정보

        // ResponseEntity를 사용해 응답 반환
        return ResponseEntity.ok(response);
    }

    @GetMapping("/master/getMainCategories")
    public ResponseEntity<List<Category1DTO>> getMainCategories() {
       
        List<Category1Entity> category1List = masterService.getCategories1();
        
        // Category1DTO 목록으로 변환
        List<Category1DTO> category1DTOList = category1List.stream()
                                                           .map(category -> new Category1DTO(
                                                                                category.getCategory1Seqno(), 
                                                                                category.getCategory1Name(),
                                                                                category.getCategory1Order()))
                                                           .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.OK).body(category1DTOList);
    }
    

    @GetMapping("/master/getMiddleCategories")
    public ResponseEntity<List<Category2DTO>> getMiddleCategories(@RequestParam("category1Seqno") Long category1Seqno) {
        // Category2Entity 목록을 가져옴
        List<Category2Entity> category2List = masterService.getCategories2ByCategory1(category1Seqno);
        
        // Category2DTO 목록으로 변환
        List<Category2DTO> category2DTOList = category2List.stream()
                                                           .map(category -> new Category2DTO(
                                                                                category.getCategory2Seqno(), 
                                                                                category.getCategory1Seqno().getCategory1Seqno(),
                                                                                category.getCategory2Name(),
                                                                                category.getCategory2Order()))
                                                           .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.OK).body(category2DTOList);
    }
    

    @GetMapping("/master/getSubCategories")
    public ResponseEntity<List<Category3DTO>> getSubCategories(@RequestParam("category2Seqno") Long category2Seqno) {
        // Category3Entity 목록을 가져옴
        List<Category3Entity> category3List = masterService.getCategories3ByCategory2(category2Seqno);
        
        // Category3DTO 목록으로 변환
        List<Category3DTO> category3DTOList = category3List.stream()
                .map(category -> new Category3DTO(
                        category.getCategory3Seqno(),
                        category.getCategory2Seqno().getCategory2Seqno(), 
                        category.getCategory3Name(),
                        category.getIsTemporary(),
                        category.getCategory3Order()))
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.OK).body(category3DTOList);
    }
    

    //상품 등록
    @PostMapping("/master/postProduct")
    public ResponseEntity<?> postFileUpload(ProductDTO productDTO, Model model,
            @RequestParam(name = "category3Seqno", required = false) Long category3Seqno,
            @RequestParam(name = "productImages", required = false) List<MultipartFile> productImages,
            @RequestParam(name = "productDetailImages", required = false) List<MultipartFile> productDetailImages,
            @RequestParam(name = "optionMap", required = false) String optionMapJson,
            @RequestParam(name = "relatedProductMap", required = false) String relatedProductMapJson) {

            List<Category1Entity> allCategory1 = masterService.getAllCategories1();
            List<Category2Entity> allCategory2 = masterService.getAllCategories2();
            List<Category3Entity> allCategory3 = masterService.getAllCategories3();

            model.addAttribute("allcategory1", allCategory1);
            model.addAttribute("allcategory2", allCategory2);
            model.addAttribute("allcategory3", allCategory3);

        try {
            // 운영체제에 따라 이미지 저장 경로 설정
            String os = System.getProperty("os.name").toLowerCase();
            String productImgPath = os.contains("win") 
                    ? "c:\\Repository\\dabkyu\\product\\thumbnails\\" 
                    : "/home/gladius/Repository/dabkyu/product/thumbnails/";
            String productDetailImgPath = os.contains("win") 
                    ? "c:\\Repository\\dabkyu\\product\\images\\" 
                    : "/home/gladius/Repository/dabkyu/product/images/";

            // 디렉토리 확인 및 생성
            File productImgDir = new File(productImgPath);
            if (!productImgDir.exists() && !productImgDir.mkdirs()) {
                throw new IOException("상품 이미지 디렉토리 생성 실패");
            }

            File productDetailImgDir = new File(productDetailImgPath);
            if (!productDetailImgDir.exists() && !productDetailImgDir.mkdirs()) {
                throw new IOException("상품 상세 이미지 디렉토리 생성 실패");
            }

            Long seqno = 0L;

            // 상품 등록 처리
            if (productDTO.getProductSeqno() == null) {
                productDTO.setLikecnt(0);
                Category3Entity category3 = masterService.findCategoryBySeqno(category3Seqno);
                productDTO.setCategory3Seqno(category3);

                ProductEntity productEntity = masterService.productPost(productDTO);
                seqno = productEntity.getProductSeqno();

            // 옵션 저장
            if (optionMapJson != null && !optionMapJson.isEmpty()) {
                // 하나의 옵션 데이터만 저장
                List<Map<String, String>> optionMapList = objectMapper.readValue(optionMapJson, new TypeReference<List<Map<String, String>>>() {});
                for (Map<String, String> option : optionMapList) {
                    String optCategory = option.get("optCategory");
                    String optName = option.get("optName");
                    int optPrice = 0;
                    try {
                        optPrice = Integer.parseInt(option.get("optPrice"));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price: " + option.get("optPrice"));
                    }

                    // 옵션 데이터 하나만 저장
                    ProductOptionDTO optionDTO = new ProductOptionDTO();
                    optionDTO.setProductSeqno(productEntity);
                    optionDTO.setOptCategory(optCategory);
                    optionDTO.setOptName(optName);
                    optionDTO.setOptPrice(optPrice);
                    masterService.saveProductOption(optionDTO);
                }
            }
            //추가상품 저장
            if (relatedProductMapJson != null && !relatedProductMapJson.isEmpty()) {
                    List<Map<String, String>> relatedProductMapList = objectMapper.readValue(relatedProductMapJson, new TypeReference<List<Map<String, String>>>() {});
                    for (Map<String, String> relatedProduct : relatedProductMapList) {
                            String relatedProductCategory = relatedProduct.get("relatedProductCategory");
                            String relatedProductName = relatedProduct.get("relatedProductName");
                            int relatedProductPrice = 0;
                            try {
                                relatedProductPrice = Integer.parseInt(relatedProduct.get("relatedProductPrice"));
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid value: " + relatedProduct.get("relatedProductPrice"));
                            }
                            RelatedProductDTO relatedProductDTO = new RelatedProductDTO();
                            relatedProductDTO.setProductSeqno(productEntity);
                            relatedProductDTO.setRelatedproductCategory(relatedProductCategory);
                            relatedProductDTO.setRelatedproductName(relatedProductName);
                            relatedProductDTO.setRelatedproductPrice(relatedProductPrice);
                            masterService.saveRelatedProduct(relatedProductDTO);
                        }
                    }
                

                // 상품 이미지 저장
                if (productImages != null && !productImages.isEmpty()) {
                    boolean isFirstImage = true;
                    for (MultipartFile file : productImages) {
                        saveFile(file, productImgPath, productEntity, isFirstImage);
                        isFirstImage = false;
                    }
                }

                // 상품 상세 이미지 저장
                if (productDetailImages != null && !productDetailImages.isEmpty()) {
                    System.out.println("상품 상세 이미지 수: " + productDetailImages.size());
                    for (MultipartFile file : productDetailImages) {
                        saveDetailFile(file, productDetailImgPath, productEntity);
                    }
                } else {
                    System.out.println("상품 상세 이미지가 제공되지 않았습니다.");
                }
                
            }
            return ResponseEntity.ok().body(Map.of("status", "success", "productSeqno", seqno));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 등록 중 오류 발생: " + e.getMessage());
        }
    }

    private void saveFile(MultipartFile file, String path, ProductEntity productEntity, boolean isThumb) throws Exception {
        String orgFilename = file.getOriginalFilename();
        String ext = orgFilename.substring(orgFilename.lastIndexOf("."));
        String storedFilename = UUID.randomUUID().toString().replaceAll("-", "") + ext;

        File targetFile = new File(path + storedFilename);
        file.transferTo(targetFile);

        ProductFileDTO fileDTO = ProductFileDTO.builder()
                                                .productSeqno(productEntity)
                                                .orgFilename(orgFilename)
                                                .storedFilename(storedFilename)
                                                .isThumb(isThumb ? "Y" : "N")
                                                .build();
        masterService.productImgFile(fileDTO);
    }

    private void saveDetailFile(MultipartFile file, String path, ProductEntity productEntity) {
        try {
            // 파일 이름 및 확장자 처리
            String orgFilename = file.getOriginalFilename();
            if (orgFilename == null || orgFilename.isEmpty()) {
                throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
            }

            String ext = orgFilename.substring(orgFilename.lastIndexOf("."));
            String storedFilename = UUID.randomUUID().toString().replaceAll("-", "") + ext;

            // 파일 저장 경로 확인 및 저장
            File targetFile = new File(path + storedFilename);
            file.transferTo(targetFile);

            // 파일 정보 DTO 생성 및 저장
            ProductInfoFileDTO detailFileDTO = ProductInfoFileDTO.builder()
                                                                .productSeqno(productEntity)
                                                                .orgFilename(orgFilename)
                                                                .storedFilename(storedFilename)
                                                                .build();
            masterService.productDetailImgFile(detailFileDTO);

            System.out.println("상품 상세 이미지 저장 성공: " + orgFilename);
        } catch (Exception e) {
            // 오류 로그 출력
            System.err.println("상품 상세 이미지 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //상품 수정
    @PutMapping("/master/updateProduct/{productSeqno}")
        public ResponseEntity<?> updateProduct(@PathVariable Long productSeqno, 
                                                ProductDTO productDTO, Model model,
                                                @RequestParam(name = "category3Seqno", required = false) Long category3Seqno,
                                                @RequestParam(name = "productImages", required = false) List<MultipartFile> productImages,
                                                @RequestParam(name = "productDetailImages", required = false) List<MultipartFile> productDetailImages,
                                                @RequestParam(name = "optionMap", required = false) String optionMapJson,
                                                @RequestParam(name = "relatedProductMap", required = false) String relatedProductMapJson,
                                                @RequestParam(name = "deleteOptionSeqnos", required = false) List<Long> deleteOptionSeqnos,
                                                @RequestParam(name = "deleteRelatedProductSeqnos", required = false) List<Long> deleteRelatedProductSeqnos,
                                                @RequestParam(name = "deleteProductImageSeqnos", required = false) List<Long> deleteProductImageSeqnos,
                                                @RequestParam(name = "deleteProductDetailImageSeqnos", required = false) List<Long> deleteProductDetailImageSeqnos
                                                ) {
            List<Category1Entity> allCategory1 = masterService.getAllCategories1();
            List<Category2Entity> allCategory2 = masterService.getAllCategories2();
            List<Category3Entity> allCategory3 = masterService.getAllCategories3();

            model.addAttribute("allcategory1", allCategory1);
            model.addAttribute("allcategory2", allCategory2);
            model.addAttribute("allcategory3", allCategory3);

            try {
                // 운영체제에 따라 이미지 저장 경로 설정
                String os = System.getProperty("os.name").toLowerCase();
                String productImgPath = os.contains("win") 
                        ? "c:\\Repository\\dabkyu\\product\\thumbnails\\" 
                        : "/home/gladius/Repository/dabkyu/product/thumbnails/";
                String productDetailImgPath = os.contains("win") 
                        ? "c:\\Repository\\dabkyu\\product\\images\\" 
                        : "/home/gladius/Repository/dabkyu/product/images/";

                // 디렉토리 확인 및 생성
                File productImgDir = new File(productImgPath);
                if (!productImgDir.exists() && !productImgDir.mkdirs()) {
                    throw new IOException("상품 이미지 디렉토리 생성 실패");
                }

                File productDetailImgDir = new File(productDetailImgPath);
                if (!productDetailImgDir.exists() && !productDetailImgDir.mkdirs()) {
                    throw new IOException("상품 상세 이미지 디렉토리 생성 실패");
                }

                // 상품 정보 업데이트 처리
                ProductEntity existingProduct = masterService.findProductBySeqno(productSeqno);
                if (existingProduct == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
                }

                // 카테고리 업데이트
                if (category3Seqno != null) {
                    Category3Entity category3 = masterService.findCategoryBySeqno(category3Seqno);
                    existingProduct.setCategory3Seqno(category3);
                }

                masterService.productModify(productDTO);
/* 
                // 옵션 삭제
                if (deleteOptionSeqnos != null && !deleteOptionSeqnos.isEmpty()) {
                    for (Long optionSeqno : deleteOptionSeqnos) {
                        ProductOptionEntity productOption = masterService.findProductOptionBySeqno(optionSeqno);
                        if (productOption != null) {
                            masterService.deleteProductOption(productOption);
                        }
                    }
                }

                // 추가상품 삭제
                if (deleteRelatedProductSeqnos != null && !deleteRelatedProductSeqnos.isEmpty()) {
                    for (Long relatedProductSeqno : deleteRelatedProductSeqnos) {
                        RelatedProductDTO relatedProductDTO = masterService.findRelatedProductBySeqno(relatedProductSeqno);
                        if (relatedProductDTO != null) {
                            masterService.deleteRelatedProduct(relatedProductDTO);
                        }
                    }
                }

                // 이미지 삭제
                if (deleteProductImageSeqnos != null && !deleteProductImageSeqnos.isEmpty()) {
                    for (Long imageSeqno : deleteProductImageSeqnos) {
                        ProductFileEntity  productImage = masterService.findProductImageBySeqno(imageSeqno);
                        if (productImage != null) {
                            // 이미지 파일 삭제
                            File productImageFile = new File(productImgPath + productImage.getStoredFilename());
                            if (productImageFile.exists()) {
                                productImageFile.delete();
                            }
                            masterService.deleteProductImage(productImage);
                        }
                    }
                }

                // 상세 이미지 삭제
                if (deleteProductDetailImageSeqnos != null && !deleteProductDetailImageSeqnos.isEmpty()) {
                    for (Long detailImageSeqno : deleteProductDetailImageSeqnos) {
                        ProductInfoFileEntity  productDetailImage = masterService.findProductDetailImageBySeqno(detailImageSeqno);
                        if (productDetailImage != null) {
                            // 상세 이미지 파일 삭제
                            File productDetailImageFile = new File(productDetailImgPath + productDetailImage.getStoredFilename());
                            if (productDetailImageFile.exists()) {
                                productDetailImageFile.delete();
                            }
                            masterService.deleteProductDetailImage(productDetailImage);
                        }
                    }
                }
*/
                // 옵션 업데이트
                if (optionMapJson != null && !optionMapJson.isEmpty()) {
                    List<Map<String, String>> optionMapList = objectMapper.readValue(optionMapJson, new TypeReference<List<Map<String, String>>>() {});
                    for (Map<String, String> option : optionMapList) {
                        String optCategory = option.get("optCategory");
                        String optName = option.get("optName");
                        int optPrice = Integer.parseInt(option.get("optPrice"));

                        // 옵션 업데이트 로직
                        ProductOptionDTO optionDTO = new ProductOptionDTO();
                        optionDTO.setProductSeqno(existingProduct);
                        optionDTO.setOptCategory(optCategory);
                        optionDTO.setOptName(optName);
                        optionDTO.setOptPrice(optPrice);
                        masterService.saveProductOption(optionDTO);
                    }
                }

                // 추가상품 업데이트
                if (relatedProductMapJson != null && !relatedProductMapJson.isEmpty()) {
                    List<Map<String, String>> relatedProductMapList = objectMapper.readValue(relatedProductMapJson, new TypeReference<List<Map<String, String>>>() {});
                    for (Map<String, String> relatedProduct : relatedProductMapList) {
                        String relatedProductCategory = relatedProduct.get("relatedProductCategory");
                        String relatedProductName = relatedProduct.get("relatedProductName");
                        int relatedProductPrice = Integer.parseInt(relatedProduct.get("relatedProductPrice"));

                        // 추가상품 업데이트 로직
                        RelatedProductDTO relatedProductDTO = new RelatedProductDTO();
                        relatedProductDTO.setProductSeqno(existingProduct);
                        relatedProductDTO.setRelatedproductCategory(relatedProductCategory);
                        relatedProductDTO.setRelatedproductName(relatedProductName);
                        relatedProductDTO.setRelatedproductPrice(relatedProductPrice);
                        masterService.saveRelatedProduct(relatedProductDTO);
                    }
                }

                // 이미지 업데이트
                if (productImages != null && !productImages.isEmpty()) {
                    for (MultipartFile file : productImages) {
                        // 새로운 상품 이미지 저장
                        saveFile(file, productImgPath, existingProduct, true);
                    }
                }

                // 상세 이미지 업데이트
                if (productDetailImages != null && !productDetailImages.isEmpty()) {
                    for (MultipartFile file : productDetailImages) {
                        // 새로운 상품 상세 이미지 저장
                        saveDetailFile(file, productDetailImgPath, existingProduct);
                    }
                }

                return ResponseEntity.ok().body(Map.of("status", "success", "productSeqno", productSeqno));

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 수정 중 오류 발생: " + e.getMessage());
            }
        }
    
    //카테고리 리스트 조회
    @GetMapping("/master/categoryList")
    public ResponseEntity<Map<String, Object>> getManageCategory() {
        // 엔티티를 DTO로 변환하여 전달
        List<Category1DTO> category1DTOs = masterService.getAllCategories1().stream()
            .map(entity -> new Category1DTO(entity.getCategory1Seqno(), entity.getCategory1Name(), entity.getCategory1Order()))
            .collect(Collectors.toList());
    
        List<Category2DTO> category2DTOs = masterService.getAllCategories2().stream()
            .map(entity -> new Category2DTO(entity.getCategory2Seqno(), entity.getCategory1Seqno().getCategory1Seqno(), entity.getCategory2Name(),entity.getCategory2Order()))
            .collect(Collectors.toList());
    
        List<Category3DTO> category3DTOs = masterService.getAllCategories3().stream()
            .map(entity -> new Category3DTO(entity.getCategory3Seqno(), entity.getCategory2Seqno().getCategory2Seqno(), entity.getCategory3Name(),entity.getIsTemporary(),entity.getCategory3Order()))
            .collect(Collectors.toList());
    
        Map<String, Object> response = new HashMap<>();
        response.put("categories1", category1DTOs);
        response.put("categories2", category2DTOs);
        response.put("categories3", category3DTOs);
    
        return ResponseEntity.ok(response);
    }
    // 카테고리 추가
    // 카테고리 1 추가
    @PostMapping("/master/category1")
    public ResponseEntity<Map<String, Object>> addCategory1(@RequestParam("category1Name") String category1Name) {

        // 가장 큰 category1Order 조회
        Integer maxOrder = masterService.findMaxCategory1Order();
        int category1Order = (maxOrder == null) ? 1 : maxOrder + 1;

        Category1Entity entity = new Category1Entity();
        entity.setCategory1Name(category1Name);
        entity.setCategory1Order(category1Order);

        //카테고리 저장
        masterService.saveCategory1(entity);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "카테고리1이 추가되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 카테고리 2 추가
    @PostMapping("/master/category2")
public ResponseEntity<Map<String, Object>> addCategory2(@RequestBody Category2DTO category2dto) {
    // Category1Entity 조회
    Category1Entity category1 = masterService.findCategory1ById(category2dto.getCategory1Seqno());
    if (category1 == null) {
        // Category1Entity가 없을 경우 예외 처리
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("message", "Category 1 not found for the given id.")
        );
    }

    // category1Seqno가 같은 항목 중 가장 큰 category2Order 조회
    Integer maxOrder = masterService.findMaxCategory2OrderByCategory1(category1.getCategory1Seqno());
    int category2Order = (maxOrder == null) ? 1 : maxOrder + 1;

    // Category2Entity 생성 및 설정
    Category2Entity entity = new Category2Entity();
    entity.setCategory1Seqno(category1);
    entity.setCategory2Name(category2dto.getCategory2Name());
    entity.setCategory2Order(category2Order);

    // 중간 카테고리 저장
    masterService.saveCategory2(entity);

    // 응답 데이터 구성
    Map<String, Object> response = new HashMap<>();
    response.put("message", "카테고리2가 추가되었습니다.");
    response.put("category2Id", entity.getCategory2Seqno()); // ID 추가

    return ResponseEntity.ok(response);
}

    // 카테고리 3 추가
    @PostMapping("/master/category3")
    public ResponseEntity<Map<String, Object>> addCategory3(@RequestBody Category3DTO category3dto) {
        // Category1Entity 조회
        Category2Entity category2 = masterService.findCategory2ById(category3dto.getCategory2Seqno());
        if (category2 == null) {
            // Category1Entity가 없을 경우 예외 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Category 2 not found for the given id.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
         // category2Seqno가 같은 항목 중 가장 큰 category3Order 조회
        Integer maxOrder = masterService.findMaxCategory3OrderByCategory2(category2.getCategory2Seqno());
        int category3Order = (maxOrder == null) ? 1 : maxOrder + 1;

        Category3Entity entity = new Category3Entity();
        entity.setCategory2Seqno(category2);
        entity.setCategory3Name(category3dto.getCategory3Name());
        entity.setCategory3Order(category3Order);
        
        masterService.saveCategory3(entity);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "카테고리3이 추가되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 카테고리 삭제
    @DeleteMapping("/master/category/{level}/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable int level,
            @PathVariable Long categoryId) {
        try {

             // 카테고리 레벨에 따른 분기
            if (categoryId == null || categoryId <= 0) {
                return ResponseEntity.badRequest().body("유효하지 않은 카테고리 ID입니다.");
            }
            switch (level) {
                case 1:
                    masterService.deleteCategory1(categoryId);
                    return ResponseEntity.ok("카테고리 1이 삭제되었습니다.");
                case 2:
                    masterService.deleteCategory2(categoryId);
                    return ResponseEntity.ok("카테고리 2가 삭제되었습니다.");
                case 3:
                    masterService.deleteCategory3(categoryId);
                    return ResponseEntity.ok("카테고리 3이 삭제되었습니다.");
                default:
                    return ResponseEntity.badRequest().body("잘못된 카테고리 레벨입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("카테고리 삭제 중 오류가 발생했습니다.");
        }
    }
    
    // 카테고리명 수정
    @PutMapping("/master/category/{level}/{categoryId}")
    public ResponseEntity<Map<String, Object>> updateCategory(
            @PathVariable("level") int level,
            @PathVariable("categoryId") Long categoryId,
            @RequestParam("categoryName") String categoryName) {

        try {
            Map<String, Object> response = new HashMap<>();

            switch (level) {
                case 1:
                    // 카테고리 1 수정
                    Category1Entity category1 = masterService.findCategory1ById(categoryId);
                    if (category1 == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", "해당 카테고리1을 찾을 수 없습니다."));
                    }
                    category1.setCategory1Name(categoryName);
                    masterService.saveCategory1(category1);
                    response.put("message", "카테고리1이 수정되었습니다.");
                    break;

                case 2:
                    // 카테고리 2 수정
                    Category2Entity category2 = masterService.findCategory2ById(categoryId);
                    if (category2 == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", "해당 카테고리2를 찾을 수 없습니다."));
                    }
                    category2.setCategory2Name(categoryName);
                    masterService.saveCategory2(category2);
                    response.put("message", "카테고리2가 수정되었습니다.");
                    break;

                case 3:
                    // 카테고리 3 수정
                    Category3Entity category3 = masterService.findCategory3ById(categoryId);
                    if (category3 == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", "해당 카테고리3을 찾을 수 없습니다."));
                    }
                    category3.setCategory3Name(categoryName);
                    masterService.saveCategory3(category3);
                    response.put("message", "카테고리3이 수정되었습니다.");
                    break;

                default:
                    return ResponseEntity.badRequest()
                            .body(Map.of("message", "유효하지 않은 카테고리 레벨입니다."));
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "카테고리 수정 중 오류가 발생했습니다."));
        }
    }

    // 카테고리 순서 변경
    @PutMapping("/master/category/order/{level}/{categoryId}")
    public ResponseEntity<String> updateCategoryOrder(@PathVariable int level, @PathVariable Long categoryId, 
    @RequestParam("direction") String direction) {
        
        masterService.updateCategoryOrder(level, categoryId, direction);
     
        return ResponseEntity.ok( "카테고리 순서가 변경되었습니다.");
    }

    // 쿠폰 리스트 조회
    @GetMapping("/master/couponList")
    public ResponseEntity<Map<String, Object>> getCouponList() {
        List<CouponEntity> coupons = masterService.getAllCoupons();
        // 결과 반환
        Map<String, Object> response = new HashMap<>();
        response.put("coupons", coupons);
        return ResponseEntity.ok(response);
    }

    // 쿠폰 상세 조회
    @GetMapping("/master/couponDetail/{couponSeqno}")
    public ResponseEntity<Map<String, Object>> getCouponDetail(@PathVariable Long couponSeqno) {
        Map<String, Object> couponDetails = masterService.getAllCouponDetail(couponSeqno);
        
        // 결과 반환
        Map<String, Object> response = new HashMap<>();
        response.put("coupons", couponDetails);
        return ResponseEntity.ok(response);
    }

    // 쿠폰 생성
    @PostMapping("/master/createCoupon")
    public ResponseEntity<String> createCoupon(@RequestBody CouponDTO couponDTO, @RequestParam(required = false) List<Long> category3Seqnos,
    @RequestParam(required = false) List<Long> productSeqnos) throws Exception {

        Long couponSeqno = 0L;
        // 쿠폰 번호가 없으면 새로 생성
        if (couponDTO.getCouponSeqno() == null) {
            couponSeqno = masterService.writeCoupon(couponDTO);
            couponDTO.setCouponSeqno(couponSeqno);

            // 카테고리 정보 처리 (여러 개)
            if (category3Seqnos != null) {
                for (Long categorySeqno : category3Seqnos) {
                    //카테고리 3 찾기
                    Category3Entity category3Entity = masterService.findCategory3BySeqno(categorySeqno);
                    //쿠폰 찾기
                    CouponEntity couponEntity = masterService.findCouponBySeqno(couponSeqno);
                    //카테고리 정보 저장
                    masterService.saveCouponCategories(category3Entity,couponEntity); 
                }
            }

            // 대상 제품 정보 처리 (여러 개)
            if (productSeqnos != null) {
                for (Long productSeqno : productSeqnos) {
                    //상품 찾기
                    ProductEntity productEntity = masterService.findProductBySeqno(productSeqno);
                    //쿠폰 찾기
                    CouponEntity couponEntity = masterService.findCouponBySeqno(couponSeqno);
                    // 대상 제품 정보 저장
                    masterService.saveCouponTargets(productEntity, couponEntity);
                    
                }
            }
        }
      return ResponseEntity.ok( "쿠폰이 성공적으로 생성되었습니다.");
    }

    // 쿠폰 수정
    @PutMapping("/master/updateCoupon")
    public ResponseEntity<String> updateCoupon(
            @RequestBody CouponDTO couponDTO,
            @RequestParam(required = false) List<Long> category3Seqnos,
            @RequestParam(required = false) List<Long> productSeqnos) throws Exception {

        // 기존 쿠폰 번호가 없으면 에러 반환
        if (couponDTO.getCouponSeqno() == null) {
            return ResponseEntity.badRequest().body("수정할 쿠폰 번호가 없습니다.");
        }

        // 쿠폰 존재 여부 확인
        CouponEntity coupon = masterService.findCouponBySeqno(couponDTO.getCouponSeqno());
        if (coupon == null) {
            return ResponseEntity.badRequest().body("해당 쿠폰을 찾을 수 없습니다.");
        }

        // 쿠폰 정보 수정
        masterService.modifyCoupon(couponDTO);

        // 카테고리 정보 수정
        if (category3Seqnos != null) {
            updateCouponCategories(coupon, category3Seqnos);
        }

        // 대상 제품 정보 수정
        if (productSeqnos != null) {
            updateCouponProducts(coupon, productSeqnos);
        }

        return ResponseEntity.ok("쿠폰이 성공적으로 수정되었습니다.");
    }

    private void updateCouponCategories(CouponEntity coupon, List<Long> category3Seqnos) {
        // 추가할 카테고리 처리
        if (category3Seqnos != null) {
            // 기존 카테고리 삭제
            masterService.deleteCouponCategories(coupon.getCouponSeqno());
            for (Long categorySeqno : category3Seqnos) {
                Category3Entity category3Entity = masterService.findCategory3BySeqno(categorySeqno);
                if (category3Entity == null) {
                    throw new IllegalArgumentException("유효하지 않은 카테고리 번호: " + categorySeqno);
                }
                masterService.saveCouponCategories(category3Entity, coupon);
            }
        }
    }

    private void updateCouponProducts(CouponEntity coupon, List<Long> productSeqnos) {
        // 추가할 대상 제품 처리
        if (productSeqnos != null) {
            // 기존 제품 삭제
            masterService.deleteCouponProducts(coupon.getCouponSeqno());
            for (Long productSeqno : productSeqnos) {
                ProductEntity productEntity = masterService.findProductBySeqno(productSeqno);
                if (productEntity == null) {
                    throw new IllegalArgumentException("유효하지 않은 제품 번호: " + productSeqno);
                }
                masterService.saveCouponTargets(productEntity, coupon);
            }
        }
    }

    //쿠폰 배포
    @PostMapping("/master/distributionCoupon")
    public ResponseEntity<String> clientCoupon(
            @RequestParam("couponSeqno") Long couponSeqno,
            @RequestParam(name = "isAllMembers", required = false) boolean isAllMembers,
            @RequestParam(name = "memberGrade", required = false) String memberGrade,
            @RequestParam(name = "isBirthday", required = false) boolean isBirthday,
            @RequestParam(name = "isNewMember", required = false) boolean isNewMember,
            @RequestParam(name = "isFirstOrderMember", required = false) boolean isFirstOrderMember,
            @RequestParam(name = "isNoOrdersLastYearMember", required = false) boolean isNoOrdersLastYearMember) {
        try {
            // 쿠폰 배포 처리
            masterService.couponToUser(couponSeqno, isAllMembers, memberGrade, isBirthday, isNewMember, isFirstOrderMember, isNoOrdersLastYearMember);
            // 성공 응답
            return ResponseEntity.ok("쿠폰이 성공적으로 배포되었습니다.");
        } catch (Exception e) {
            // 에러 처리 및 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("쿠폰 배포 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 쿠폰 비활성화(회원쿠폰 비활성화)

    // 관리자가 선택한 쿠폰을 isExpired를 "Y"로 업데이트해서 만료처리
    @PutMapping("/master/deactivateCoupon/{couponSeqno}")
    public ResponseEntity<String> deactivateCoupon(@PathVariable Long couponSeqno) {

        masterService.markCouponAsInactive(couponSeqno);

        return ResponseEntity.ok("선택한 쿠폰이 성공적으로 만료 처리되었습니다.");
    }

    // 관리자가 선택한 여러 쿠폰을 isExpired를 "Y"로 업데이트해서 만료처리
    @PutMapping("/master/deactivateCoupons")
    public ResponseEntity<Map<String, String>> deactivateCoupons(@RequestBody List<Long> couponSeqnos) {

        masterService.markMultipleCouponsAsInactive(couponSeqnos);
        Map<String, String> response = new HashMap<>();
        response.put("message", "선택한 쿠폰이 만료 처리되었습니다.");

        return ResponseEntity.ok(response);
    }


    // 관리자가 쿠폰 종료일이 지난 쿠폰들을 isExpired를 "Y"로 업데이트해서 만료처리
    @PutMapping("/master/expiredUpdate")
    public ResponseEntity<String> updateExpiredCoupons() {
        LocalDateTime referenceDate = LocalDateTime.now();

        // 만료된 쿠폰을 처리하는 서비스 메서드 호출
        masterService.setExpiredCouponsToExpired(referenceDate);

        // 성공적인 처리 후 상태 코드 200 반환
        return ResponseEntity.ok("종료일이 지난 쿠폰들이 성공적으로 만료 처리되었습니다.");
    }


    // 회원 리스트 조회
    @GetMapping("/master/memberList")
    public ResponseEntity<Map<String, Object>> getMemberList() {
        List<MemberEntity> members = masterService.getAllMembers();
        // 결과 반환
        Map<String, Object> response = new HashMap<>();
        response.put("members", members);
        return ResponseEntity.ok(response);
    }

    // 회원 상세 보기
    @GetMapping("/master/memberDetail/{email}")
    public ResponseEntity<Map<String, Object>> getMemberDetail(@PathVariable String email) {
        Map<String, Object> memberDetails = masterService.getAllMemberDetail(email);
        
        // 결과 반환
        Map<String, Object> response = new HashMap<>();
        response.put("member", memberDetails);
        return ResponseEntity.ok(response);
    } 

    // 회원 등급 수정
    @PutMapping("/master/updateGrade/{email}")
    public ResponseEntity<String> updateMemberGrade(@PathVariable("email") String email, @RequestBody MemberDTO memberDTO) {
        try {
            masterService.updateMemberGrade(email, memberDTO.getMemberGrade());
            return ResponseEntity.ok("회원 등급이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("회원 등급 수정에 실패하였습니다: " + e.getMessage());
        }
    }

   // 회원 활성화여부 수정 
    @PutMapping("/master/changeMemberActive/{email}")
    public ResponseEntity<String> changeMemberActivityStatus(@PathVariable("email") String email, @RequestBody MemberDTO memberDTO) {
        try {
            // 회원 활성화 여부를 수정
            masterService.changeMemberActive(email, memberDTO.getIsActive());

            // 수정이 성공적으로 이루어진 경우
            return ResponseEntity.ok("회원 활성화 여부가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            // 오류 발생 시
            return ResponseEntity.status(400).body("회원 활성화 여부 수정에 실패하였습니다: " + e.getMessage());
        }
    }


    //주문관리
    //주문내역 조회
    @GetMapping("/master/orderList")
    public ResponseEntity<Map<String, Object>> getOrderList() {
        List<OrderInfoEntity> orders = masterService.getAllOrders();
        // 결과 반환
        Map<String, Object> response = new HashMap<>();
        response.put("orders", orders);
        return ResponseEntity.ok(response);
    }

    //주문 상세 조회
    @GetMapping("/master/orderDetail/{orderSeqno}")
    public ResponseEntity<Map<String, Object>> getOrderDetail(@PathVariable Long orderSeqno) {
        Map<String, Object> orderInfoDetail = masterService.getAllOrderDetail(orderSeqno);
        
        // 결과 반환
        Map<String, Object> response = new HashMap<>();
        response.put("orderInfoDetail", orderInfoDetail);
        return ResponseEntity.ok(response);
    } 


    //주문 상태 변경
    
    //주문 취소
    
    //주문 환불



    //문의,리뷰,리뷰신고,

    //메일,통계


   
    }

