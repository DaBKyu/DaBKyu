package com.dabkyu.dabkyu.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.repository.CouponRepository;
import com.dabkyu.dabkyu.entity.repository.MasterRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.ProductFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.service.MasterService;
import com.dabkyu.dabkyu.service.MemberService;
import com.dabkyu.dabkyu.util.PageUtil;

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
    private final MemberService memberService;
    private final MasterRepository masterRepository;
    private final MasterService masterService;
    private final CouponRepository couponRepository;
    
    //리뷰
    //문의

    //메인페이지
    @GetMapping("/manageBack") //manageback??
    public void getManageback() {}

    //고객정보페이지
    @GetMapping("/manageBack/client")
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

    //고객페이지 상세보기 >> 필요?


    //고객 삭제 기능..? 
    @Transactional
    @GetMapping("/manageBack/clientdelete")
    public String deleteClient(@RequestParam("email") String email) throws Exception{
 
		
		masterService.clientDelete(email); 

		return "redirect:/manageBack/client";

    }

    //고객정보 수정 화면보기
    @GetMapping("/manageBack/clientmodify")
    public void modifyViewClient() {


    }

    //고객정보 수정 (등급) >> 따로 화면? OR 리스트에서 바로 수정 가능? 
    //수동? 자동? 
    //수동 > 직접 수정 가능하게... ???????
    //자동 > 기간, 소비금액 총액 계산 후 자동 변경 기능... ?????
    //수정필요
    @PostMapping("/manageBack/clientmodify")
    public String modifyClient(@RequestParam("email") String email,
                            @RequestParam("memberGrade") String memberGrade) throws Exception {

        MemberEntity memberEntity = masterService.getMemberByEmail(email);
        memberEntity.setMemberGrade(memberGrade);
        masterService.saveMemberGrade(memberEntity);

        return "redirect:/manageBack/client";
        
    }

    //적립금 관리.. > 등급별로 설정 가능하게..? 
    //등급 <-> 적립금 % 조절 가능하게..? 
    @PostMapping("manageBack/point")
    public String modifyPoint() {

        //아직
        return "redirect:/manageBack/client";
    }

    //이메일 작성 화면보기
    @GetMapping("manageBack/clientemail")
    public void sendEmail() {
        //아직

    }

    //이메일 작성

    //이메일 발송
    // @PostMapping("/send")
    // public String sendEmail(@RequestBody EmailRequest emailRequest) {
    //     emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
    //     return "Email sent successfully!";
    // } 
    
    //상품 리스트 화면보기
    //수정필요
    //원본제품설명이미지?? 이미지파일 불러와야함. .
    /*
    @GetMapping("/manageBack/productlist")
    public void getProductPost(
        Model model,
        @RequestParam("page")
        int pageNum,
		@RequestParam(name="keyword1",defaultValue="",required=false)
        Long keyword1,
        @RequestParam(name="keyword2",defaultValue="",required=false)
        String keyword2
    ) throws Exception{

        int postNum = 10; 
        int pageListCount = 10; 
        
        PageUtil page = new PageUtil();
        Page<ProductEntity> productList = masterService.productList(pageNum, postNum, keyword1, keyword2);
        int totalCount = (int)productList.getTotalElements();

        model.addAttribute("productList", productList);
        model.addAttribute("listIsEmpty", productList.hasContent()?"N":"Y");
        model.addAttribute("totalElement", totalCount);
        model.addAttribute("postNum", postNum);
        model.addAttribute("page", pageNum);
        model.addAttribute("keyword", keyword1);
        model.addAttribute("keyword", keyword2);
        model.addAttribute("pageList", page.getPageProduct(pageNum, postNum, pageListCount,totalCount,keyword1, keyword2));
}
 */

    //상품 이미지 보기...? 


    //상품 등록 화면보기 
    @GetMapping("/manageBack/productpost") 
    public void getProduct() {}

    //상품 수정 화면보기 >> 수정 눌렀을 때
    @GetMapping("/manageBack/productmodify")
    public void modifyProduct() {}

    //상품 등록 
    @ResponseBody
    @PostMapping("/manageBack/productpost")
    public String postProduct(ProductDTO productDTO, @RequestParam("kind") String kind,
                            @RequestParam(name="sendToFileList",required=false) List<MultipartFile> multipartFile,
                            @RequestParam(name="deleteFileList",required=false) Long[] deleteFileList) throws Exception{
        
        String os = System.getProperty("os.name").toLowerCase();
		String path;
		if(os.contains("win"))
			path = "c:\\Repository\\file\\";
		else 
			path = "/home/gladius/Repository/file";
        
        File p = new File(path);
        if(!p.exists()) p.mkdir();
        
        //상품등록 >> 카테고리는???
        //아직
        
        //상품등록 수정

        
        //상품이미지파일 >> 상품 이미지, 상품 상세페이지 이미지 


        //상품페이지 미리보기...

        return null; //
        

    }

    //상품 삭제 /////////////////////////////수정 필요. 활성 비활성))))
    @Transactional
	@GetMapping("/manageBack/productdelete") 
    public String productDelete(@RequestParam("productSeqno") Long productSeqno) throws Exception {

        Map<String, Object> data = new HashMap<>();
		data.put("kind", "B");
		data.put("seqno", productSeqno);
		
		masterService.deleteProductFileList(data);
		masterService.deleteProductList(productSeqno); 

        return "redirect:/manageBack/productlist";
    }

    //주문내역 리스트 화면보기  ///수정수정수정서웅ㅇ서줭수정써ㅜㅈ엇
    //추가상품 정보 O -> 추가
    //제품 옵션 정보 O -> 추가 
    @GetMapping("/manageBack/order")
    public void getOrder(@RequestParam("orderSeqno") Long orderSeqno,Model model) {





        
    }

    //주문내역 상세보기... 
    //order_detail > order_product, order_info / product>product_name
    @GetMapping("/manageBack/orderview")
    public void getOrderView() {
        //아직
    }

    //주문 상태 변경
    @ResponseBody
    @PostMapping("/manageBack/order")
    public void modifyOrder() {
        //아직

    }

    //카테고리 화면보기
    @GetMapping("/manageBack/category")
    public void getCategory() {
        //아직

    }

    //카테고리 추가 화면보기
    @GetMapping("/manageBack/categorypost")
    public void postViewCategory() {}

    //카테고리 수정 화면보기
    //아직

    //카테고리 추가 
    @ResponseBody
    @PostMapping("/manageBack/categorypost")
    public void postCategory() {
        //카테고리 추가
        //아직
        //카테고리 수정

    }

    //카테고리 삭제
    @Transactional
	@GetMapping("/manageBack/categorydelete") 
    public String deleteCategory() {
        //아직
        return "redirect:/manageBack/category";
    }

    //상품문의 확인, 답변은 링크 타고 들어가게..? 
    @GetMapping("/manageBack/questionview")
    public void getQuestion() {
        //아직

    }

    //리뷰 리스트 화면 
    @GetMapping("/manageBack/responsereview")
    public void getReview(Model model, @RequestParam("page") int pageNum, 
                @RequestParam(name="keyword",defaultValue="",required=false) 
                Long keyword) throws Exception{
        
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

    //리뷰 상세보기?? > 링크 타고 들어가게? ???????????????
    //아직

    //리뷰 삭제
    @GetMapping("/manageBack/reviewdelete")
    public String deleteResponse(@RequestParam("reviewSeqno") Long reviewSeqno) throws Exception{

        Map<String, Object> data = new HashMap<>();
		data.put("kind", "B");
		data.put("reviewSeqno", reviewSeqno);

        masterService.deleteReviewFileList(data);
		masterService.deleteReview(reviewSeqno); 

        return "redirect:/manageBack/responsereview";
    }

    //쿠폰 리스트보기
    //couponCategory -> coupon_seqno , category3seqno
    //couponTarget -> coupon_seqno, product_seqno
    @GetMapping("/manageBack/coupon")
    public void getCoupon(Model model,@RequestParam("page") int pageNum,
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
    @GetMapping("/manageBack/couponpost")
    public void getPostCoupon() {}

    //쿠폰 수정 화면보기
    @GetMapping("/manageBack/couponmodify")
    public void getModifyCoupon() {}

    //쿠폰 등록, 수정
    //수정필요
    @ResponseBody
    @PostMapping("/manageBack/couponpost")
    public void postCoupon(CouponDTO coupon, @RequestParam("kind") String kind) throws Exception{
       
        if(kind.equals("I")){
            masterService.writeCoupon(coupon);

            //쿠폰 적용 범위를 카테고리 검색으로. 검색도 가능. 여러개 선택도 가능... 
            //권한설정: coupon_role > 맴버등급으로.
            //사용옵션: 추가 할인 가능 >> 이건 뭐지?? 체크표시... 

        }

        if(kind.equals("U")){
            masterService.modifyCoupon(coupon);    
        }

    }
    
    //쿠폰-사용자 배포
    //자동: 특정 회원 지정 발행, 웅영자 지정 대상 자동 발행(신규회원, 첫주문 완료 회원, 생일인 회원)
    //수동: 고객 다운로드, 운영자의 쿠폰코드 생성
    @PostMapping()
    //아직


    //쿠폰 삭제
    //수정필요. >> 삭제가 아니라 사용만료, 기간만료에 들어가게 됨. 
    //             사용조건이 맞지 않더라도 위치 옮김. (특정 등급 제한 쿠폰일 경우, 기간 만료 전 등급이 변경됐을 때 etc.)
    @GetMapping("/manageBack/coupondelete")
    public String deleteCoupon(@RequestParam("couponSeqno") Long couponSeqno) throws Exception{
        Map<String, Object> data = new HashMap<>();
		data.put("kind", "B"); //B??
		data.put("seqno", couponSeqno);
        masterService.deleteCoupon(couponSeqno);
        return "redirect:/manageBack/coupon";
    }

    
    //매출, //통계 (관심카테고리, 찜목록, 구매) ??????????????
    @GetMapping("/manageBack/status")
    public void getStatus() {

        //아직
    }
    
     // 결제 취소 및 환불 신청 내역 보기
    @GetMapping("/master/paymentCancelAndRefund")
    public List<OrderDetailEntity> getCancelAndRefund() {
    
        return masterService.getCancelAndRefundDetails();
    }

    // 결제 취소 처리
    @PostMapping("/master/paymentCancel")
    public String cancelOrder
    (@RequestParam Long orderDetailSeqno,
    @RequestParam(value = "couponSeqno", required = false) Long couponSeqno,
	@RequestParam(value = "point", required = false) int point
    ) {
        masterService.cancelOrRefundOrder(orderDetailSeqno,couponSeqno, point, false);

        return "{\"message\":\"good\"}";
    }

    // 환불 처리
    @PostMapping("/master/paymentRefund")
    public String refundOrder
    (@RequestParam Long orderDetailSeqno,
	@RequestParam(value = "couponSeqno", required = false) Long couponSeqno,
	@RequestParam(value = "point", required = false) int point
    ) {
        masterService.cancelOrRefundOrder(orderDetailSeqno, couponSeqno, point, true);

        return "{\"message\":\"good\"}";
    }

    //전체 회원의 누적구매금액을 조회 후 등급 업데이트
    @PostMapping("/master/gradeUpdate")
    public String updateCustomerGrade() {
        LocalDateTime referenceDate = LocalDateTime.now();
        
        masterService.calculateAndUpdateCustomerGrade(referenceDate);

        return "{\"message\":\"good\"}";
    }
}


