package com.dabkyu.dabkyu.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale.Category;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.dabkyu.dabkyu.dto.Category1DTO;
import com.dabkyu.dabkyu.dto.Category2DTO;
import com.dabkyu.dabkyu.dto.Category3DTO;
import com.dabkyu.dabkyu.dto.CategorySalesDTO;
import com.dabkyu.dabkyu.dto.CouponCategoryDTO;
import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.dto.CouponTargetDTO;
import com.dabkyu.dabkyu.dto.DailySalesDTO;
import com.dabkyu.dabkyu.dto.DailyVisitorDTO;
import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.MemberSalesDTO;
import com.dabkyu.dabkyu.dto.MonthlySalesDTO;
import com.dabkyu.dabkyu.dto.OrderInfoDTO;
import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.dto.ProductInfoFileDTO;
import com.dabkyu.dabkyu.dto.ProductOptionDTO;
import com.dabkyu.dabkyu.dto.ProductSalesDTO;
import com.dabkyu.dabkyu.dto.QuestionCommentDTO;
import com.dabkyu.dabkyu.dto.QuestionDTO;
import com.dabkyu.dabkyu.dto.QuestionFileDTO;
import com.dabkyu.dabkyu.dto.RelatedProductDTO;
import com.dabkyu.dabkyu.dto.ReviewFileDTO;
import com.dabkyu.dabkyu.dto.SalesByAgeGroupDTO;
import com.dabkyu.dabkyu.dto.SalesByMemberGradeDTO;
import com.dabkyu.dabkyu.dto.SignupAgeStatDTO;
import com.dabkyu.dabkyu.dto.SignupDateStatDTO;
import com.dabkyu.dabkyu.dto.SignupGenderStatDTO;
import com.dabkyu.dabkyu.dto.VisitorCountDTO;
import com.dabkyu.dabkyu.entity.AddedRelatedProductEntity;
import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.OrderProductOptionEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.ReportEntity;
import com.dabkyu.dabkyu.entity.repository.Category1Repository;
import com.dabkyu.dabkyu.entity.repository.Category2Repository;
import com.dabkyu.dabkyu.entity.repository.Category3Repository;
import com.dabkyu.dabkyu.entity.repository.CouponRepository;
import com.dabkyu.dabkyu.entity.repository.MemberLogRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.ProductFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;

import com.dabkyu.dabkyu.service.MasterService;
import com.dabkyu.dabkyu.service.MemberService;
import com.dabkyu.dabkyu.service.QuestionService;
import com.dabkyu.dabkyu.service.ReviewService;
import com.dabkyu.dabkyu.util.PageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@AllArgsConstructor
public class MasterController{

    private final MasterService masterService;
    private final MemberService memberService;
    private final QuestionService questionService;
    private final ReviewService reviewService;
    
    //메인페이지
    @GetMapping("/master") 
    public void getMaster() {}

    //고객정보페이지
    @GetMapping("/master/client")
    public void getClient(Model model, 
                @RequestParam("page") int pageNum,
                @RequestParam(name="keyword",defaultValue="",required=false) String keyword,
                @RequestParam(name="memberGrade",defaultValue="",required=false) String memberGrade) 
                throws Exception {
       
        int postNum = 15; //한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 10; //화면 하단에 보여지는 페이지리스트의 페이지 갯수	
		
		PageUtil page = new PageUtil();
		Page<MemberEntity> clientList = masterService.memberList(pageNum, postNum, keyword, memberGrade);
		int totalCount = (int)clientList.getTotalElements();

		model.addAttribute("list", clientList);
		model.addAttribute("listIsEmpty", clientList.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("memberGrade", memberGrade);
		model.addAttribute("pageList", page.getPageClient(pageNum, postNum, pageListCount,totalCount,keyword));
    }

    //고객 삭제 기능
    @Transactional
    @GetMapping("/master/deleteClient")
    public String getDeleteClient(@RequestParam("email") String email) throws Exception{
 
		masterService.clientDelete(email); 

		return "redirect:/master/client"; 
    }

    //고객정보 수정 화면보기
    @GetMapping("/master/modifyClient")
    public void getModifyClient(@RequestParam("email") String email, Model model) throws Exception{

        model.addAttribute("member", masterService.getMemberByEmail(email));
    }

    //고객정보 수정 
    @ResponseBody
    @PostMapping("/master/modifyViewClient")
    public String postModifyClient(@ModelAttribute MemberDTO memberDTO) throws Exception {

        masterService.memberModify(memberDTO);

        return "{\"message\":\"good\"}";
    }

    //상품 리스트 화면보기 
    @GetMapping("/master/productList")
    public void getProductList(Model model, 
                @RequestParam("page") int pageNum, 
                @RequestParam(name="productName",defaultValue="",required=false) String productName, //productname 검색
                @RequestParam(name="category1Seqno", required=false) Long category1Seqno, //category 필터링
                @RequestParam(name="category2Seqno", required=false) Long category2Seqno,
                @RequestParam(name="category3Seqno", required=false) Long category3Seqno) 
                throws Exception{
        int postNum = 10;
        int pageListCount = 10;
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "productName"));

        PageUtil page = new PageUtil();
        Page<ProductEntity> productList = masterService.productList(category1Seqno, category2Seqno, category3Seqno, productName, pageRequest);

        int totalCount = (int)productList.getTotalElements();

        List<ProductFileDTO> thumbnailFiles = new ArrayList<>(); //썸네일 이미지
        for (ProductEntity product : productList) {
            List<ProductFileDTO> productFiles = masterService.getProductFiles(product.getProductSeqno());
            ProductFileDTO thumbnail = null;
            for (ProductFileDTO file : productFiles) {
                if ("Y".equals(file.getIsThumb())) {
                    thumbnail = file;
                    break; 
                }
            }
            thumbnailFiles.add(thumbnail);
        }

        model.addAttribute("productList", productList);
        model.addAttribute("listIsEmpty", productList.hasContent()?"N":"Y");
        model.addAttribute("totalElement", totalCount);
        model.addAttribute("postNum", postNum);
        model.addAttribute("page", pageNum);
        model.addAttribute("productName", productName);
        model.addAttribute("category1Seqno", category1Seqno);
        model.addAttribute("category2Seqno", category2Seqno);
        model.addAttribute("category3Seqno", category3Seqno);
        model.addAttribute("thumbnailFiles", thumbnailFiles);
		model.addAttribute("pageList", page.getPageProduct(pageNum, postNum, pageListCount,totalCount,productName));
    }

    //상품 상세보기 
    @GetMapping("/master/getProductDetail/{productSeqno}")
    public void getProductDetail(@PathVariable Long productSeqno, 
                Model model) 
                throws Exception {

        ProductEntity productEntity = masterService.getProductBySeqno(productSeqno); 
        model.addAttribute("productview", productEntity); // 상품 정보           
        model.addAttribute("productfileview", masterService.getProductFiles(productSeqno)); //상품 파일 정보 
        model.addAttribute("productinfofileview", masterService.getProductInfoFiles(productSeqno)); //상품설명 이미지 파일 정보
        model.addAttribute("optionview", masterService.getProductOptions(productSeqno)); //상품 옵션 정보
        model.addAttribute("relatedproductview", masterService.getRelatedProducts(productSeqno)); //추가 상품 정보
        model.addAttribute("category1view", masterService.getCategory1ByProduct(productSeqno)); //카테고리
        model.addAttribute("category2view", masterService.getCategory2ByProduct(productSeqno)); 
        model.addAttribute("category3view", masterService.getCategory3ByProduct(productSeqno));          
    }

    //상품 등록 화면보기 
    @GetMapping("/master/postProduct")
    public void getPostProduct(Model model) {
        List<Category1Entity> allCategory1 = masterService.getAllCategories1();
        List<Category2Entity> allCategory2 = masterService.getAllCategories2();
        List<Category3Entity> allCategory3 = masterService.getAllCategories3();
        
        model.addAttribute("allcategory1", allCategory1);
        model.addAttribute("allcategory2", allCategory2);
        model.addAttribute("allcategory3", allCategory3);

    }
    
    @ResponseBody
    @GetMapping("/master/getMiddleCategories")
    public List<Category2DTO> getMiddleCategories(@RequestParam("category1Seqno") Long category1Seqno) {
        // Category2Entity 목록을 가져옴
        List<Category2Entity> category2List = masterService.getCategories2ByCategory1(category1Seqno);
        
        // Category2DTO 목록으로 변환
        List<Category2DTO> category2DTOList = category2List.stream()
                .map(category -> new Category2DTO(
                        category.getCategory2Seqno(), 
                        category.getCategory1Seqno().getCategory1Seqno(),
                        category.getCategory2Name()))
                .collect(Collectors.toList());
        
        return category2DTOList;
    }
    

    @GetMapping("/master/getSubCategories")
    @ResponseBody
    public List<Category3DTO> getSubCategories(@RequestParam("category2Seqno") Long category2Seqno) {
        // Category3Entity 목록을 가져옴
        List<Category3Entity> category3List = masterService.getCategories3ByCategory2(category2Seqno);
        
        // Category3DTO 목록으로 변환
        List<Category3DTO> category3DTOList = category3List.stream()
                .map(category -> new Category3DTO(
                        category.getCategory3Seqno(),
                        category.getCategory2Seqno().getCategory2Seqno(), 
                        category.getCategory3Name()))
                .collect(Collectors.toList());
        
        return category3DTOList;
    }
    
    //상품 수정 화면보기 >> 수정 눌렀을 때 
    @GetMapping("/master/modifyProduct")
    public void getmMdifyProduct(@RequestParam("productSeqno") Long productSeqno,
                Model model) 
                throws Exception{
                
        ProductEntity productEntity = masterService.getProductBySeqno(productSeqno); 
        String isTemporaryCategory = productEntity.getCategory3Seqno().getIsTemporary(); // "Y","N" 값으로 임시 카테고리 여부 확인
        if(isTemporaryCategory == null){
            isTemporaryCategory = "N";
        }
        model.addAttribute("productview", productEntity); // 상품 정보           
        model.addAttribute("productfileview", masterService.getProductFiles(productSeqno)); //상품 파일 정보 
        model.addAttribute("productinfofileview", masterService.getProductInfoFiles(productSeqno)); //상품설명 이미지 파일 정보
        model.addAttribute("optionview", masterService.getProductOptions(productSeqno)); //상품 옵션 정보
        model.addAttribute("relatedproductview", masterService.getRelatedProducts(productSeqno)); //추가 상품 정보
        model.addAttribute("category1view", masterService.getCategory1ByProduct(productSeqno)); //기존에 등록한 상품 카테고리 정보   
        model.addAttribute("category2view", masterService.getCategory2ByProduct(productSeqno)); 
        model.addAttribute("category3view", masterService.getCategory3ByProduct(productSeqno));          
        model.addAttribute("allcategory1", masterService.getAllCategories1()); //모든 카테고리 정보
        model.addAttribute("allcategory2", masterService.getAllCategories2());
        model.addAttribute("allcategory3", masterService.getAllCategories3());

        model.addAttribute("needsModification", isTemporaryCategory); //임시 카테고리 //[수정] 알림을 위한
    }

    //상품 등록 및 수정  
    @ResponseBody
    @PostMapping("/master/postProduct")
    public String postProduct(ProductDTO productDTO, Model model,
                @RequestParam(name="subCategory", required =false) Long subCategory,
                @RequestParam(name="productImage",required=false) List<MultipartFile> productImages,
                @RequestParam(name="detailImage", required=false) List<MultipartFile> detailImages,
                @RequestParam(name="deleteProductImages", required=false) Long[] deleteProductImages,
                @RequestParam(name="deleteDetailProductImage",required=false) Long[] deleteDetailProductImages,
                @RequestParam(name="optionMap", required=false) Map<String, String> optionMap,          
                @RequestParam(name="relatedProductMap", required=false) Map<String, String> relatedProductMap,
                @RequestParam(name="deleteOptionMap", required=false) Map<String, String> deleteOptionMap,
                @RequestParam(name="deleteRelatedMap", required=false) Map<String, String> deleteRelatedMap) 
                throws Exception{
        
        //파일 저장
        String os = System.getProperty("os.name").toLowerCase();
		String productImgPath;
        String productDetailImgPath;

		if(os.contains("win")){
            productImgPath = "c:\\Repository\\productfile\\productImages\\";
            productDetailImgPath = "c:\\Repository\\productfile\\detailimage\\";
        }else{
            productImgPath = "/home/gladius/Repository/productfile/productImages/";
            productDetailImgPath = "/home/gladius/Repository/productfile/detailimage/";
        }
			
        File pImg = new File(productImgPath);
        File pdetailImg = new File(productDetailImgPath);
        if(!pImg.exists()) pImg.mkdir();
        if(!pdetailImg.exists()) pdetailImg.mkdir();

        Long seqno = 0L;

        //상품 정보
        if(productDTO.getProductSeqno() == null){
            productDTO.setLikecnt(0);
            log.info("-------------카테고리3Seqno: {} ----------------", subCategory);
            Category3Entity category3 = masterService.findCategoryBySeqno(subCategory);
            log.info("-------------카테고리3: {} ----------------", category3.getCategory3Name());
            productDTO.setCategory3Seqno(category3);
            seqno = masterService.productPost(productDTO);
            productDTO.setProductSeqno(seqno);
        }else{ //상품 수정하기
            masterService.productModify(productDTO);
        }
        ProductEntity productEntity = masterService.getProductBySeqno(seqno);
        //옵션 추가 및 수정
        List<ProductOptionDTO> productOptionDTOList = new ArrayList<>();
        if(optionMap != null){ 
            for(Map.Entry<String,String> entry : optionMap.entrySet()){
                String[] keys = entry.getKey().split(",");
                String optCategory = keys[0];
                String optName = keys[1];
                int optPrice = Integer.parseInt(entry.getValue());

                ProductOptionDTO optionDTO = new ProductOptionDTO();
                optionDTO.setProductSeqno(productEntity);
                optionDTO.setOptCategory(optCategory);
                optionDTO.setOptName(optName);
                optionDTO.setOptPrice(optPrice);
                productOptionDTOList.add(optionDTO);
            }
        }

        //옵션 저장
        productOptionDTOList.forEach(optionDTO -> {
            try {
                masterService.saveProductOption(optionDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //추가상품 추가 및 수정
        List<RelatedProductDTO> relatedProductDTOList = new ArrayList<>();
        if(relatedProductMap != null){ 
            for(Map.Entry<String,String> entry : relatedProductMap.entrySet()){
                String[] keys = entry.getKey().split(",");
                String relatedproductCategory = keys[0];
                String relatedproductName = keys[1];
                int relatedproductPrice = Integer.parseInt(entry.getValue());

                RelatedProductDTO relatedProductDTO = new RelatedProductDTO();
                relatedProductDTO.setProductSeqno(productEntity);
                relatedProductDTO.setRelatedproductCategory(relatedproductCategory);
                relatedProductDTO.setRelatedproductName(relatedproductName);
                relatedProductDTO.setRelatedproductPrice(relatedproductPrice);
                relatedProductDTOList.add(relatedProductDTO);
            }
        }

        //추가상품 저장
        relatedProductDTOList.forEach(relatedProductDTO -> {
            try {
                masterService.saveRelatedProduct(relatedProductDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        //상품이미지파일 //productfile
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

        //상세보기이미지파일 //product
        if(detailImages != null){
            for(MultipartFile mprd:detailImages){
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

        //상품 이미지 파일 삭제
        if (deleteProductImages != null) {
            for (Long productFileSeqno : deleteProductImages) {
                masterService.deleteProductFile(productFileSeqno); 
            }
        }
        //상세보기 파일 삭제
        if (deleteDetailProductImages != null){
            for (Long productInfoFileSeqno : deleteDetailProductImages) {
                masterService.deleteProductInfoFile(productInfoFileSeqno);
            }
        }

        return "{\"message\":\"good\"}"; 
    }

    

    //상품 삭제 (활성 비활성) 
    @ResponseBody
	@GetMapping("/master/secretProduct") 
    public void postSecretProduct(@RequestParam("productSeqno") Long productSeqno) throws Exception {
        masterService.secretYNProduct(productSeqno);
    }

    //주문내역 리스트
    @GetMapping("/master/order")
    public void getOrderList(Model model, 
                            @RequestParam(name="page", defaultValue="1") int pageNum, 
                            @RequestParam(name="productname",defaultValue="",required=false) String productname,
                            @RequestParam(name="category",required=false) Long category)
                            throws Exception{
        int postNum = 10; 
        int pageListCount = 10;                         
        
        PageUtil page = new PageUtil();
        Page<Map<String, Object>> orderList = masterService.orderList(pageNum, postNum, productname, category);                        
        int totalCount = (int) orderList.getTotalElements(); 
        
        model.addAttribute("orderList", orderList);
        model.addAttribute("listIsEmpty", orderList.hasContent()?"N":"Y");
        model.addAttribute("totalElements", totalCount);
        model.addAttribute("postNum", postNum);
        model.addAttribute("page", pageNum);
        model.addAttribute("productname", productname);
        model.addAttribute("category", category);
        model.addAttribute("pageList", page.getPageOrder(pageNum, postNum, pageListCount,totalCount,productname,category));
    }
        

    //주문내역 상세보기 
    @GetMapping("/master/orederView/{orderSeqno}")
    public void getOrderView(@PathVariable Long orderSeqno, Model model) throws Exception {
        OrderInfoEntity orderInfoEntity = masterService.getOrderInfo(orderSeqno); 
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO(orderInfoEntity);
        List<OrderDetailEntity> orderDetailEntities = masterService.getOrderDetails(orderSeqno);
        List<OrderProductEntity> orderProducts = masterService.getOrderProducts(orderSeqno);
        List<AddedRelatedProductEntity> addedRelatedProducts = masterService.getAddedRelatedProducts(orderProducts);
        List<OrderProductOptionEntity> orderProductOptions = masterService.getOrderProductOptions(orderProducts);
        List<ProductEntity> products = masterService.getProducts(orderProducts);

        model.addAttribute("orderInfo", orderInfoDTO);//주문정보
        model.addAttribute("orderDetails", orderDetailEntities); //주문상세
        model.addAttribute("orderProducts", orderProducts); //주문제품
        model.addAttribute("addedRelatedProducts", addedRelatedProducts); //추가상품
        model.addAttribute("orderProductOptions", orderProductOptions); //옵션
        model.addAttribute("products", products); //상품정보
    }

    //주문 상태 변경 //newStatus : 상품준비중 - 배송중 - 배송완료?
    @ResponseBody
    @GetMapping("/master/modifyOrder")
    public String postModifyOrder(@RequestParam("orderSeqno") Long orderSeqno,
                                @RequestParam("orderStatus") String orderStatus)
                                throws Exception {
        masterService.modifyOrderStatus(orderSeqno,orderStatus);

        return "redirect:/master/order";
    }

    //카테고리 리스트 화면보기
    @GetMapping("/master/manageCategory")
    public void getManageCategory(Model model) {

        // 엔티티를 DTO로 변환하여 전달
        List<Category1DTO> category1DTOs = masterService.getAllCategories1().stream()
            .map(entity -> new Category1DTO(entity.getCategory1Seqno(), entity.getCategory1Name()))
            .collect(Collectors.toList());

        List<Category2DTO> category2DTOs = masterService.getAllCategories2().stream()
            .map(entity -> new Category2DTO(entity.getCategory2Seqno(), entity.getCategory1Seqno().getCategory1Seqno(), entity.getCategory2Name()))
            .collect(Collectors.toList());

        List<Category3DTO> category3DTOs = masterService.getAllCategories3().stream()
            .map(entity -> new Category3DTO(entity.getCategory3Seqno(), entity.getCategory2Seqno().getCategory2Seqno(), entity.getCategory3Name()))
            .collect(Collectors.toList());
            
        model.addAttribute("categories1", category1DTOs);
        model.addAttribute("categories2", category2DTOs);
        model.addAttribute("categories3", category3DTOs);
    }

    
    //카테고리 추가 및 수정 및 삭제 
    @Transactional
    @ResponseBody
    @PostMapping("/master/manageCategory")
    public ResponseEntity<String> postManageCategory(
                        @RequestParam(name="category1") Map<String, String> category1Map,
                        @RequestParam(name="category2") Map<String, String> category2Map,
                        @RequestParam(name="category3") Map<String, String> category3Map,
                        @RequestParam(name="deleteCategory1",required=false) Map<String, String> deleteCategory1Map,
                        @RequestParam(name="deleteCategory2",required=false) Map<String, String> deleteCategory2Map,
                        @RequestParam(name="deleteCategory3",required=false) Map<String, String> deleteCategory3Map) 
                        throws Exception{  
                                            
        try{
            List<Category1DTO> category1DTOList = new ArrayList<>();
            for (Map.Entry<String, String> entry : category1Map.entrySet()) {
                Long category1Seqno = Long.parseLong(entry.getKey());
                String category1Name = entry.getValue();
                category1DTOList.add(new Category1DTO(category1Seqno, category1Name));
            }

            List<Category2DTO> category2DTOList = new ArrayList<>();
            for(Map.Entry<String, String> entry : category2Map.entrySet()){
                // entry => "21(2Seqno),13(1Seqno)" : "TV"(name)
                String[] keys = entry.getKey().split(",");
                Long category2Seqno = Long.parseLong(keys[0]);    // 21
                Long category1Seqno = Long.parseLong(keys[1]);    // 13
                String category2Name = entry.getValue();    // "TV"
                category2DTOList.add(new Category2DTO(category2Seqno, category1Seqno, category2Name));
            }

            List<Category3DTO> category3DTOList = new ArrayList<>();
            for (Map.Entry<String, String> entry : category3Map.entrySet()){
                String[] keys = entry.getKey().split(",");
                Long category3Seqno = Long.parseLong(keys[0]);
                Long category2Seqno = Long.parseLong(keys[1]);
                String category3Name = entry.getValue();
                category3DTOList.add(new Category3DTO(category3Seqno, category2Seqno, category3Name));
            }

            // 카테고리 저장/수정 처리
            masterService.saveCategories(category1DTOList, category2DTOList, category3DTOList);

            // 카테고리 삭제 처리
            List<Category1DTO> deleteCategory1List = new ArrayList<>();
            if (deleteCategory1Map != null){
                for(Map.Entry<String, String> entry : deleteCategory1Map.entrySet()){
                    Long category1Seqno = Long.parseLong(entry.getKey());
                    String category1Name = entry.getValue();
                    deleteCategory1List.add(new Category1DTO(category1Seqno, category1Name));  
                }
            }

            List<Category2DTO> deleteCategory2List = new ArrayList<>();
            if(deleteCategory2Map != null){
                for(Map.Entry<String, String> entry : deleteCategory2Map.entrySet()){
                    String[] keys = entry.getKey().split(",");
                    Long category2Seqno = Long.parseLong(keys[0]);
                    Long category1Seqno = Long.parseLong(keys[1]);
                    String category2Name = entry.getValue();
                    deleteCategory2List.add(new Category2DTO(category2Seqno, category1Seqno, category2Name ));
                }
            }
            
            List<Category3DTO> deleteCategory3List = new ArrayList<>();
            if(deleteCategory3Map != null){
               for (Map.Entry<String, String> entry : deleteCategory3Map.entrySet()){
                    String[] keys = entry.getKey().split(",");
                    Long category3Seqno = Long.parseLong(keys[0]);
                    Long category2Seqno = Long.parseLong(keys[1]);
                    String category3Name = entry.getValue();
                    deleteCategory3List.add(new Category3DTO(category3Seqno, category2Seqno, category3Name));
                } 
            }
            
            deleteCategory3List.forEach(dto -> {
                try {
                    masterService.deleteCategory3(dto.getCategory3Seqno());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            deleteCategory2List.forEach(dto -> {
                try {
                    masterService.deleteCategory2(dto.getCategory2Seqno());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            deleteCategory1List.forEach(dto -> {
                try {
                    masterService.deleteCategory1(dto.getCategory1Seqno());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            return ResponseEntity.ok("Categories saved successfully.");
        }catch(Exception e){
            return ResponseEntity.status(500).body("Error saving categories: " + e.getMessage());
        }
    } 
        


    //문의 리스트 
    @GetMapping("/master/question")
    public void getQuestion(Model model, 
                @RequestParam("page") int pageNum, 
                @RequestParam(name="queType",defaultValue="",required=false) String queType)
                throws Exception{
        int postNum = 15;
        int pageListCount = 10;

        PageUtil page = new PageUtil();
        Page<Map<String, Object>> questionList = masterService.questionList(pageNum, postNum, queType);                        
        int totalCount = (int) questionList.getTotalElements(); 

        model.addAttribute("questionList", questionList);
        model.addAttribute("listIsEmpty", questionList.hasContent() ? "N" : "Y");
        model.addAttribute("totalElements", totalCount);
        model.addAttribute("postNum", postNum);
        model.addAttribute("page", pageNum);
        model.addAttribute("queType", queType);
        model.addAttribute("pageList", page.getPageQuestion(pageNum, postNum, pageListCount, totalCount, queType));
    }

    //문의 상세보기
    @GetMapping("/master/questionDetail")
    public void getQuestionView(@RequestParam("queSeqno") Long queSeqno, @RequestParam("page") int pageNum,
            @RequestParam(name="queType",defaultValue="",required=false) String queType,
            Model model) throws Exception {

        // 문의 정보 가져오기
        QuestionDTO question = questionService.view(queSeqno);
        List<QuestionFileDTO> questionFiles = questionService.fileListView(queSeqno);

        // 이전/다음 문의 번호 가져오기
        Long preSeqno = questionService.pre_seqno(queSeqno, queType);
        Long nextSeqno = questionService.next_seqno(queSeqno, queType);

        // 모델에 데이터 추가
        model.addAttribute("question", question);
        model.addAttribute("questionFiles", questionFiles);
        model.addAttribute("pre_seqno", preSeqno);
        model.addAttribute("next_seqno", nextSeqno);
        model.addAttribute("page", pageNum);
        model.addAttribute("queType", queType);
            
    }

    //문의 답변 등록 및 수정
    @ResponseBody
    @PostMapping("/master/question/reply")
    public String postReply(
            @RequestParam("option") String option, 
            @RequestBody QuestionCommentDTO commentDTO, HttpSession session
            ) throws Exception {
        log.info("답변 등록 시작");
        log.info(option);
        String email = (String) session.getAttribute("email");
        Long queSeqno = commentDTO.getQuestionSeqno();
        QuestionEntity questionEntity = masterService.getQuestionSeqno(queSeqno);
        commentDTO.setQueSeqno(questionEntity);
        commentDTO.setEmail(masterService.getMemberEmail(email));

        if ("I".equals(option)) {
            log.info("답변 등록 서비스 호출");
            masterService.replyQuestion(commentDTO, questionEntity);
        } else if("U".equals(option)) {
            masterService.replyQuestionModify(commentDTO);
        }
        return "{\"message\":\"good\"}";

	}

    //문의 삭제 
    @Transactional
    @PostMapping("/master/question/delete")
    public ResponseEntity<?> deleteQuestion(@RequestParam("queSeqno") Long queSeqno) {
        try {
            // 첨부 파일 삭제
            List<QuestionFileDTO> questionFiles = questionService.fileListView(queSeqno);
            if (questionFiles != null) {
                for (QuestionFileDTO questionFile : questionFiles) {
                    masterService.deleteQuestionFile(questionFile.getQuestionFileSeqno());
                }
            }

            // 문의 삭제
            masterService.deleteQuestion(queSeqno);

            // 성공 응답 반환
            return ResponseEntity.ok().body("문의가 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            // 데이터가 존재하지 않을 경우
            return ResponseEntity.status(404).body("해당 문의를 찾을 수 없습니다.");
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(500).body("문의 삭제 중 오류가 발생했습니다.");
        }
    }

    //문의 답변 삭제
    @Transactional
    @PostMapping("/master/question/replydelete")
    public ResponseEntity<String> deleteReply(@RequestParam("queSeqno") Long queSeqno) throws Exception {
        QuestionEntity questionEntity = masterService.getQuestionSeqno(queSeqno);
        
        masterService.deleteQueComment(questionEntity);
        
        return ResponseEntity.ok("답변이 성공적으로 삭제되었습니다.");
    }

    //리뷰 리스트 화면 
    @GetMapping("/master/reviewList")
    public void getReviewList(Model model, @RequestParam("page") int pageNum, 
                @RequestParam(name="category", required=false) Long category) 
                throws Exception{
        int postNum = 15;
        int pageListCount = 10;

        PageUtil page = new PageUtil();
		Page<Map<String,Object>> reviewList = masterService.reviewList(pageNum, postNum, category);
		int totalCount = (int)reviewList.getTotalElements();

		model.addAttribute("reviewList", reviewList);
		model.addAttribute("listIsEmpty", reviewList.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", category);
		model.addAttribute("pageList", page.getPageReview(pageNum, postNum, pageListCount,totalCount,category));
    } 

    //리뷰 상세보기
    @PostMapping("/master/reviewList/{reviewSeqno}")
    @ResponseBody
    public Map<String, Object> getReviewView(@PathVariable Long reviewSeqno) throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("review", reviewService.view(reviewSeqno));
        response.put("reviewFiles", reviewService.fileListView(reviewSeqno));
        return response;
    }

    //리뷰 신고 리스트
    @GetMapping("/master/reviewReport")
    public void getReviewReportList(Model model, 
                @RequestParam("page") int pageNum,
                @RequestParam("reportTitle") String reportTitle) //title로 검색
                throws Exception{
        int postNum = 10;
        int pageListCount = 10;

        PageUtil page = new PageUtil();
        Page<ReportEntity> reviewReportList = masterService.reviewReposrtList(pageNum, postNum, reportTitle);
        int totalCount = (int)reviewReportList.getTotalElements();

        model.addAttribute("list", reviewReportList);
		model.addAttribute("listIsEmpty", reviewReportList.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
        model.addAttribute("reportTitle", reportTitle);
		model.addAttribute("pageList", page.getPageRoport(pageNum, postNum, pageListCount, totalCount, reportTitle));
    }

    //리뷰 신고 삭제
    @Transactional
    @PostMapping("/master/reviewReport/delete")
    public String getDeleteRoport(@RequestParam("reportSeqno") Long reportSeqno) throws Exception{
        masterService.deleteReport(reportSeqno);
        return "redirect:/master/reviewReport";
    }

    //리뷰 삭제 
    @Transactional
    @PostMapping("/master/review/delete")
    public String getDeleteReview(@RequestParam("reviewSeqno") Long reviewSeqno) throws Exception{
        List<ReviewFileDTO> reviewFiles = reviewService.fileListView(reviewSeqno);
        if (reviewFiles != null) {
            for (ReviewFileDTO reviewFile : reviewFiles) {
                masterService.deleteReviewFile(reviewFile.getReviewFileSeqno()); //리뷰 파일삭제
            }
        }
        masterService.deleteReview(reviewSeqno); //리뷰삭제

        return "redirect:/master/reviewList?page=1";
    }

    //쿠폰 리스트보기 
    @GetMapping("/master/couponList")
    public void getCouponList(Model model,
                @RequestParam("page") int pageNum) 
                throws Exception {
        int postNum = 15; 
        int pageListCount = 10; 
        
        PageUtil page = new PageUtil();
        Page<Map<String, Object>> couponPage = masterService.couponList(pageNum, postNum);
        int totalCount = (int) couponPage.getTotalElements();

        model.addAttribute("couponPage", couponPage);
		model.addAttribute("listIsEmpty", couponPage.hasContent()?"N":"Y");
        model.addAttribute("totalElement", totalCount);
        model.addAttribute("postNum", postNum);
        model.addAttribute("page", pageNum);
        model.addAttribute("pageList", page.getPageCoupon(pageNum, postNum, pageListCount, totalCount));
    }

    //쿠폰 등록 화면보기
    @GetMapping("/master/createCoupon")
    public void getCreateCoupon() {}

    //쿠폰 수정 화면보기
    @GetMapping("/master/modifyCoupon")
    public void getModifyCoupon(@RequestParam("couponSeqno") Long couponSeqno,
                Model model) 
                throws Exception{

        model.addAttribute("couponview", masterService.getCouponBySeqno(couponSeqno)); //쿠폰 정보
        model.addAttribute("existingCategories", masterService.getCouponCategories(couponSeqno)); //기존에 등록한 쿠폰 카테고리
        model.addAttribute("existingTargets", masterService.getCouponTargets(couponSeqno)); //기존에 등록한 쿠폰 타겟
        model.addAttribute("allCategories", masterService.getAllCategories3()); //모든 카테고리
        model.addAttribute("allProducts", masterService.getAllProducts());  //모든 상품
    }

    //쿠폰 등록, 수정
    //쿠폰 타입 A(전체상품적용), T(특정상품적용(카테고리)), C(단일상품적용) 
    @ResponseBody
    @PostMapping("/master/createCoupon")
    public String postCreateCoupon(CouponDTO couponDTO, 
                @RequestParam("kind") String kind,
                @RequestParam(name="couponCategory", required=false) List<Long> couponCategories,
                @RequestParam(name="couponTarget", required=false) List<Long> couponTargets,
                @RequestParam(name="deleteCouponCategory", required=false) List<Long> deleteCouponCategories,
                @RequestParam(name="deleteCouponTarget", required=false) List<Long> deleteCouponTargets) 
                throws Exception{
        
        Long couponSeqno = 0L;
       
        if(kind.equals("I")){
            
            couponSeqno = masterService.writeCoupon(couponDTO);

        }else if(kind.equals("U")){
            masterService.modifyCoupon(couponDTO); 
            couponSeqno = couponDTO.getCouponSeqno(); 
            //카테고리 삭제
            if(deleteCouponCategories != null){
                for (Long categorySeqno : deleteCouponCategories) {   
                    masterService.deleteCouponCategory(couponSeqno, categorySeqno);   
                } 
            } 
            //타겟 삭제
            if(deleteCouponTargets != null){
                for (Long targetSeqno : deleteCouponTargets) {
                    masterService.deleteCouponTarget(couponSeqno, targetSeqno);
                }
            } 
        }
        if("A".equals(couponDTO.getCouponType())){ //모든 상품 적용
            List<ProductEntity> allProducts = masterService.getAllProducts(); 
            for (ProductEntity product : allProducts) {
                masterService.saveCouponTarget(couponSeqno, product);
            }
        }else if("C".equals(couponDTO.getCouponType())){ //특정 상품 적용(카테고리)
            //List<Category3DTO> categories = masterService.getAllCategories();
            for (Long categorySeqno : couponCategories) {
                masterService.saveCouponCategory(couponSeqno, categorySeqno);
            }
        }else if("T".equals(couponDTO.getCouponType())){ //단일 상품 적용(상품) // 이름으로 검색?? //수정필요
            for (Long productSeqno : couponTargets) {
                ProductEntity productEntity = masterService.getProductBySeqno(productSeqno); //seqno로 받아서 name으로 검색? 
                masterService.saveCouponTarget(couponSeqno, productEntity);
            }
        }
        return "{\"message\":\"good\"}"; 
    }

    //쿠폰-사용자 배포 (자동)
    @PostMapping("/master/distributionCoupon")
    public void clientCoupon(@RequestParam("couponSeqno") Long couponSeqno,
                @RequestParam(name = "isAllMembers", required = false) boolean isAllMembers,
                @RequestParam(name = "memberGrade", required = false) String memberGrade,
                @RequestParam(name = "isBirthday", required = false) boolean isBirthday,
                @RequestParam(name = "isNewMember", required = false) boolean isNewMember) 
                throws Exception{
        //자동: 특정 회원 지정 발행, 웅영자 지정 대상 자동 발행(신규회원, 첫주문 완료 회원, 생일인 회원) ///구매한지 1년 후 쿠폰 발급??              
        masterService.couponToUser(couponSeqno, isAllMembers, memberGrade, isBirthday, isNewMember); 
    }

    //쿠폰 다운로드 
    @PostMapping("/master/downloadCoupon")
    public void clientDownloadCoupon(@RequestParam("couponSeqno") Long couponSeqno, 
                @RequestParam("email") String email) 
                throws Exception{
        masterService.downloadCoupon(couponSeqno, email);
    }

    //쿠폰 코드 발급
    @PostMapping("/master/codeCoupon")
    public void clientCodeCoupon(@RequestParam("couponCode") String couponCode,
                @RequestParam("email") String email) 
                throws Exception{
        masterService.getCouponCode(couponCode, email);
    }

    // 결제 취소 및 환불 신청 내역 보기
    @PostMapping("/master/paymentCancelAndRefund")
    public List<OrderDetailEntity> getCancelAndRefund() {
    
        return masterService.getCancelAndRefundDetails();
    }

    // 결제 취소 처리
    @PostMapping("/master/paymentCancel")
    public String getCancelOrder(
        @RequestParam Long orderDetailSeqno,
        @RequestParam(value = "couponSeqno", required = false) Long couponSeqno,
        @RequestParam(value = "point", required = false) int point
    ) {
        masterService.cancelOrRefundOrder(orderDetailSeqno,couponSeqno, point, false);

        return "{\"message\":\"good\"}";
    }
  
    // 환불 처리
    @PostMapping("/master/paymentRefund")
    public String getRefundOrder(
        @RequestParam Long orderDetailSeqno,
	      @RequestParam(value = "couponSeqno", required = false) Long couponSeqno,
	      @RequestParam(value = "point", required = false) int point
    ) {
        masterService.cancelOrRefundOrder(orderDetailSeqno, couponSeqno, point, true);

        return "{\"message\":\"good\"}";
    }  

    //매출, //통계 (관심카테고리, 찜목록, 구매) 
    @GetMapping("/master/Status")
    public void getStatus() {
    }   

    //전체 회원의 누적구매금액을 조회 후 등급 업데이트
    @PostMapping("/master/gradeUpdate")
    public String updateCustomerGrade() {
        LocalDateTime referenceDate = LocalDateTime.now();
        
        masterService.calculateAndUpdateCustomerGrade(referenceDate);

        return "{\"message\":\"good\"}";
    }

    
    // 관리자가 쿠폰 종료일이 지난 쿠폰들을 isExpired를 "Y"로 업데이트해서 만료처리
    @PostMapping("/master/expiredUpdate")
    public void updateExpiredCoupons() {
        LocalDateTime referenceDate = LocalDateTime.now();

        // 만료된 쿠폰을 처리하는 서비스 메서드 호출
        masterService.setExpiredCouponsToExpired(referenceDate);

        // 성공적인 처리 후 아무 것도 반환하지 않음 (HTTP 상태 200)
    }
    

    //통계 페이지(매출통계,가입통계,방문통계)
    @GetMapping("/master/statisticsPage")
    public void getStatisticsPage() {
    }

    /* 매출통계
    -카테고리별 
    -일별 
    -월별 
    -연령대별 
    -회원별 
    -등급별 
    -상품별 
    */
    //-카테고리별 매출 통계
    @GetMapping("/master/salesByCategory")
    public void getSalesByCategoryPage() {
    }

    @GetMapping("/master/salesByCategoryData")
    @ResponseBody
    public List<CategorySalesDTO> getCategorySalesData() {
        return masterService.getCategorySales(); // JSON 데이터를 반환
    }

    //일별 매출 통계
    @GetMapping("/master/salesByDaily")
    public void getSalesByDailyPage() {
    }

    @GetMapping("/master/salesByDailyData")
    @ResponseBody
    public List<DailySalesDTO> getDailySales(
        @RequestParam("startDateTime")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
        @RequestParam("endDateTime")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        return masterService.getDailySales(startDateTime, endDateTime);
    }

    //월별 매출 통계
    @GetMapping("/master/salesByYear")
    public void getSalesByYearPage() {
    }
    
    @GetMapping("/master/salesByYearData")
    @ResponseBody
    public List<MonthlySalesDTO> getYearlySales(
        @RequestParam("year") int year) {
        return masterService.getYearlySales(year);
    }

    //회원별 매출 통계
    @GetMapping("/master/salesByMember")
    public void getSalesByMemberPage() {
    }
    
    @GetMapping("/master/salesByMemberData")
    @ResponseBody
    public List<MemberSalesDTO> getMemberSales() {
        return masterService.getMemberSales();
    }

    //연령대별 매출 통계
    @GetMapping("/master/salesByAge")
    public void getSalesByAgePage() {
    }
    
    @GetMapping("/master/salesByAgeData")
    @ResponseBody
    public List<SalesByAgeGroupDTO> getSalesByAge() {
        return masterService.getSalesByAge();
    }

    //등급별 매출 통계
    @GetMapping("/master/salesByGrade")
    public void getSalesByGradePage() {
    }
    
    @GetMapping("/master/salesByGradeData")
    @ResponseBody
    public List<SalesByMemberGradeDTO> getSalesByGrade() {
        return masterService.getSalesByGrade();
    }
    
    //상품별 매출 통계
    @GetMapping("/master/salesByProduct")
    public void getSalesByProductPage() {
    }
    
    @GetMapping("/master/salesByProductData")
    @ResponseBody
    public List<ProductSalesDTO> getProductSales() {
        return masterService.getSalesByProduct();
    }
    
    /* 
    //가입통계
    -가입일 기준
    -성별
    -연령
    */

    //가입일 기준 가입 통계
    @GetMapping("/master/signupDateStat")
    public void getSignupDateStatPage() {
    }
    
    @GetMapping("/master/signupDateStatData")
    @ResponseBody
    public List<SignupDateStatDTO> getSignupDateStat(
        @RequestParam("startDateTime") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
        @RequestParam("endDateTime") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {

        try {
            // Service를 호출하여 통계 데이터를 가져옵니다.
            return masterService.getSignupDateStat(startDateTime, endDateTime);
        } catch (Exception e) {
            // 예외 발생 시 JSON 형식으로 오류 메시지 반환
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", e);
        }
    }

    //성별 기준 가입 통계
    @GetMapping("/master/signupGenderStat")
    public void getSignupGenderStatPage() {
    }
    
    @GetMapping("/master/signupGenderStatData")
    @ResponseBody
    public List<SignupGenderStatDTO> getSignupGenderStat() {
        return masterService.getSignupGenderStat();
    }

    //연령대 기준 가입 통계
    @GetMapping("/master/signupAgeStat")
    public void getSignupAgeStatPage() {
    }
    
    @GetMapping("/master/signupAgeStatData")
    @ResponseBody
    public List<SignupAgeStatDTO> getSignupAgeStat() {
        return masterService.getSignupAgeStat();
    }

     /*
    //방문통계
    -방문자수 
    */

    // 일별 방문자 통계 페이지
    @GetMapping("/master/visitorsByDaily")
    public void getVisitorsByDailyPage() {
    }

    @GetMapping("/master/visitorsByDailyData")
    @ResponseBody
    public List<DailyVisitorDTO> getDailyVisitors(
        @RequestParam("startDateTime")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
        @RequestParam("endDateTime") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        
        return masterService.getDailyVisitors(startDateTime, endDateTime);
    }
}


