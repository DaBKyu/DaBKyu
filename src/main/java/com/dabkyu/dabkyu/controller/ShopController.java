package com.dabkyu.dabkyu.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dabkyu.dabkyu.dto.MemberAddressDTO;
import com.dabkyu.dabkyu.dto.MemberCouponDTO;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.OrderInfoDTO;
import com.dabkyu.dabkyu.dto.OrderProductDTO;
import com.dabkyu.dabkyu.dto.QuestionDTO;
import com.dabkyu.dabkyu.dto.QuestionFileDTO;
import com.dabkyu.dabkyu.dto.ReviewDTO;
import com.dabkyu.dabkyu.dto.ReviewFileDTO;
import com.dabkyu.dabkyu.dto.TopSellingProductDTO;
import com.dabkyu.dabkyu.entity.MemberReviewLikeEntity;
import com.dabkyu.dabkyu.dto.ReportDTO;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.ShoppingCartEntity;
import com.dabkyu.dabkyu.service.MemberService;
import com.dabkyu.dabkyu.service.ProductService;
import com.dabkyu.dabkyu.service.ProductServiceImpl.TopProduct;
import com.dabkyu.dabkyu.service.QuestionService;
import com.dabkyu.dabkyu.service.ReviewService;
import com.dabkyu.dabkyu.service.ShoppingCartService;
import com.dabkyu.dabkyu.util.PageUtil;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ShopController {

    private final ProductService productService;
	private final ShoppingCartService shoppingCartService;
	private final MemberService memberService;
	private final QuestionService questionService;
	private final ReviewService reviewService;

	@GetMapping("/shop/main")
    public String getProductList(Model model, 
                                 @RequestParam(name = "page", defaultValue = "1") int pageNum,
                                 @RequestParam(name = "keyword", defaultValue = "", required = false) String keyword) throws Exception {

        int postNum = 10; // 한 페이지에 표시할 상품 개수
        int pageListCount = 10; // 페이지 하단에 표시할 페이지 번호 개수

        // 상품 목록 페이징 처리
        Page<ProductEntity> productList = productService.findProductList(pageNum, postNum, keyword);
        int totalCount = (int) productList.getTotalElements(); // 전체 상품 개수

        // 모델에 데이터 추가
        model.addAttribute("list", productList.getContent()); // 상품 목록
        model.addAttribute("totalElement", totalCount);
        model.addAttribute("postNum", postNum);
        model.addAttribute("page", pageNum);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", new PageUtil().getMainPageList(pageNum, postNum, pageListCount, totalCount, keyword));
        
        return "shop/main"; 
    }

	//상품 목록 보기(카테고리 조회)
	@GetMapping("/shop/list")
	public String getList(Model model, 
						@RequestParam("page") int pageNum,
						@RequestParam(name = "keyword", defaultValue = "", required = false) String keyword,
						@RequestParam(name = "category1Seqno", defaultValue = "", required = false) Long category1Seqno,
						@RequestParam(name = "category2Seqno", defaultValue = "", required = false) Long category2Seqno,
						@RequestParam(name = "category3Seqno", defaultValue = "", required = false) Long category3Seqno) throws Exception {
		
		int postNum = 10; // 한 페이지에 표시할 상품 개수
		int pageListCount = 10; // 페이지 하단에 표시할 페이지 번호 개수
		
		// 페이징 처리
		Page<ProductEntity> list = productService.list(pageNum, postNum, keyword, category1Seqno, category2Seqno, category3Seqno);
		int totalCount = (int) list.getTotalElements(); // 전체 상품 개수
		
		// 모델에 데이터 추가
		model.addAttribute("list", list);
		model.addAttribute("listIsEmpty", list.hasContent() ? "N" : "Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("category1Seqno", category1Seqno);
		model.addAttribute("category2Seqno", category2Seqno);
		model.addAttribute("category3Seqno", category3Seqno);
		model.addAttribute("pageList", new PageUtil().getProductList(pageNum, postNum, pageListCount, totalCount, keyword, category1Seqno, category2Seqno, category3Seqno));
		
		return "shop/list"; 
	}

	//가장 많이 팔린 상품 10개 조회
	@ResponseBody
	@GetMapping("/shop/topSelling")
	public List<TopSellingProductDTO> getTopSellingProducts() throws Exception {
		return productService.getTop10BestSellingProducts();
	}
	
	/* 
	//로그인한 사용자의 연령대별 가장 많이 팔린 상품 10개 조회 테스트
	@ResponseBody
	@GetMapping("/shop/topProductsByAgeForLoggedUser")
	public Map<String, List<TopProduct>> getTopProductsForLoggedUser() throws Exception {
		// 하드코딩된 테스트용 이메일
		String email = "1234@example.com";  // 로그인된 사용자가 아닌 테스트용으로 하드코딩된 이메일을 사용
	
		// 서비스 메서드 호출
		return productService.getTopProductsByAgeForUser(email);
	}
	*/
	
	//로그인한 사용자의 연령대별 가장 많이 팔린 상품 10개 조회
	@ResponseBody
	@GetMapping("/shop/topProductsByAgeForLoggedUser")
    public Map<String, List<TopProduct>> getTopProductsForLoggedUser() throws Exception {
        // 로그인된 사용자 정보에서 이메일을 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();  // 로그인한 사용자의 이메일
            return productService.getTopProductsByAgeForUser(email);
        }
        throw new IllegalStateException("사용자가 로그인되지 않았습니다.");
    }

	/* 
	// 연령대별 가장 많이 팔린 상품 10개 조회
	@ResponseBody
	@GetMapping("/shop/topProductsByAge")
	public Map<String, List<TopProduct>> getTopProductsByAgeGroup() throws Exception{
		System.out.println("getTopProductsByAgeGroup 호출됨");  // 메서드 호출 확인용 로그
		return productService.getTop10ProductsByAgeGroup();
	}
	*/
    
    //상품 상세 보기
	@GetMapping("/shop/view")
	public void getView(@RequestParam("productSeqno") Long productSeqno, @RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword,
			Model model, HttpSession session) throws Exception {

		//String sessionEmail = (String)session.getAttribute("email");

        model.addAttribute("view", productService.view(productSeqno));
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pre_seqno", productService.pre_seqno(productSeqno,keyword));		
		model.addAttribute("next_seqno", productService.next_seqno(productSeqno,keyword));
		model.addAttribute("fileListView", productService.fileListView(productSeqno));
		
		// 상품의 옵션과 추가상품을 조회하여 추가
		model.addAttribute("productOptions", productService.getProductOptions(productSeqno));
		model.addAttribute("relatedProducts", productService.getRelatedProducts(productSeqno));
    }

	// 장바구니 보기
	@ResponseBody
	@GetMapping("/purchase/cart")
	public List<ShoppingCartEntity> getCartItems(HttpSession session) throws Exception {
		String email = (String)session.getAttribute("email");
		return shoppingCartService.getCartItems(email);
}

    // 장바구니에 상품 추가
    @PostMapping("/purchase/addcart")
    public String addToCart(
		@RequestBody OrderProductDTO orderProduct,
		HttpSession session,
		@RequestParam(name="relatedProductList",required=false)
		Map<Long, Integer> relatedProducts,
		@RequestParam(name="productOptionList",required=false)
		List<Long> productOptions
	) throws Exception {

		String email = (String)session.getAttribute("email");
		MemberDTO member = memberService.memberInfo(email);

		// 관련 상품 처리		
        shoppingCartService.addToCart(orderProduct, member, relatedProducts, productOptions);
	
		return "{\"message\":\"good\"}";
    }

	// 장바구니에 등록된 상품 수량 수정
    @PutMapping("/purchase/updateCartItemQuantity")
    public String updateCartItemQuantity(
            @RequestParam String email,
            @RequestParam Long orderProductSeqno,
            @RequestParam int newQuantity
	) throws Exception {
		try {
			shoppingCartService.updateCartItemQuantity(email, orderProductSeqno, newQuantity);
			return "{\"message\":\"good\"}";
		} catch (RuntimeException e) {
			return  e.getMessage();
		}
	}


    // 장바구니에서 특정 상품 삭제
    @PostMapping("/purchase/removecart")
    public String removeFromCart(HttpSession session, @RequestParam("orderProductSeqno") Long orderProductSeqno) throws Exception {
		String email = (String)session.getAttribute("email");
        shoppingCartService.removeFromCart(email, orderProductSeqno);
        return "{\"message\":\"good\"}";
    }

    // 장바구니에서 모든 상품 삭제
    @PostMapping("/purchase/clearcart")
    public String clearCart(HttpSession session) throws Exception {
		String email = (String)session.getAttribute("email");
        shoppingCartService.clearCart(email);
        return "{\"message\":\"good\"}";
    }

	// 결제 화면 보기
	@GetMapping("/purchase/pay")
	public void getPay(
		HttpSession session,
		@RequestParam("toPayOrderProductList")
		List<Long> toPayOrderProductList,
		@RequestParam("orderInfo") OrderInfoDTO orderInfo,
		@RequestParam("memberAddress") MemberAddressDTO memberAddress,
		@RequestParam("memberCoupon") MemberCouponDTO memberCoupon,
		@RequestParam("member") MemberDTO member
		) {}


	// 결제 
    @PostMapping("/purchase/pay")
    public String pay(
		HttpSession session,
		@RequestParam("toPayOrderProductList")
		List<Long> toPayOrderProductList,
		@RequestParam(value = "couponSeqno", required = false) Long couponSeqno,
		@RequestParam(value = "point", required = false) int point,
		@RequestBody OrderInfoDTO orderInfo) {
		String email = (String)session.getAttribute("email");
		try {
			shoppingCartService.pay(email, toPayOrderProductList,couponSeqno, point, orderInfo);
			return "{\"message\":\"good\"}";
		} catch (RuntimeException e) {
			return "결제 실패: " + e.getMessage();
		}
    }

	// 결제 취소 신청
	@PostMapping("/purchase/cancelToPay")
	public String cancelToPay(
			HttpSession session,
			@RequestParam Long orderSeqno) {
		String email = (String)session.getAttribute("email");
		try {
			shoppingCartService.cancelToPay(email, orderSeqno);
			return "{\"message\":\"good\"}";
		} catch (RuntimeException e) {
			return e.getMessage();
		}
	}
		 
	 // 환불 신청
	 @PostMapping("/purchase/refundRequest")
	 public String requestRefund(
			 HttpSession session,
			 @RequestParam Long orderSeqno) { 
		 String email = (String) session.getAttribute("email");
		 try {
			 shoppingCartService.refundRequest(email,orderSeqno);
			 return "{\"message\":\"good\"}";
		 } catch (RuntimeException e) {
			 return e.getMessage();
		 }
	 }

	// 상품 문의 내역 보기
	@GetMapping("/purchase/questionList")
	public void getQuestionList(Model model,@RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword) throws Exception {
		
		int postNum = 10; //한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 10; //화면 하단에 보여지는 페이지리스트의 페이지 갯수	
		
		PageUtil page = new PageUtil();
		Page<QuestionEntity> list = questionService.list(pageNum, postNum, keyword);
		int totalCount = (int)list.getTotalElements();

		model.addAttribute("list", list);
		model.addAttribute("listIsEmpty", list.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList", page.getQuestionList(pageNum, postNum, pageListCount,totalCount,keyword));
	
	}

	// 상품 문의 상세 보기
	@GetMapping("/purchase/questionView")
	public void getQuestionView(@RequestParam("queSeqno") Long queSeqno, @RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword,
			Model model, HttpSession session) throws Exception {

		//세션 email값 가져 오기
		String sessionEmail = (String)session.getAttribute("email");
		
		model.addAttribute("view", questionService.view(queSeqno));
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pre_seqno", questionService.pre_seqno(queSeqno,keyword));		
		model.addAttribute("next_seqno", questionService.next_seqno(queSeqno,keyword));
		model.addAttribute("fileListView", questionService.fileListView(queSeqno));	
	}
	

	// 문의 작성(일반,상품,배송)
	@ResponseBody
	@PostMapping("/purchase/writeQuestion")
	public String postQuestion(QuestionDTO question,
			@RequestParam(name="sendToFileList",required=false) List<MultipartFile> multipartFile
			) throws Exception {	
			
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 시작
		String os = System.getProperty("os.name").toLowerCase();
		String path;
		if(os.contains("win"))
			path ="c:\\Repository\\question\\";
		else 
		path = "/home/nayoung/Repository/question";
		
		//디렉토리가 존재하는지 체크해서 없다면 생성
		File p = new File(path);
		if(!p.exists()) p.mkdir();
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 종료
			
		Long seqno = 0L;		

		//게시물 등록
		questionService.write(question);
		seqno = questionService.getMaxSeqno(question.getEmail().getEmail());
		

		//첨부 파일이 존재하면
		if(multipartFile != null) {			
			File targetFile = null;
						
			for(MultipartFile mpr:multipartFile) {
				
				String orgFilename = mpr.getOriginalFilename();
				String orgFileExtension = orgFilename.substring(orgFilename.lastIndexOf("."));			
				String storedFilename = UUID.randomUUID().toString().replaceAll("-", "") + orgFileExtension;
				
				try {
					targetFile = new File(path + storedFilename);				
					mpr.transferTo(targetFile);
					
					QuestionFileDTO fileInfo =QuestionFileDTO.builder()
											.questionFileSeqno(seqno)
											.orgFilename(orgFilename)
											.storedFilename(storedFilename)
											.build();
					
					questionService.fileInfoRegistry(fileInfo);
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		return "{\"message\":\"good\"}";
	}

	// 상품 리뷰 내역 보기
	@GetMapping("/shop/reviewList")
	public void getReviewList(Model model,@RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword) throws Exception {
		
		int postNum = 10; //한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 10; //화면 하단에 보여지는 페이지리스트의 페이지 갯수	
		
		PageUtil page = new PageUtil();
		Page<ReviewEntity> list = reviewService.list(pageNum, postNum, keyword);
		int totalCount = (int)list.getTotalElements();

		model.addAttribute("list", list);
		model.addAttribute("listIsEmpty", list.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList", page.getReviewList(pageNum, postNum, pageListCount,totalCount,keyword));
	}

	// 상품 리뷰 상세 보기
	@GetMapping("/shop/reviewView")
	public void getReviewView(@RequestParam("reviewSeqno") Long reviewSeqno, @RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword,
			Model model, HttpSession session) throws Exception {

		//세션 email값 가져 오기
		String sessionEmail = (String)session.getAttribute("email");
		
		model.addAttribute("view", reviewService.view(reviewSeqno));
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pre_seqno", reviewService.pre_seqno(reviewSeqno,keyword));		
		model.addAttribute("next_seqno", reviewService.next_seqno(reviewSeqno,keyword));
		model.addAttribute("fileListView", reviewService.fileListView(reviewSeqno));	
	}

	// 상품 리뷰 작성
	@ResponseBody
	@PostMapping("/shop/writeReview")
	public String postReview(ReviewDTO review,
			@RequestParam(name="sendToFileList",required=false) List<MultipartFile> multipartFile) throws Exception {		
		
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 시작
		String os = System.getProperty("os.name").toLowerCase();
		String path;
		if(os.contains("win"))
			path = "c:\\Repository\\file\\";
		else 
			path = "/home/nayoung/Repository/file";
		
		//디렉토리가 존재하는지 체크해서 없다면 생성
		File p = new File(path);
		if(!p.exists()) p.mkdir();
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 종료
		
		Long seqno = 0L;		

		//게시물 등록
		reviewService.write(review);
		seqno = reviewService.getMaxSeqno(review.getEmail().getEmail());
		
		//첨부 파일이 존재하면
		if(multipartFile != null) {			
			File targetFile = null;
						
			for(MultipartFile mpr:multipartFile) {
				
				String orgFilename = mpr.getOriginalFilename();
				String orgFileExtension = orgFilename.substring(orgFilename.lastIndexOf("."));			
				String storedFilename = UUID.randomUUID().toString().replaceAll("-", "") + orgFileExtension;
				
				try {
					targetFile = new File(path + storedFilename);				
					mpr.transferTo(targetFile);
					
					ReviewFileDTO fileInfo = ReviewFileDTO.builder()
											.reviewFileSeqno(seqno)
											.orgFilename(orgFilename)
											.storedFilename(storedFilename)
											.build();
					
					reviewService.fileInfoRegistry(fileInfo);
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		return "{\"message\":\"good\"}";
	}
	// 상품 리뷰 삭제
	@Transactional
	@GetMapping("/shop/reviewDelete")
	public String getDelete(@RequestParam("reviewSeqno") Long reviewSeqno) throws Exception {

		Map<String, Object> data = new HashMap<>();
		data.put("kind", "B");
		data.put("reviewSeqno", reviewSeqno);
		
		reviewService.deleteFileList(data);
		reviewService.delete(reviewSeqno); 

		return "{\"message\":\"good\"}";
	}

	//상품 리뷰 도움이되었어요 관리
	@ResponseBody
	@PostMapping("/shop/likeCheck")
	public String postLikeCheck(@RequestBody Map<String,Object> likeCheckData) throws Exception {
		
		int seq = (int)likeCheckData.get("reviewSeqno");
		Long reviewSeqno = (long)seq;
		String email = (String)likeCheckData.get("email");
		int checkCnt = (int)likeCheckData.get("checkCnt");		
		
		//MemberReviewLike 테이블 입력/수정
		MemberReviewLikeEntity likeCheckView = reviewService.likeCheckView(reviewSeqno,email);
		if(likeCheckView == null) //도움이되었어요를 등록한 적이 없음 
			reviewService.likeCheckRegistry(reviewSeqno, email);
		else //도움이되었어요를 등록한 적이 있음
			reviewService.likeCheckUpdate(reviewSeqno, email); 
		
		int likeCnt = reviewService.view(reviewSeqno).getLikecnt();

		switch(checkCnt) {
		case 1: likeCnt--; break;
		case 2: likeCnt++; break;
		}

		ReviewDTO review = ReviewDTO.builder()
									.reviewSeqno(reviewSeqno)
									.likecnt(likeCnt)
									.build();
		reviewService.reviewLikeUpdate(review);
		
		return "{\"likecnt\":\"" + likeCnt + "\"}";
	}


	// 리뷰 신고 화면
	@GetMapping("/purchase/report")
	public void getReport(
		@RequestParam(name="reviewSeqno")
		Long reviewSeqno,
		Model model
	) {
		model.addAttribute("reviewSeqno", reviewSeqno);
	}
	
	// 리뷰 신고
	@PostMapping("/purchase/report")
	public String postReport(
		ReportDTO report
	) throws Exception {

		productService.report(report);
		
		return "{\"message\":\"good\"}";
	}
	
   
}
    


