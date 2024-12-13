package com.dabkyu.dabkyu.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dabkyu.dabkyu.dto.Category1DTO;
import com.dabkyu.dabkyu.dto.Category2DTO;
import com.dabkyu.dabkyu.dto.Category3DTO;
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
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.service.MasterService;
import com.dabkyu.dabkyu.service.ProductService;
import com.dabkyu.dabkyu.util.PageUtil;

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
                        category.getCategory1Name()))
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
	public ResponseEntity<?> postFileUpload(ProductDTO productDTO,Model model,
            @RequestParam(name="category3Seqno", required =false) Long category3Seqno,
            @RequestParam(name="productImages",required=false) List<MultipartFile> productImages,
            @RequestParam(name="productDetailImages", required=false) List<MultipartFile> productDetailImages,
			@RequestParam(name="deleteProductImages",required=false) Long[] deleteProductImages,
            @RequestParam(name="deleteProductDetailImages",required=false) Long[] deleteProductDetailImages,
            @RequestParam(name="optionMap", required=false) Map<String, String> optionMap,          
            @RequestParam(name="relatedProductMap", required=false) Map<String, String> relatedProductMap,
            @RequestParam(name="deleteOptionMap", required=false) Map<String, String> deleteOptionMap,
            @RequestParam(name="deleteRelatedMap", required=false) Map<String, String> deleteRelatedMap) throws Exception{

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

		if(os.contains("win")){ 
            productImgPath = "c:\\Repository\\dabkyu\\product\\images\\";
            productDetailImgPath = "c:\\Repository\\dabkyu\\product\\thumbnails\\";
        }else{
            productImgPath = "/home/gladius/Repository/dabkyu/product/images/";
            productDetailImgPath = "/home/gladius/Repository/dabkyu/product/thumbnails/";
        }
		
		//디렉토리가 존재하는지 체크해서 없다면 생성
		File pImg = new File(productImgPath);
        File pdetailImg = new File(productDetailImgPath);
        if(!pImg.exists()) pImg.mkdir();
        if(!pdetailImg.exists()) pdetailImg.mkdir();
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 종료
		
		Long seqno =0L;
		
		//상품 등록
		if(productDTO.getProductSeqno() == null){
            productDTO.setLikecnt(0);
            Category3Entity category3 = masterService.findCategoryBySeqno(category3Seqno);
            //세부카테고리(category3) 설정
            productDTO.setCategory3Seqno(category3);
            seqno = masterService.productPost(productDTO);
            productDTO.setProductSeqno(seqno);
        }else{ //상품 수정하기
            masterService.productModify(productDTO);
			seqno = productDTO.getProductSeqno();

            //옵션 삭제
            if(deleteOptionMap != null){
                for(Map.Entry<String,String> entry : deleteOptionMap.entrySet()){
                    Long optionSeqno = Long.parseLong(entry.getKey());
                    masterService.deleteProductOption(optionSeqno);
                }
            }

            //추가상품 삭제
            if(deleteRelatedMap != null){
                for(Map.Entry<String,String> entry : deleteRelatedMap.entrySet()){
                    Long relatedProductSeqno = Long.parseLong(entry.getKey());
                    masterService.deleteRelatedProduct(relatedProductSeqno);
                }
            }

			//상품 이미지파일 삭제
            if (deleteProductImages != null) {
                for (Long productFileSeqno : deleteProductImages) {
                    masterService.deleteProductFile(productFileSeqno); 
                }
            }
            //상품 설명이미지파일 삭제
            if (deleteProductDetailImages != null){
                for (Long productInfoFileSeqno : deleteProductDetailImages) {
                    masterService.deleteProductInfoFile(productInfoFileSeqno);
                }
            }
		}

        ProductEntity productEntity = masterService.getProductBySeqno(seqno);

        //옵션 추가
        List<ProductOptionDTO> productOptionDTOList = new ArrayList<>();
        if(optionMap != null){ 
            for(Map.Entry<String,String> entry : optionMap.entrySet()){
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
        if(relatedProductMap != null){ 
            for(Map.Entry<String,String> entry : relatedProductMap.entrySet()){
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
        

        //상품 이미지파일 저장
        if(productImages != null){
            boolean firstImage = true;
            for(MultipartFile mpr:productImages){
                String org_filename = mpr.getOriginalFilename();
                String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));			
                String stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;

                try{
                    File targetFile = new File(productImgPath + stored_filename);				
                    mpr.transferTo(targetFile);

                    String isThumb = firstImage?"Y":"N"; //썸네일 지정
                    firstImage = false;

                    ProductFileDTO pImgFile = ProductFileDTO.builder()
                                            .productFileSeqno(seqno)
                                            .orgFilename(org_filename)
                                            .storedFilename(stored_filename)
                                            .isThumb(isThumb)
                                            .build();
                    masterService.productImgFile(pImgFile);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        //상품 설명이미지파일 저장
        if(productDetailImages != null){
            for(MultipartFile mprd:productDetailImages){
                String org_filename = mprd.getOriginalFilename();
                String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));			
                String stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;

                try{
                    File targetFile = new File(productDetailImgPath + stored_filename);				
                    mprd.transferTo(targetFile);
                    
                    ProductInfoFileDTO pDImgFile = ProductInfoFileDTO.builder()
                                                .productInfoFileSeqno(seqno)
                                                .orgFilename(org_filename)
                                                .storedFilename(stored_filename)
                                                .build();
                    masterService.productDetailImgFile(pDImgFile);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
		
		return ResponseEntity.ok().build();
	}

    //상품 수정

    //상품 삭제

}