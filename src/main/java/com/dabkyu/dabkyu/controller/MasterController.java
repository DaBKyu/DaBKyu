package com.dabkyu.dabkyu.controller;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Locale.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

import com.dabkyu.dabkyu.dto.Category1DTO;
import com.dabkyu.dabkyu.dto.Category2DTO;
import com.dabkyu.dabkyu.dto.Category3DTO;
import com.dabkyu.dabkyu.dto.CouponCategoryDTO;
import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.dto.CouponTargetDTO;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.OrderInfoDTO;
import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.dto.ProductInfoFileDTO;
import com.dabkyu.dabkyu.dto.ProductOptionDTO;
import com.dabkyu.dabkyu.dto.QuestionCommentDTO;
import com.dabkyu.dabkyu.dto.QuestionDTO;
import com.dabkyu.dabkyu.dto.QuestionFileDTO;
import com.dabkyu.dabkyu.dto.RelatedProductDTO;
import com.dabkyu.dabkyu.dto.ReviewDTO;
import com.dabkyu.dabkyu.dto.ReviewFileDTO;
import com.dabkyu.dabkyu.entity.AddedRelatedProductEntity;
import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.OrderProductOptionEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.ReportEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.repository.Category1Repository;
import com.dabkyu.dabkyu.entity.repository.Category2Repository;
import com.dabkyu.dabkyu.entity.repository.Category3Repository;
import com.dabkyu.dabkyu.entity.repository.CouponRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.ProductFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.service.MasterService;
import com.dabkyu.dabkyu.service.MemberService;
import com.dabkyu.dabkyu.service.QuestionService;
import com.dabkyu.dabkyu.service.ReviewService;
import com.dabkyu.dabkyu.util.PageUtil;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import oracle.jdbc.proxy.annotation.Post;

@Controller
@Log4j2
@AllArgsConstructor
public class MasterController{

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductFileRepository productFileRepository;
    private final MasterService masterService;
    private final QuestionService questionService;
    private final ReviewService reviewService;
    private final CouponRepository couponRepository;
    private final Category1Repository category1Repository;
    private final Category2Repository category2Repository;
    private final Category3Repository category3Repository;
    
    //메인페이지
    @GetMapping("/master") 
    public void getMaster() {}

    //고객정보페이지 //등급으로 필터링? 다시 한 번 확인 필요 
    @GetMapping("/master/client")
    public void getClient(Model model, 
                @RequestParam("page") int pageNum,
                @RequestParam(name="keyword",defaultValue="",required=false) String keyword) 
                throws Exception {
       
        int postNum = 15; //한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 10; //화면 하단에 보여지는 페이지리스트의 페이지 갯수	
		
		PageUtil page = new PageUtil();
		Page<MemberEntity> clientList = masterService.memberList(pageNum, postNum, keyword);
		int totalCount = (int)clientList.getTotalElements();

		model.addAttribute("list", clientList);
		model.addAttribute("listIsEmpty", clientList.hasContent()?"N":"Y");
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
    public void getModifyClient(@PathVariable String email, Model model) throws Exception{

        model.addAttribute("member", masterService.getMemberByEmail(email));
    }

    //고객정보 수정 
    @PostMapping("/master/modifyViewClient/{email}")
    public String postModifyClient(@ModelAttribute MemberDTO memberDTO) throws Exception {

        masterService.memberModify(memberDTO);

        return "redirect:/master/client"; 
    }





    //이메일 발송
    // @PostMapping("/send")
    // public String sendEmail(@RequestBody EmailRequest emailRequest) {
    //     emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
    //     return "Email sent successfully!";
    // } 

    
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

        model.addAttribute("list", productList);
		    model.addAttribute("listIsEmpty", productList.hasContent()?"N":"Y");
		    model.addAttribute("totalElement", totalCount);
		    model.addAttribute("postNum", postNum);
		    model.addAttribute("page", pageNum);
		    model.addAttribute("productName", productName);
        model.addAttribute("category1Seqno", category1Seqno);
        model.addAttribute("category2Seqno", category2Seqno);
        model.addAttribute("category3Seqno", category3Seqno);
		    model.addAttribute("pageList", page.getPageProduct(pageNum, postNum, pageListCount,totalCount,productName));
    }
    

    //상품 상세보기 
    @GetMapping("/master/getProductDetail/{productSeqno}")
    public void getProductDetail(@PathVariable Long productSeqno, Model model) throws Exception {
     
        ProductEntity productEntity = masterService.getProductBySeqno(productSeqno);
        List<ProductFileDTO> productFiles = masterService.getProductFiles(productSeqno); //file
        List<ProductInfoFileDTO> productInfoFiles = masterService.getProductInfoFiles(productSeqno); //infofile
        List<ProductOptionDTO> productOptions = masterService.getProductOptions(productSeqno); //상품옵션
        List<RelatedProductDTO> relatedProducts = masterService.getRelatedProducts(productSeqno); //추가상품
        Category3Entity category3 = masterService.getCategory3ByProduct(productSeqno); // 카테고리

        model.addAttribute("product", new ProductDTO(productEntity));
        model.addAttribute("productFiles", productFiles);
        model.addAttribute("productInfoFiles", productInfoFiles);
        model.addAttribute("productOptions", productOptions);
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("category3", category3);
    }

    //상품 등록 화면보기 
    @GetMapping("/master/postProduct") 
    public void getPostProduct() {}


    //상품 수정 화면보기 >> 수정 눌렀을 때 
    @GetMapping("/master/modifyProduct")
    public void getmMdifyProduct(@RequestParam("productSeqno") Long productSeqno,
                @RequestParam("page") int pageNum,
                @RequestParam(name="productName",defaultValue="",required=false) String productName,
                Model model) throws Exception{
        model.addAttribute("page", pageNum);
        model.addAttribute("productName", productName);            
        model.addAttribute("productview", masterService.getProductBySeqno(productSeqno));
        model.addAttribute("productfileview", masterService.getProductFiles(productSeqno));
        model.addAttribute("productinfofileview", masterService.getProductInfoFiles(productSeqno));
        model.addAttribute("optionview", masterService.getProductOptions(productSeqno));
        model.addAttribute("relatedproductview", masterService.getRelatedProducts(productSeqno));
        model.addAttribute("categoryview", masterService.getCategory3ByProduct(productSeqno));
    }

    //상품 등록 및 수정  
    @ResponseBody
    @PostMapping("/master/postProduct")
    public String postProduct(ProductDTO productDTO, 
                  @RequestParam("kind") String kind,
                  @RequestParam(name="productImage",required=false) List<MultipartFile> productImages,
                  @RequestParam(name="detailImage", required=false) List<MultipartFile> detailImages,
                  @RequestParam(name="deleteProductImages", required=false) Long[] deleteProductImages,
                  @RequestParam(name="deleteDetailProductImage",required=false) Long[] deleteDetailProductImages,
                  @RequestParam(name="category1Seqno") Long category1Seqno,
                  @RequestParam(name="category2Seqno") Long category2Seqno,
                  @RequestParam(name="category3Seqno") Long category3Seqno,
                  @RequestParam(name="productOptions", required=false) List<ProductOptionDTO> productOptions,
                  @RequestParam(name="relatedProducts", required=false) List<RelatedProductDTO> relatedProducts)
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
        
        ProductEntity productEntity = productDTO.dtoToEntity(productDTO);

        //카테고리 연결
        Category1Entity category1 = category1Repository.findById(category1Seqno).get();
        Category2Entity category2 = category2Repository.findById(category2Seqno).get();
        Category3Entity category3 = category3Repository.findById(category3Seqno).get();

        productDTO.setCategory3Seqno(category3);

        if(kind.equals("I")){ //상품 등록하기
            
            Long seqno = masterService.productPost(productDTO);
            productDTO.setProductSeqno(seqno);

            //상품옵션
            if(productOptions != null) {
                for (ProductOptionDTO option : productOptions) {
                    option.setProductSeqno(productEntity); 
                    masterService.saveProductOption(option);
                }
            }
            //추가상품
            if (relatedProducts != null) {
                for (RelatedProductDTO related : relatedProducts) {
                    related.setProductSeqno(productEntity); 
                    masterService.saveRelatedProduct(related);
                }
            }
            //상품이미지파일 //productfile
            if(productImages != null){
                for(MultipartFile mpr:productImages){
                    String org_filename = mpr.getOriginalFilename();
                    String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));			
                    String stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;

                    try{
                        File targetFile = new File(productImgPath + stored_filename);				
                        mpr.transferTo(targetFile);

                        ProductFileDTO pImgFile = ProductFileDTO.builder()
                                                .productFileSeqno(seqno)
                                                .orgFilename(org_filename)
                                                .storedFilename(stored_filename)
                                                .isThumb("Y")
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
        }else if (kind.equals("U")) { //상품 수정하기
            
            masterService.productModify(productDTO);

            // 상품 옵션 수정
            if (productOptions != null) {
                for (ProductOptionDTO option : productOptions) {
                    option.setProductSeqno(productEntity); 
                    masterService.saveProductOption(option);
                }
            }
            // 관련 상품 수정
            if (relatedProducts != null) {
                for (RelatedProductDTO related : relatedProducts) {
                    related.setProductSeqno(productEntity);
                    masterService.saveRelatedProduct(related);
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
        }
        return "{\"message\":\"good\"}"; 
    }

    //상품 삭제 (활성 비활성) //??? 다시 확인 필요. 
    @ResponseBody
	@GetMapping("/master/secretProduct") 
    public  List<ProductDTO> postSecretProduct(@RequestParam("kind") String kind,
                            @RequestParam(name="role", required=false) String role) throws Exception {

        List<ProductDTO> productList = masterService.getAllProducts();

        //관리자: 상품 전체 보기 가능, 사용자: Y처리된 상품만 보기 가능
        if("MASTER".equals(role)){ 
            return productList;
        }
        return productList.stream().filter(product -> "Y".equals(product.getSecretYn()))
                                   .collect(Collectors.toList());
    }

    //주문내역 리스트
     
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

        model.addAttribute("orderInfo", orderInfoDTO);
        model.addAttribute("orderDetails", orderDetailEntities);
        model.addAttribute("orderProducts", orderProducts);
        model.addAttribute("addedRelatedProducts", addedRelatedProducts);
        model.addAttribute("orderProductOptions", orderProductOptions);
        model.addAttribute("products", products);
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

    //카테고리 리스트 화면보기
    @GetMapping("/master/categoryList")
    public void getCategoryList(Model model) {
        model.addAttribute("categories1", masterService.getAllCategories1());
        model.addAttribute("categories2", masterService.getAllCategories2());
        model.addAttribute("categories3", masterService.getAllCategories3());
    }

    //카테고리 추가 화면보기
    @GetMapping("/master/createCategory")
    public void getCreateCategory() {}

    //카테고리 수정 화면보기 
    @GetMapping("/master/modifyCategory")
    public void getModifyCategory(Model model) {
        model.addAttribute("categories1", masterService.getAllCategories1());
        model.addAttribute("categories2", masterService.getAllCategories2());
        model.addAttribute("categories3", masterService.getAllCategories3());
    }

    //카테고리 추가 및 수정
    @ResponseBody
    @PostMapping("/master/createCategory")
    public String postCreateCategory(@RequestParam String kind,
                @RequestBody List<Category1DTO> category1DTOs,
                @RequestBody List<Category2DTO> category2DTOs,
                @RequestBody List<Category3DTO> category3DTOs) 
                throws Exception{
        masterService.createCategory(kind, category1DTOs, category2DTOs, category3DTOs);
        return "redirect:/master/categoryList"; 
    }

    //카테고리 삭제
    @Transactional
	@GetMapping("/master/deleteCategory") 
    public String getDeleteCategory(@RequestParam String categoryType, 
                @RequestParam Long categorySeqno) 
                throws Exception{
        try {
            switch (categoryType) {
                case "1": //카테고리1번
                    masterService.deleteCategory1(categorySeqno);
                    break;
                case "2": //카테고리2번
                    masterService.deleteCategory2(categorySeqno);
                    break;
                case "3": //카테고리3번
                    masterService.deleteCategory3(categorySeqno);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }      
        return "redirect:/master/categoryList";  
    }

    //모든 카테고리 불러오기
    @GetMapping("/master/categories")
    public List<Category3DTO> getCategories() throws Exception {
        return masterService.getAllCategories(); 
    }

    //문의 리스트 
    @GetMapping("/master/question")
    public void getQuestion(Model model, @RequestParam("page") int pageNum, 
                @RequestParam(name="queType",defaultValue="",required=false) String queType)
                throws Exception{
        int postNum = 15;
        int pageListCount = 10;

        PageUtil page = new PageUtil();
        Page<Map<String, Object>> questionList = masterService.questionList(pageNum, postNum, queType);                        
        int totalCount = (int) questionList.getTotalElements(); 

        model.addAttribute("list", questionList);
        model.addAttribute("listIsEmpty", questionList.hasContent() ? "N" : "Y");
        model.addAttribute("totalElements", totalCount);
        model.addAttribute("postNum", postNum);
        model.addAttribute("page", pageNum);
        model.addAttribute("keyword", queType);
        model.addAttribute("pageList", page.getPageQuestion(pageNum, postNum, pageListCount, totalCount, queType));
    }

    //문의 상세보기
    @GetMapping("/master/question/{queSeqno}")
    public void getQuestionView(@PathVariable Long queSeqno, Model model) throws Exception {
        QuestionDTO question = questionService.view(queSeqno);
        model.addAttribute("question", question);

        List<QuestionFileDTO> questionFiles = questionService.fileListView(queSeqno);
        model.addAttribute("questionFiles", questionFiles); 
    }

    //문의 답변 등록 및 수정
    @ResponseBody
    @PostMapping("/master/question/reply/{queSeqno}")
    public void postReply(@PathVariable Long queSeqno,
                @RequestParam("kind") String kind, 
                @RequestBody QuestionCommentDTO commentDTO)
                throws Exception{
        
        QuestionEntity questionEntity = masterService.getQuestionSeqno(queSeqno);
        commentDTO.setQueSeqno(questionEntity);

        //등록
        if(kind.equals("I")){
            masterService.replyQuestion(commentDTO, questionEntity);
        }
        //수정
        if(kind.equals("U")){
            masterService.replyQuestionModify(commentDTO);
        } 
	}

    //문의 삭제 
    @Transactional
    @GetMapping("/master/question/delete")
    public String deleteQuestion(@RequestParam("queSeqno") Long queSeqno) throws Exception{

        masterService.deleteQuestion(queSeqno); //문의 삭제

        List<QuestionFileDTO> questionFiles = questionService.fileListView(queSeqno);
        if(questionFiles != null){
            for(QuestionFileDTO questionFile : questionFiles){
                masterService.deleteQuestionFile(questionFile.getQuestionFileSeqno()); //문의 파일 삭제
            }  
        }
        return "redirect:/master/question";
    }

    //문의 답변 삭제
    @Transactional
    @GetMapping("/master/question/replydelete")
    public void deleteReply(@RequestParam("queSeqno") Long queSeqno) throws Exception{
        QuestionEntity questionEntity;
        questionEntity = masterService.getQuestionSeqno(queSeqno);
        masterService.deleteQueComment(questionEntity); 
    }

    //리뷰 리스트 화면 
    @GetMapping("/master/reviewList")
    public void getReviewList(Model model, @RequestParam("page") int pageNum, 
                @RequestParam(name="category",defaultValue="",required=false) Long category) 
                throws Exception{
        int postNum = 15;
        int pageListCount = 10;

        PageUtil page = new PageUtil();
		Page<Map<String,Object>> reviewList = masterService.reviewList(pageNum, postNum, category);
		int totalCount = (int)reviewList.getTotalElements();

		model.addAttribute("list", reviewList);
		model.addAttribute("listIsEmpty", reviewList.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", category);
		model.addAttribute("pageList", page.getPageReview(pageNum, postNum, pageListCount,totalCount,category));
    } 

    //리뷰 상세보기
    @GetMapping("/master/review/{reviewSeqno}")
    public void getReviewView(@PathVariable Long reviewSeqno, Model model) throws Exception{
        ReviewDTO review = reviewService.view(reviewSeqno);
        model.addAttribute("review", review);

        List<ReviewFileDTO> reviewFiles = reviewService.fileListView(reviewSeqno);
        model.addAttribute("reviewFiles", reviewFiles);
    }

    //리뷰 신고 리스트
    @GetMapping("/master/reviewReport")
    public void getReviewReportList(Model model, @RequestParam("page") int pageNum,
                @RequestParam("reportTitle") String reportTitle)
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
		model.addAttribute("pageList", page.getPageRoport(pageNum, postNum, pageListCount,totalCount,reportTitle));
    }

    //리뷰 신고 삭제
    @Transactional
    @GetMapping("/master/reviewReport/delete")
    public String getDeleteRoport(@RequestParam("reportSeqno") Long reportSeqno) throws Exception{
        masterService.deleteReport(reportSeqno);
        return "redirect:/master/reviewReport";
    }

    //리뷰 삭제 
    @Transactional
    @GetMapping("/master/review/delete")
    public String getDeleteReview(@RequestParam("reviewSeqno") Long reviewSeqno) throws Exception{
        
        masterService.deleteReview(reviewSeqno); //리뷰삭제

        List<ReviewFileDTO> reviewFiles = reviewService.fileListView(reviewSeqno);
        if (reviewFiles != null) {
            for (ReviewFileDTO reviewFile : reviewFiles) {
                masterService.deleteReviewFile(reviewFile.getReviewFileSeqno()); //리뷰 파일삭제
            }
        }
        return "redirect:/master/reviewList";  
    }

    //쿠폰 리스트보기 //수정필요
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
    public void getModifyCoupon() {
        //

    }

    //쿠폰 등록, 수정
    //수정필요
    //쿠폰 타입 A(전체상품적용), T(특정상품적용(카테고리)), C(단일상품적용) 
    @ResponseBody
    @PostMapping("/master/createCoupon")
    public String postCreateCoupon(CouponDTO couponDTO, 
                @RequestParam("kind") String kind,
                @RequestParam(name="couponCategory", required=false) List<Long> couponCategories,
                @RequestParam(name="couponTarget", required=false) List<Long> couponTargets) 
                throws Exception{

       
        if(kind.equals("I")){
     
            //쿠폰 적용 범위를 카테고리 검색으로. 검색도 가능(상품이름). 여러개 선택도 가능... 
            //coupon_category , coupon_target

            //권한설정: coupon_role > 맴버등급으로.
            //사용옵션: 추가 할인 가능 >> 체크표시? 중복가능여부 isDupl
            //

            Long couponSeqno = masterService.writeCoupon(couponDTO);

            if("A".equals(couponDTO.getCouponType())){ //모든 상품 적용
                List<ProductEntity> allProducts = masterService.getAllProductsA();
                for (ProductEntity product : allProducts) {
                    CouponTargetDTO couponTargetDTO = new CouponTargetDTO();
                    CouponEntity couponEntity = masterService.getCouponSeqno(couponSeqno);
                    couponTargetDTO.setCouponSeqno(couponEntity);
                    couponTargetDTO.setProductSeqno(product);
                    masterService.saveCouponTarget(couponTargetDTO);
                }
            }else if("T".equals(couponDTO.getCouponType())){ //특정 상품 적용(카테고리)
                //List<Category3DTO> categories = masterService.getAllCategories();
                for (Long categoryId : couponCategories) {
                CouponCategoryDTO couponCategoryDTO = new CouponCategoryDTO();
                CouponEntity couponEntity = masterService.getCouponSeqno(couponSeqno);
                couponCategoryDTO.setCouponSeqno(couponEntity); // 방금 생성한 쿠폰의 ID
                Category3Entity category3Entity = masterService.getCategory3Seqno(categoryId);
                couponCategoryDTO.setCategory3seqno(category3Entity); // 카테고리 ID 설정
                masterService.saveCouponCategory(couponCategoryDTO); // 카테고리 저장 메서드 호출
                }
            }else if("C".equals(couponDTO.getCouponType())){ //단일 상품 적용(상품)
                for (Long productId : couponTargets) {
                    CouponTargetDTO couponTargetDTO = new CouponTargetDTO();
                    CouponEntity couponEntity = masterService.getCouponSeqno(couponSeqno);
                    couponTargetDTO.setCouponSeqno(couponEntity); // 방금 생성한 쿠폰의 ID
                    ProductEntity productEntity = masterService.getProductBySeqno(productId);
                    couponTargetDTO.setProductSeqno(productEntity); // 상품 ID 설정
                    masterService.saveCouponTarget(couponTargetDTO); // 상품 저장 메서드 호출
                }
            }
        }else if(kind.equals("U")){
            //masterService.modifyCoupon(couponDTO);



        }
        return "{\"message\":\"good\"}"; 
    }
    
    //쿠폰-사용자 배포
    //자동: 특정 회원 지정 발행, 웅영자 지정 대상 자동 발행(신규회원, 첫주문 완료 회원, 생일인 회원)
    //수동: 고객 다운로드, 운영자의 쿠폰코드 생성
    //@PostMapping()
    //


    //쿠폰 삭제
    //수정필요. >> 삭제가 아니라 사용만료, 기간만료에 들어가게 됨. 
    //             사용조건이 맞지 않더라도 위치 옮김. (특정 등급 제한 쿠폰일 경우, 기간 만료 전 등급이 변경됐을 때 etc.)
    //isExpire : Y(사용가능), N(사용불가능)
    @GetMapping("/master/deleteCoupon")
    public String getDeleteCoupon(@RequestParam("couponSeqno") Long couponSeqno) throws Exception{
        




        return "redirect:/master/couponList";  
    }

    
     // 결제 취소 및 환불 신청 내역 보기
    @GetMapping("/master/paymentCancelAndRefund")
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

    //관리자가 쿠폰종료일이 지난 쿠폰들을 isExpired를 "Y"로 업데이트해서 만료처리
    @PostMapping("/master/ExpiredUpdate")
    public String updateExpiredCoupons() {
        LocalDateTime referenceDate = LocalDateTime.now();
        
        masterService.setExpiredCouponsToExpired(referenceDate);

        return "{\"message\":\"good\"}";
    }

}


