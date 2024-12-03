package com.dabkyu.dabkyu.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;
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
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
public class ShopController {

    private final ProductService productService;
	private final ShoppingCartService shoppingCartService;
	private final MemberService memberService;
	private final QuestionService questionService;
	private final ReviewService reviewService;

	// 내 알림 화면
    @GetMapping("/shop/notice")
	public void getNotice() {
	
	}

	// 전체 상품 검색 화면
    @GetMapping("/shop/searchAll")
	public void getSearchAll(
		Model model,
		@RequestParam(name="keyword",defaultValue="",required=false) String keyword)
		throws Exception {
		
		int postNum = 12; 
		int pageListCount = 10; 
		PageUtil page = new PageUtil();

		List<ProductEntity> product = productService.productAllList(keyword);

		List<Category1Entity> mist = productService.category1List();
		List<Category2Entity> list2 = productService.category2List();
		List<Category3Entity> list3 = productService.category3List();
		
		// Category2 리스트를 category2Seqno 기준으로 오름차순 정렬
    	list2 = list2.stream()
					.sorted(Comparator.comparingLong(Category2Entity::getCategory2Seqno)) // category2Seqno 기준 오름차순 정렬
					.collect(Collectors.toList()); // 다시 리스트로 수집
		model.addAttribute("mist", mist);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
		model.addAttribute("keyword", keyword);
		model.addAttribute("product", product);
		model.addAttribute("pageList", page.getPageList1(postNum, pageListCount, keyword));
	}

	
	//사이드바 카테고리 목록 보기
	@GetMapping("/shop/main")
	public void getMain(
		Model model) throws Exception {
		List<Category1Entity> mist = productService.category1List();
		List<Category2Entity> list2 = productService.category2List();
		List<Category3Entity> list3 = productService.category3List();
		List<ProductEntity> product = productService.productList();
		// Category2 리스트를 category2Seqno 기준으로 오름차순 정렬
    	list2 = list2.stream()
					.sorted(Comparator.comparingLong(Category2Entity::getCategory2Seqno)) // category2Seqno 기준 오름차순 정렬
					.collect(Collectors.toList()); // 다시 리스트로 수집

		model.addAttribute("mist", mist);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
		model.addAttribute("product", product);
	}	


	// //카테고리별 상품 보기 (사이드바 카테고리 목록 보기 와 코드 동일)
	// @GetMapping("/shop/list")
	// public void getList(Model model) throws Exception {
	// 	List<Category1Entity> list = productService.category1List();
	// 	List<Category2Entity> list2 = productService.category2List();
	// 	List<Category3Entity> list3 = productService.category3List();

	// 	// Category2 리스트를 category2Seqno 기준으로 오름차순 정렬
    // 	list2 = list2.stream()
	// 				 .sorted(Comparator.comparingLong(Category2Entity::getCategory2Seqno)) // category2Seqno 기준 오름차순 정렬
	// 			   	 .collect(Collectors.toList()); // 다시 리스트로 수집

	// 	model.addAttribute("list", list);
	// 	model.addAttribute("list2", list2);
	// 	model.addAttribute("list3", list3);
	// }
	
	//상품 목록 보기
	@GetMapping("/shop/list")
	public void getList(Model model,@RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword,
			@RequestParam(name="cate") Long CateSeqno,
			@RequestParam(name = "category1Seqno",defaultValue = "", required = false) Long category1Seqno,
			@RequestParam(name = "category2Seqno",defaultValue = "", required = false) Long category2Seqno,
			@RequestParam(name = "category3Seqno",defaultValue = "", required = false) Long category3Seqno) throws Exception {
		
		int postNum = 10; 
		int pageListCount = 10; 
		
		PageUtil page = new PageUtil();
		Page<ProductEntity> list = productService.list(pageNum, postNum, keyword,category3Seqno);
		log.info(list);
		
		int totalCount = (int)list.getTotalElements();

		List<Category1Entity> mist = productService.category1List();
		List<Category2Entity> list2 = productService.category2List();
		List<Category3Entity> list3 = productService.category3List();

		model.addAttribute("list", list);
		model.addAttribute("listIsEmpty", list.hasContent() ? "N" : "Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("category1Seqno", category1Seqno);  
		model.addAttribute("category2Seqno", category2Seqno); 
		model.addAttribute("category3Seqno", category3Seqno); 
		model.addAttribute("pageList", page.getPageList(pageNum, postNum, pageListCount,totalCount,keyword,CateSeqno));

		// Category2 리스트를 category2Seqno 기준으로 오름차순 정렬
    	list2 = list2.stream()
					.sorted(Comparator.comparingLong(Category2Entity::getCategory2Seqno)) // category2Seqno 기준 오름차순 정렬
					.collect(Collectors.toList()); // 다시 리스트로 수집

		model.addAttribute("mist", mist);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
	}

	// 카테고리별 상품 리스트
	@GetMapping("/shop/categoryProduct")
	public void getCategoryProduct(
		Model model,
		@RequestParam(name="cate") Long CateSeqno,
		@RequestParam(name="page") int pageNum,
		@RequestParam(name="keyword",defaultValue="",required=false) String keyword
		) throws Exception {

		int postNum = 12;
		int pageListCount = 10;

		PageUtil page = new PageUtil();	
		//상품 리스트 호출 서비스
		Page<ProductEntity> list = productService.list(pageNum, postNum, keyword, CateSeqno);

		int totalCount = (int)list.getTotalElements();
		
		List<Category1Entity> mist = productService.category1List();
		List<Category2Entity> list2 = productService.category2List();
		List<Category3Entity> list3 = productService.category3List();

		model.addAttribute("list", list);
		
		// Category2 리스트를 category2Seqno 기준으로 오름차순 정렬
    	list2 = list2.stream()
					.sorted(Comparator.comparingLong(Category2Entity::getCategory2Seqno)) // category2Seqno 기준 오름차순 정렬
					.collect(Collectors.toList()); // 다시 리스트로 수집

		model.addAttribute("mist", mist);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList", page.getPageList(pageNum, postNum, pageListCount,totalCount,keyword,CateSeqno));
	}

	//상품 조회 테스트
	@GetMapping("/shop/shopmainTest")
	public String shopMain() {
		return "shop/shopmainTest"; // 템플릿 파일의 경로가 맞는지 확인
	}

	//가장 많이 팔린 상품 10개 조회
	@ResponseBody
	@GetMapping("/shop/topSelling")
	public List<TopSellingProductDTO> getTopSellingProducts() throws Exception {
		return productService.getTop10BestSellingProducts();
	}
	
	//로그인한 사용자의 연령대별 가장 많이 팔린 상품 10개 조회
	@ResponseBody
	@GetMapping("/shop/topProductsByAgeForLoggedUser")
	public Map<String, List<TopProduct>> getTopProductsForLoggedUser(HttpSession session) throws Exception {
		String email = (String) session.getAttribute("email"); 
		if (email != null) {
			return productService.getTopProductsByAgeForUser(email); 
		}
		throw new IllegalStateException("사용자가 로그인되지 않았습니다.");
	}

  //상품 상세 보기
	@GetMapping("/shop/view")
	public void getView(
		Model model,
		@RequestParam(name="productSeqno") Long productSeqno, 
		@RequestParam(name="page") int pageNum,
		@RequestParam(name="keyword",defaultValue="",required=false) String keyword,
		HttpSession session) throws Exception {
		
		List<Category1Entity> mist = productService.category1List();
		List<Category2Entity> list2 = productService.category2List();
		List<Category3Entity> list3 = productService.category3List();

		//String sessionEmail = (String)session.getAttribute("email");
        model.addAttribute("view", productService.view(productSeqno));
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pre_seqno", productService.pre_seqno(productSeqno,keyword));		
		model.addAttribute("next_seqno", productService.next_seqno(productSeqno,keyword));
		// model.addAttribute("fileListView", productService.fileListView(productSeqno));	

		// Category2 리스트를 category2Seqno 기준으로 오름차순 정렬
    	list2 = list2.stream()
					.sorted(Comparator.comparingLong(Category2Entity::getCategory2Seqno)) // category2Seqno 기준 오름차순 정렬
					.collect(Collectors.toList()); // 다시 리스트로 수집

		model.addAttribute("mist", mist);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
    }

	// 장바구니 보기
    @GetMapping("/mypage/shoppingCart")
    public List<ShoppingCartEntity> getCartItems(
		Model model,
		HttpSession session
	) throws Exception {

		List<Category1Entity> mist = productService.category1List();
		List<Category2Entity> list2 = productService.category2List();
		List<Category3Entity> list3 = productService.category3List();
		List<ProductEntity> product = productService.productList();
		
        String email = (String)session.getAttribute("email");

		model.addAttribute("list", shoppingCartService.getCartItems(email));
		// Category2 리스트를 category2Seqno 기준으로 오름차순 정렬
		list2 = list2.stream()
					.sorted(Comparator.comparingLong(Category2Entity::getCategory2Seqno)) // category2Seqno 기준 오름차순 정렬
					.collect(Collectors.toList()); // 다시 리스트로 수집

		model.addAttribute("mist", mist);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
		model.addAttribute("product", product);
		
		return null;
    }

    // 장바구니에 상품 추가
	@ResponseBody
    @PostMapping("/mypage/shoppingCart")
    public String addToCart(
		@RequestBody OrderProductDTO orderProduct,
		HttpSession session,
		@RequestParam(name="relatedProductList",required=false)
		Map<Long, Integer> relatedProducts,
		@RequestParam(name="productOptionList",required=false)
		List<Long> productOptions,
		@RequestParam(name="kind")
		String kind
	) throws Exception {

		String email = (String)session.getAttribute("email");
		MemberDTO member = memberService.memberInfo(email);
		Long orderProductSeqno = orderProduct.getOrderProductSeqno();
		int newQuantity = orderProduct.getAmount();
		log.info(kind);
		if (kind.equals("I")) {
			// 장바구니에 상품 추가
			log.info("장바구니 추가 시작");
			orderProduct.setReviewYn("N");
			shoppingCartService.addToCart(orderProduct, member, relatedProducts, productOptions);
		} else if (kind.equals("U")) {
			// 장바구니 상품 갯수 수정
			log.info("장바구니 수정 시작");
			shoppingCartService.updateCartItemQuantity(email, orderProductSeqno, newQuantity);
		}
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
	@GetMapping("/shop/pay")
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
    @PostMapping("/shop/pay")
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
			@RequestParam(name="cate") Long CateSeqno,
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
			@RequestParam(name="cate") Long CateSeqno,
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
    


