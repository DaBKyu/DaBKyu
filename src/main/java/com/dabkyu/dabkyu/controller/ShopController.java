package com.dabkyu.dabkyu.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.OrderInfoDTO;
import com.dabkyu.dabkyu.dto.OrderProductDTO;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ShoppingCartEntity;
import com.dabkyu.dabkyu.service.MemberService;
import com.dabkyu.dabkyu.service.ProductService;
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

    //상품 목록 보기
    @GetMapping("/shop/list")
    public void getList(Model model,@RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword) throws Exception {
		
		int postNum = 10; 
		int pageListCount = 10; 
		
		PageUtil page = new PageUtil();
		Page<ProductEntity> list = productService.list(pageNum, postNum, keyword);
		int totalCount = (int)list.getTotalElements();

		model.addAttribute("list", list);
		model.addAttribute("listIsEmpty", list.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList", page.getPageList(pageNum, postNum, pageListCount,totalCount,keyword));
	}
    
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
    }
    
	// 장바구니 보기
    @GetMapping("/shop/cart")
    public List<ShoppingCartEntity> getCartItems(Model model, HttpSession session) throws Exception {
        String email = (String)session.getAttribute("email");
		model.addAttribute("list", shoppingCartService.getCartItems(email));
		return null;
    }

    // 장바구니에 상품 추가
    @PostMapping("/shop/addcart")
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
	
		return null;
    }

	// 장바구니에 등록된 상품 수량 수정
    @PutMapping("/shop/updateCartItemQuantity")
    public String updateCartItemQuantity(
            @RequestParam String email,
            @RequestParam Long orderProductSeqno,
            @RequestParam int newQuantity
	) throws Exception {
		try {
			shoppingCartService.updateCartItemQuantity(email, orderProductSeqno, newQuantity);
			return "장바구니 수량이 업데이트되었습니다.";
		} catch (RuntimeException e) {
			return  e.getMessage();
		}
	}


    // 장바구니에서 특정 상품 삭제
    @PostMapping("/shop/removecart")
    public String removeFromCart(HttpSession session, @RequestParam("orderProductSeqno") Long orderProductSeqno) throws Exception {
		String email = (String)session.getAttribute("email");
        shoppingCartService.removeFromCart(email, orderProductSeqno);
        return null;
    }

    // 장바구니에서 모든 상품 삭제
    @PostMapping("/shop/clearcart")
    public String clearCart(HttpSession session) throws Exception {
		String email = (String)session.getAttribute("email");
        shoppingCartService.clearCart(email);
        return null;
    }

	// 결제 화면 보기
	@GetMapping("/shop/pay")
	public void getPay(
		HttpSession session,
		@RequestParam("toPayOrderProductList")
		List<Long> toPayOrderProductList,
		@RequestBody OrderInfoDTO orderInfo
	) {
		String email = (String)session.getAttribute("email");
	}


	// 결제 
    @PostMapping("/shop/pay")
    public String pay(
			HttpSession session,
            @RequestParam("toPayOrderProductList")
			 List<Long> toPayOrderProductList,
            @RequestBody OrderInfoDTO orderInfo) {
		String email = (String)session.getAttribute("email");
		try {
			shoppingCartService.pay(email, toPayOrderProductList, orderInfo);
			return "결제가 완료되었습니다.";
		} catch (RuntimeException e) {
			return "결제 실패: " + e.getMessage();
		}
    }

	// 결제 취소
	@PostMapping("/shop/cancelToPay")
	public String cancelToPay(
			HttpSession session,
			@RequestParam Long orderSeqno) {
		String email = (String)session.getAttribute("email");
		try {
			shoppingCartService.cancelToPay(email, orderSeqno);
			return "결제가 취소되었습니다.";
		} catch (RuntimeException e) {
			return "결제 취소 실패: " + e.getMessage();
		}
	}

	// 배송

    // 교환,환불 

    // 상품 문의 
	
    // 상품 리뷰


}
    


