package com.dabkyu.dabkyu.controller;

import java.io.File;
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
import com.dabkyu.dabkyu.entity.ProductEntity;
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
                                                                                category.getCategory2Name()))
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
                        category.getCategory3Name()))
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.OK).body(category3DTOList);
    }
    

    //상품 등록
    @PostMapping("/master/postProduct")
    public ResponseEntity<?> postFileUpload(ProductDTO productDTO, Model model,
            @RequestParam(name = "category3Seqno", required = false) Long category3Seqno,
            @RequestParam(name = "productImages", required = false) List<MultipartFile> productImages,
            @RequestParam(name = "productDetailImages", required = false) List<MultipartFile> productDetailImages,
            @RequestParam(name = "optionMap", required = false) Map<String, String> optionMap,
            @RequestParam(name = "relatedProductMap", required = false) Map<String, String> relatedProductMap) throws Exception {

        List<Category1Entity> allCategory1 = masterService.getAllCategories1();
        List<Category2Entity> allCategory2 = masterService.getAllCategories2();
        List<Category3Entity> allCategory3 = masterService.getAllCategories3();

        model.addAttribute("allcategory1", allCategory1);
        model.addAttribute("allcategory2", allCategory2);
        model.addAttribute("allcategory3", allCategory3);

        //운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 시작
        String os = System.getProperty("os.name").toLowerCase();
        String productImgPath;
        String productDetailImgPath;

        if (os.contains("win")) {
            productImgPath = "c:\\Repository\\dabkyu\\product\\thumbnails\\";
            productDetailImgPath = "c:\\Repository\\dabkyu\\product\\images\\";
        } else {
            productImgPath = "/home/gladius/Repository/dabkyu/product/thumbnails/";
            productDetailImgPath = "/home/gladius/Repository/dabkyu/product/images/";
        }

        //디렉토리가 존재하는지 체크해서 없다면 생성
        File pImg = new File(productImgPath);
        File pdetailImg = new File(productDetailImgPath);
        if (!pImg.exists()) pImg.mkdir();
        if (!pdetailImg.exists()) pdetailImg.mkdir();
        //운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 종료

        Long seqno = 0L;

        //상품 등록
        if (productDTO.getProductSeqno() == null) {
            productDTO.setLikecnt(0);
            Category3Entity category3 = masterService.findCategoryBySeqno(category3Seqno);
            //세부카테고리(category3) 설정
            productDTO.setCategory3Seqno(category3);
            ProductEntity productEntity = masterService.productPost(productDTO);
            productDTO.setProductSeqno(productEntity.getProductSeqno());
        

            productEntity = masterService.getProductBySeqno(seqno);

        //옵션 추가
        List<ProductOptionDTO> productOptionDTOList = new ArrayList<>();
        if (optionMap != null) {
            for (Map.Entry<String, String> entry : optionMap.entrySet()) {
                String[] keys = entry.getKey().split(",");
                String optCategory = keys[0];
                String optName = keys[1];
                int optPrice = Integer.parseInt(entry.getValue());

                ProductOptionDTO optionDTO = new ProductOptionDTO();
                optionDTO.setProductSeqno(productEntity);
                //옵션카테고리
                optionDTO.setOptCategory(optCategory);
                //옵션명
                optionDTO.setOptName(optName);
                //옵션가격
                optionDTO.setOptPrice(optPrice);
                productOptionDTOList.add(optionDTO);
            }
        }

        //옵션 db저장
        productOptionDTOList.forEach(optionDTO -> {
            try {
                masterService.saveProductOption(optionDTO);
            } catch (Exception e) {
                throw new RuntimeException("옵션 저장 중 오류 발생", e);
            }
        });

        //추가상품 추가
        List<RelatedProductDTO> relatedProductDTOList = new ArrayList<>();
        if (relatedProductMap != null) {
            for (Map.Entry<String, String> entry : relatedProductMap.entrySet()) {
                String[] keys = entry.getKey().split(",");
                String relatedproductCategory = keys[0];
                String relatedproductName = keys[1];
                int relatedproductPrice = Integer.parseInt(entry.getValue());

                RelatedProductDTO relatedProductDTO = new RelatedProductDTO();
                relatedProductDTO.setProductSeqno(productEntity);
                //추가상품카테고리
                relatedProductDTO.setRelatedproductCategory(relatedproductCategory);
                //추가상품명
                relatedProductDTO.setRelatedproductName(relatedproductName);
                //추가상품가격
                relatedProductDTO.setRelatedproductPrice(relatedproductPrice);
                relatedProductDTOList.add(relatedProductDTO);
            }
        }

        //추가상품 db저장
        relatedProductDTOList.forEach(relatedProductDTO -> {
            try {
                masterService.saveRelatedProduct(relatedProductDTO);
            } catch (Exception e) {
                throw new RuntimeException("추가상품 저장 중 오류 발생", e);
            }
        });

        //상품 이미지 파일 저장
        if (productImages != null && !productImages.isEmpty()) {
            try {
                boolean firstImage = true;
                for (MultipartFile mpr : productImages) {
                    String org_filename = mpr.getOriginalFilename();
                    String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));
                    String stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;

                    File targetFile = new File(productImgPath + stored_filename);
                    mpr.transferTo(targetFile);

                    String isThumb = firstImage ? "Y" : "N"; // 썸네일 지정
                    firstImage = false;

                    // ProductFileDTO 객체 생성 및 DB 저장
                    ProductFileDTO pImgFile = ProductFileDTO.builder()
                            .productSeqno(productEntity)  
                            .orgFilename(org_filename)
                            .storedFilename(stored_filename)
                            .isThumb(isThumb)
                            .build();

                    masterService.productImgFile(pImgFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("파일 업로드 실패: " + e.getMessage());
            }
        }

        //상품 설명 이미지 파일 저장
        if (productDetailImages != null) {
            for (MultipartFile mprd : productDetailImages) {
                String org_filename = mprd.getOriginalFilename();
                String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));
                String stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;

                try {
                    File targetFile = new File(productDetailImgPath + stored_filename);
                    mprd.transferTo(targetFile);

                    ProductInfoFileDTO pDImgFile = ProductInfoFileDTO.builder()
                            .productSeqno(productEntity)  
                            .orgFilename(org_filename)
                            .storedFilename(stored_filename)
                            .build();
                    masterService.productDetailImgFile(pDImgFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        }
        return ResponseEntity.ok().body(Map.of("status", "success", "productSeqno", seqno));
        
    }

    //상품 수정

    //상품 비활성화

    //카테고리 리스트 조회
    @GetMapping("/master/categoryList")
    public ResponseEntity<Map<String, Object>> getManageCategory() {
        // 엔티티를 DTO로 변환하여 전달
        List<Category1DTO> category1DTOs = masterService.getAllCategories1().stream()
            .map(entity -> new Category1DTO(entity.getCategory1Seqno(), entity.getCategory1Name(), entity.getCategory1Order()))
            .collect(Collectors.toList());
    
        List<Category2DTO> category2DTOs = masterService.getAllCategories2().stream()
            .map(entity -> new Category2DTO(entity.getCategory2Seqno(), entity.getCategory1Seqno().getCategory1Seqno(), entity.getCategory2Name()))
            .collect(Collectors.toList());
    
        List<Category3DTO> category3DTOs = masterService.getAllCategories3().stream()
            .map(entity -> new Category3DTO(entity.getCategory3Seqno(), entity.getCategory2Seqno().getCategory2Seqno(), entity.getCategory3Name()))
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

        Category1Entity entity = new Category1Entity();
        entity.setCategory1Name(category1Name);

        //카테고리 저장
        masterService.saveCategory1(entity);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "카테고리1이 추가되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 카테고리 2 추가
    @PostMapping("/master/category2")
    public ResponseEntity<Map<String, Object>> addCategory2(@RequestBody Category2DTO category2dto){
        // Category1Entity 조회
        Category1Entity category1 = masterService.findCategory1ById(category2dto.getCategory1Seqno());
        if (category1 == null) {
            // Category1Entity가 없을 경우 예외 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Category 1 not found for the given id.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        Category2Entity entity = new Category2Entity();
        entity.setCategory1Seqno(category1); 
        entity.setCategory2Name(category2dto.getCategory2Name());  
    
        // 중간 카테고리 저장
        masterService.saveCategory2(entity);
    
        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("message", "카테고리2가 추가되었습니다.");
    
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
        Category3Entity entity = new Category3Entity();
        entity.setCategory2Seqno(category2);
        entity.setCategory3Name(category3dto.getCategory3Name());
        
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
    public ResponseEntity<String> updateCoupon(@RequestBody CouponDTO couponDTO, 
                                            @RequestParam(required = false) List<Long> category3Seqnos,
                                            @RequestParam(required = false) List<Long> productSeqnos,
                                            @RequestParam(required = false) Boolean deleteCategories,
                                            @RequestParam(required = false) Boolean deleteProducts) throws Exception {

        // 기존 쿠폰 번호가 없으면 에러 반환
        if (couponDTO.getCouponSeqno() == null) {
            return ResponseEntity.badRequest().body("수정할 쿠폰 번호가 없습니다.");
        }

        // 쿠폰 존재 여부 확인
        CouponEntity coupon = masterService.findCouponBySeqno(couponDTO.getCouponSeqno());
        if (coupon == null) {
            return ResponseEntity.badRequest().body("해당 쿠폰을 찾을 수 없습니다.");
        }

        masterService.modifyCoupon(couponDTO);

        // 카테고리 정보 수정 (여러 개)
        if (category3Seqnos != null) {
            // 카테고리 삭제 여부 확인 후 삭제
            if (deleteCategories != null && deleteCategories) {
                // 기존 카테고리 정보 삭제
                masterService.deleteCouponCategories(coupon);
            }
            
            // 새 카테고리 정보 저장
            for (Long categorySeqno : category3Seqnos) {
                Category3Entity category3Entity = masterService.findCategory3BySeqno(categorySeqno);
                masterService.saveCouponCategories(category3Entity, coupon);
            }
        }

        // 대상 제품 정보 수정 (여러 개)
        if (productSeqnos != null) {
            // 대상 제품 삭제 여부 확인 후 삭제
            if (deleteProducts != null && deleteProducts) {
                // 기존 대상 제품 정보 삭제
                masterService.deleteCouponTargets(coupon);
            }

            // 새 대상 제품 정보 저장
            for (Long productSeqno : productSeqnos) {
                ProductEntity productEntity = masterService.findProductBySeqno(productSeqno);
                masterService.saveCouponTargets(productEntity, coupon);
            }
        }

        return ResponseEntity.ok("쿠폰이 성공적으로 수정되었습니다.");
    }

    // 쿠폰 배포
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

    // 쿠폰 비활성화

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

    // 회원 등급 수정

    // 회원 비활성화 컬럼 추가


   
    }

