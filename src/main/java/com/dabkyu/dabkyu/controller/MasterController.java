package com.dabkyu.dabkyu.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Locale.Category;

import org.springframework.data.domain.Page;
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

import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.OrderInfoDTO;
import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.dto.QuestionDTO;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.repository.Category3Repository;
import com.dabkyu.dabkyu.entity.repository.CouponRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.ProductFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.service.MasterService;
import com.dabkyu.dabkyu.service.MemberService;
import com.dabkyu.dabkyu.util.PageUtil;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@AllArgsConstructor
public class MasterController{

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductFileRepository productFileRepository;
    private final MasterService masterService;
    private final CouponRepository couponRepository;
    private final Category3Repository category3Repository;
    
    //리뷰
    //문의

    //메인페이지
    @GetMapping("/master") 
    public void getMaster() {}

    //고객정보페이지
    @GetMapping("/master/client")
    public void getClient(Model model, @RequestParam("page") int pageNum,
            @RequestParam(name="keyword",defaultValue="",required=false) String keyword) throws Exception {
       
        int postNum = 10; //한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 10; //화면 하단에 보여지는 페이지리스트의 페이지 갯수	
		
		PageUtil page = new PageUtil();
		Page<MemberEntity> list = masterService.memberList(pageNum, postNum, keyword);
		int totalCount = (int)list.getTotalElements();

		model.addAttribute("list", list);
		model.addAttribute("listIsEmpty", list.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList", page.getPageClient(pageNum, postNum, pageListCount,totalCount,keyword));
    }

    //고객 삭제 기능
    @Transactional
    @GetMapping("/master/deleteClient")
    public String getDeleteClient(@RequestParam("email") String email) throws Exception{
 
		masterService.clientDelete(email); 

		return "redirect:/"; ////
    }

    //고객정보 수정 화면보기
    @GetMapping("/master/modifyClient/{email}")
    public void getModifyClient(@PathVariable String email, Model model) {

        model.addAttribute("member", masterService.getMemberByEmail(email));
    }

    //고객정보 수정 
    @PostMapping("/master/modifyViewClient/{email}")
    public String postModifyClient(@ModelAttribute MemberDTO memberDTO) throws Exception {

        masterService.memberModify(memberDTO);

        return "redirect:/"; ////   
    }

    //이메일 작성 화면보기
    @GetMapping("/master/sendEmail")
    public void getSendEmail() {
        //

    }

    //이메일 작성

    //이메일 발송 
    
    //상품 리스트 화면보기
    @GetMapping("/master/productList")
    public void getProductList(Model model, 
                            @RequestParam("page") int pageNum, 
                            @RequestParam(name="keyword",defaultValue="",required=false) Long keyword1,
                            @RequestParam(name="keyword",defaultValue="",required=false) String keyword2) throws Exception{
        int postNum = 10;
        int pageListCount = 10;

        PageUtil page = new PageUtil();
        Page<ProductDTO> productList = masterService.productList(pageNum, postNum, keyword1, keyword2);
        int totalCount = (int)productList.getTotalElements();

        model.addAttribute("list", productList);
		model.addAttribute("listIsEmpty", productList.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword1", keyword1);
        model.addAttribute("keyword2", keyword2);
		model.addAttribute("pageList", page.getPageProduct(pageNum, postNum, pageListCount,totalCount,keyword1, keyword2));
    }

    //상품 등록 화면보기 
    @GetMapping("/master/postProduct") 
    public void getPostProduct() {


    }

    //상품 수정 화면보기 >> 수정 눌렀을 때
    @GetMapping("/master/modifyProduct")
    public void getmMdifyProduct() {


    }

    //상품 등록 및 수정  ////수정 필요. 상품이미지+썸네일
    @ResponseBody
    @PostMapping("/master/postProduct")
    public String postProduct(ProductDTO productDTO, 
                            @RequestParam("kind") String kind,
                            @RequestParam(name="productImage",required=false) List<MultipartFile> productImages,
                            @RequestParam(name="detailImage", required=false) MultipartFile detailImage,
                            @RequestParam(name="deleteProductImages", required=false) Long[] deleteProductImages,
                            @RequestParam(name="deleteDetailProductImage",required=false) Long[] deleteDetailProductImage,
                            @RequestParam(name="category1Seqno") Long category1Seqno,
                            @RequestParam(name="category2Seqno") Long category2Seqno,
                            @RequestParam(name="category3Seqno") Long category3Seqno) 
                            throws Exception{
        
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
			
        File p = new File(productImgPath);
        File d = new File(productDetailImgPath);
        if(!p.exists()) p.mkdir();
        if(!d.exists()) d.mkdir();
        
        Long seqno = 0L;	
        
        //카테고리 연결
        Category3Entity category3 = category3Repository.findByCategory3Seqno(category3Seqno);
        productDTO.setCategory3Seqno(category3);

        //등록
        if(kind.equals("I")){
            seqno = masterService.productPost(productDTO);
        }

        //수정
        if(kind.equals("U")){
            masterService.productModify(productDTO);
            seqno = productDTO.getProductSeqno();

            //상품 파일 삭제
            if (deleteProductImages != null) {
                for (Long productFileSeqno : deleteProductImages) {
                    masterService.deleteProductFile(productFileSeqno); 
                }
            }

            //상세보기 파일 삭제
            if (deleteDetailProductImage != null) {
                String storedFilename = productDTO.getInfoStoredImage(); 
                File detailImgDelete = new File(productDetailImgPath + storedFilename);
                if (detailImgDelete.exists()) {
                    detailImgDelete.delete(); 
                }
                productDTO.setInfoOrgImage(null);
                productDTO.setInfoStoredImage(null);    
            }
        }
        
        //상품이미지파일 //productfile
        if(productImages != null){
            for(MultipartFile mpr:productImages) {
                String org_filename = mpr.getOriginalFilename();
                String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));			
                String stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;

                try{
                    File targetFile = new File(productImgPath + stored_filename);				
					mpr.transferTo(targetFile);

                    ProductFileDTO fileInfo = ProductFileDTO.builder()
                                            .productFileSeqno(seqno)
                                            .orgFilename(org_filename)
                                            .storedFilename(stored_filename)
                                            .build();
                    masterService.productImgFile(fileInfo);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        //상세보기이미지파일 //product
        if(detailImage != null){
            String org_filename = detailImage.getOriginalFilename();
            String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));			
            String stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;

            try{
                File targetFile = new File(productDetailImgPath + stored_filename);				
                detailImage.transferTo(targetFile);
                /* 
                ProductDTO fileInfo = ProductDTO.builder()
                                            .infoOrgImage(org_filename)
                                            .infoStoredImage(stored_filename)
                                            .build();
                */
                productDTO.setInfoOrgImage(org_filename); 
                productDTO.setInfoStoredImage(stored_filename);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return "{\"message\":\"good\"}"; 
    }

    
    //상품 미리보기 화면


    //상품 삭제 (활성 비활성)
    @ResponseBody
	@GetMapping("/master/secretProduct") 
    public  List<ProductDTO> postSecretProduct(@RequestParam("kind") String kind,
                            @RequestParam(name="role", required=false) String role) throws Exception {

        List<ProductDTO> productList = masterService.getAllProducts();

        if("MASTER".equals(role)){
            return productList;
        }
        return productList.stream().filter(product -> "Y".equals(product.getSecretYn()))
                                   .collect(Collectors.toList());
    }

    //주문내역 리스트 화면보기  ///수정 필요
    //추가상품 정보 O -> 추가
    //제품 옵션 정보 O -> 추가 
    //카테고리 정보, 상품명
    //검색 > 주문현황 수정필요
    @GetMapping("/master/order")
    public void getOrderList(Model model, 
                            @RequestParam("page") int pageNum, 
                            @RequestParam(name="keyword",defaultValue="",required=false) String productname,
                            @RequestParam(name="keyword",defaultValue="",required=false) Long category) 
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

    //주문내역 상세보기?
    //order_detail > order_product, order_info / product>product_name
    @GetMapping("/master/orederView")
    public void getOrderView() {
        
    }

    //주문 상태 변경 //newStatus : 상품준비중 - 배송중 - 배송완료?
    @ResponseBody
    @PostMapping("/master/modifyOrder")
    public String postModifyOrder(@RequestParam("orderSeqno") Long orderSeqno,
                                @RequestParam("newStatus") String newStatus)
                                throws Exception {
        masterService.modifyOrderStatus(orderSeqno,newStatus);

        return "redirect:/master/order";
    }

    //카테고리 화면보기
    @GetMapping("/master/categoryList")
    public void getCategoryList() {
        //

    }

    //카테고리 추가 화면보기
    @GetMapping("/master/createCategory")
    public void getCreateCategory() {}

    //카테고리 수정 화면보기
    //

    //카테고리 추가 
    @ResponseBody
    @PostMapping("/master/createCategory")
    public void postCreateCategory() {
        //카테고리 추가
        //
        //카테고리 수정

    }

    //카테고리 삭제
    @Transactional
	@GetMapping("/master/deleteCategory") 
    public String getDeleteCategory() {
        //
        return "redirect:/";  /////
    }

    // 문의 리스트

    //상품문의 확인, 답변
    @GetMapping("/master/question")
    public void getQuestion(Model model, @RequestParam("page") int pageNum, 
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
        model.addAttribute("keyword", queType);
        model.addAttribute("pageList", page.getPageQuestion(pageNum, postNum, pageListCount, totalCount, queType));


    }

    //상품문의 삭제 
    

    //상품문의 답변 //답변 후 상태 변경 (답변전 -> 답변완료)

    //리뷰 리스트 화면 
    @GetMapping("/master/reviewList")
    public void getReviewList(Model model, @RequestParam("page") int pageNum, 
                @RequestParam(name="keyword",defaultValue="",required=false) Long keyword) 
                throws Exception{
        
        int postNum = 10;
        int pageListCount = 10;

        PageUtil page = new PageUtil();
		Page<ReviewEntity> reviewList = masterService.reviewList(pageNum, postNum, keyword);
		int totalCount = (int)reviewList.getTotalElements();

		model.addAttribute("list", reviewList);
		model.addAttribute("listIsEmpty", reviewList.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList", page.getPageReview(pageNum, postNum, pageListCount,totalCount,keyword));
    }

    //리뷰 상세보기
  

    //리뷰 삭제
    @GetMapping("/master/deleteReview")
    public String getDeleteReview(@RequestParam("reviewSeqno") Long reviewSeqno) throws Exception{

        Map<String, Object> data = new HashMap<>();
		data.put("kind", "B");
		data.put("reviewSeqno", reviewSeqno);

        masterService.deleteReviewFileList(data);
		masterService.deleteReview(reviewSeqno); 

        return "redirect:/";  ///
    }

    //쿠폰 리스트보기
    //couponCategory -> coupon_seqno , category3seqno
    //couponTarget -> coupon_seqno, product_seqno
    @GetMapping("/master/couponList")
    public void getCouponList(Model model,@RequestParam("page") int pageNum,
                        @RequestParam(name="keyword",defaultValue="",required=false) String keyword) 
                        throws Exception {
        int postNum = 15; 
        int pageListCount = 10; 
        
        PageUtil page = new PageUtil();
        Page<Map<String, Object>> couponPage = masterService.couponList(pageNum,postNum,keyword);
        int totalCount = (int) couponPage.getTotalElements();

        model.addAttribute("couponPage", couponPage);
		model.addAttribute("listIsEmpty", couponPage.hasContent()?"N":"Y");
        model.addAttribute("totalElement", totalCount);
        model.addAttribute("postNum", postNum);
        model.addAttribute("page", pageNum);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", page.getPageCoupon(pageNum, postNum, pageListCount,totalCount,keyword));
    }

    //쿠폰 등록 화면보기
    @GetMapping("/master/createCoupon")
    public void getCreateCoupon() {}

    //쿠폰 수정 화면보기
    @GetMapping("/master/modifyCoupon")
    public void getModifyCoupon() {}

    //쿠폰 등록, 수정
    //수정필요
    @ResponseBody
    @PostMapping("/master/createCoupon")
    public void postCreateCoupon(CouponDTO coupon, @RequestParam("kind") String kind) throws Exception{
       
        if(kind.equals("I")){
            masterService.writeCoupon(coupon);

            //쿠폰 적용 범위를 카테고리 검색으로. 검색도 가능. 여러개 선택도 가능... 
            //권한설정: coupon_role > 맴버등급으로.
            //사용옵션: 추가 할인 가능 >> 체크표시?

        }

        if(kind.equals("U")){
            masterService.modifyCoupon(coupon);    
        }

    }
    
    //쿠폰-사용자 배포
    //자동: 특정 회원 지정 발행, 웅영자 지정 대상 자동 발행(신규회원, 첫주문 완료 회원, 생일인 회원)
    //수동: 고객 다운로드, 운영자의 쿠폰코드 생성
    //@PostMapping()
    //


    //쿠폰 삭제
    //수정필요. >> 삭제가 아니라 사용만료, 기간만료에 들어가게 됨. 
    //             사용조건이 맞지 않더라도 위치 옮김. (특정 등급 제한 쿠폰일 경우, 기간 만료 전 등급이 변경됐을 때 etc.)
    @GetMapping("/master/deleteCoupon")
    public String getDeleteCoupon(@RequestParam("couponSeqno") Long couponSeqno) throws Exception{
        Map<String, Object> data = new HashMap<>();
		data.put("kind", "B"); //B??
		data.put("seqno", couponSeqno);
        masterService.deleteCoupon(couponSeqno);
        return "redirect:/";  //
    }

    
    //결제취소 확인, 처리 확인  (처리를 어떤식으로 하는가)
    @GetMapping("/master/cancelOrder")
    public void getCancelOrder() {
        //
    }

    

    //교환환불 확인, 처리 (처리를 어떤식으로 하는가)
    @GetMapping("/master/refundOrder")
    public void getRefundOrder() {
        //
        
    }

    //매출, //통계 (관심카테고리, 찜목록, 구매) 
    @GetMapping("/master/Status")
    public void getStatus() {

        //
    }

    //전체 회원의 누적구매금액을 조회 후 등급 업데이트
    @PostMapping("/manageBack/gradeUpdate")
    public String updateCustomerGrade() {

        LocalDateTime referenceDate = LocalDateTime.now();

        masterService.calculateAndUpdateCustomerGrade(referenceDate);

    return "{\"message\":\"good\"}";
    }
}


