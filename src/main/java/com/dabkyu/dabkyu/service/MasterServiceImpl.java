package com.dabkyu.dabkyu.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.Category1DTO;
import com.dabkyu.dabkyu.dto.Category2DTO;
import com.dabkyu.dabkyu.dto.Category3DTO;
import com.dabkyu.dabkyu.dto.CategorySalesDTO;
import com.dabkyu.dabkyu.dto.CouponCategoryDTO;
import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.dto.CouponTargetDTO;
import com.dabkyu.dabkyu.dto.DailySalesDTO;
import com.dabkyu.dabkyu.dto.DailyVisitorDTO;
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
import com.dabkyu.dabkyu.dto.ReviewDTO;
import com.dabkyu.dabkyu.dto.ReviewFileDTO;
import com.dabkyu.dabkyu.dto.SalesByAgeGroupDTO;
import com.dabkyu.dabkyu.dto.SalesByMemberGradeDTO;
import com.dabkyu.dabkyu.dto.SignupAgeStatDTO;
import com.dabkyu.dabkyu.dto.SignupDateStatDTO;
import com.dabkyu.dabkyu.dto.SignupGenderStatDTO;
import com.dabkyu.dabkyu.entity.AddedRelatedProductEntity;
import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.CouponCategoryEntity;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.CouponTargetEntity;
import com.dabkyu.dabkyu.entity.MemberCouponEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.MemberNotificationEntity;
import com.dabkyu.dabkyu.entity.NotificationEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.OrderProductOptionEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductOptionEntity;
import com.dabkyu.dabkyu.entity.QuestionCommentEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.QuestionFileEntity;
import com.dabkyu.dabkyu.entity.ReportEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;
import com.dabkyu.dabkyu.entity.repository.AddedRelatedProductRepository;
import com.dabkyu.dabkyu.entity.repository.Category1Repository;
import com.dabkyu.dabkyu.entity.repository.Category2Repository;
import com.dabkyu.dabkyu.entity.repository.Category3Repository;
import com.dabkyu.dabkyu.entity.repository.CouponCategoryRepository;
import com.dabkyu.dabkyu.entity.repository.CouponRepository;
import com.dabkyu.dabkyu.entity.repository.MasterRepository;
import com.dabkyu.dabkyu.entity.repository.MemberCouponRepository;
import com.dabkyu.dabkyu.entity.repository.MemberLogRepository;
import com.dabkyu.dabkyu.entity.repository.MemberCouponRepository;
import com.dabkyu.dabkyu.entity.repository.MemberNotificationRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.NotificationRepository;
import com.dabkyu.dabkyu.entity.repository.OrderDetailRepository;
import com.dabkyu.dabkyu.entity.repository.OrderInfoRepository;
import com.dabkyu.dabkyu.entity.repository.OrderProductOptionRepository;
import com.dabkyu.dabkyu.entity.repository.ProductFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductInfoFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductOptionRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.entity.repository.QuestionCommentRepository;
import com.dabkyu.dabkyu.entity.repository.QuestionFileRepository;
import com.dabkyu.dabkyu.entity.repository.QuestionRepository;
import com.dabkyu.dabkyu.entity.repository.RelatedProductRepository;
import com.dabkyu.dabkyu.entity.repository.ReportRepository;
import com.dabkyu.dabkyu.entity.repository.ReviewFileRepository;
import com.dabkyu.dabkyu.entity.repository.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final CouponCategoryRepository couponCategoryRepository;
    private final CouponTargetRepository couponTargetRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final ProductRepository productRepository;
    private final ProductFileRepository productFileRepository;
    private final ProductInfoFileRepository productInfoFileRepository;
    private final ProductOptionRepository productOptionRepository;
    private final RelatedProductRepository relatedProductRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewFileRepository reviewFileRepository;
    private final ReportRepository reportRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final NotificationRepository notificationRepository;
    private final MemberNotificationRepository memberNotificationRepository;
    private final AddedRelatedProductRepository addedRelatedProductRepository;
    private final OrderProductOptionRepository orderProductOptionRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final ReviewService reviewService;
    private final QuestionCommentRepository questionCommentRepository;
    private final QuestionFileRepository questionFileRepository;
    private final Category1Repository category1Repository;
    private final Category2Repository category2Repository;
    private final Category3Repository category3Repository;
    private final MemberCouponRepository memberCouponRepository;
    private final MemberLogRepository memberLogRepository;

    //맴버 리스트 보기
    @Override
    public Page<MemberEntity> memberList(int pageNum, int postNum, String keyword, String memberGrade) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Sort.Direction.DESC, "username"));
        if (memberGrade != null && !memberGrade.isEmpty()) {
            return memberRepository.findByMemberGrade(memberGrade.toUpperCase(), pageRequest); // 등급 필터링 추가
        } else {
            return memberRepository.findByEmailContainingOrUsernameContaining(keyword, keyword,pageRequest);
        }
    }

    //맴버 이메일로 검색
    @Override
    public MemberDTO getMemberByEmail(String email){
        return memberRepository.findById(email).map((member)->new MemberDTO(member)).get();
    }
   
    //맴버 등급 수정
    @Override
    public void memberModify(MemberDTO memberDTO) throws Exception{
        MemberEntity memberEntity = memberRepository.findById(memberDTO.getEmail()).get();
        memberEntity.setEmail(memberDTO.getEmail());
        memberEntity.setUsername(memberDTO.getUsername());
        memberEntity.setTelno(memberDTO.getTelno());
        memberEntity.setGender(memberDTO.getGender());
        memberEntity.setBirthDate(memberDTO.getBirthDate());
        memberEntity.setMemberGrade(memberDTO.getMemberGrade());
        memberEntity.setPay(memberDTO.getPay());
        memberEntity.setPoint(memberDTO.getPoint());
        memberEntity.setRole(memberDTO.getRole());
        memberEntity.setEmailRecept(memberDTO.getEmailRecept());
        memberRepository.save(memberEntity);  
    }

    //맴버 삭제 
    @Override
    public void clientDelete(String email) throws Exception{
        memberRepository.deleteById(email);
    }

    //상품 리스트 보기
    @Override
    public Page<ProductEntity> productList(Long category1Seqno, Long category2Seqno, Long category3Seqno, String productName, PageRequest pageRequest) {
        if (category1Seqno == null && category2Seqno == null && category3Seqno == null && productName == null) {
            return productRepository.findAll(pageRequest); 
        }else{ //검색 (카테고리 OR 상품명)
            return  productRepository.findByAllCategories(category1Seqno, category2Seqno, category3Seqno, productName, pageRequest);
        }
    }
    

    //상품 상세보기
    //productSeqno로 상품 정보 가져오기
    @Override
    public ProductEntity getProductBySeqno(Long productSeqno) throws Exception {
        return productRepository.findById(productSeqno).get();
    }

    //상품 옵션 정보 가져오기
    @Override
    public List<ProductOptionDTO> getProductOptions(Long productSeqno) throws Exception {
        List<ProductOptionEntity> productOptions = productOptionRepository.findByProductSeqno_ProductSeqno(productSeqno);
        return productOptions.stream()
                .map(ProductOptionDTO::new)
                .collect(Collectors.toList());
    }

    //추가 상품 정보 가져오기
    @Override
    public List<RelatedProductDTO> getRelatedProducts(Long productSeqno) throws Exception {
        return relatedProductRepository.findByProductSeqno_ProductSeqno(productSeqno)
                .stream()
                .map(RelatedProductDTO::new)
                .collect(Collectors.toList());
    }

    //상품 카테고리 정보 가져오기
    @Override
    public Category3Entity getCategory3ByProduct(Long productSeqno) throws Exception {
        ProductEntity productEntity = productRepository.findById(productSeqno).get();
        return productEntity.getCategory3Seqno();
    }

    @Override
    public Category2Entity getCategory2ByProduct(Long productSeqno) throws Exception {
        ProductEntity productEntity = productRepository.findById(productSeqno).get();
        return productEntity.getCategory3Seqno().getCategory2Seqno();
    }

    @Override
    public Category1Entity getCategory1ByProduct(Long productSeqno) throws Exception {
        ProductEntity productEntity = productRepository.findById(productSeqno).get();
        return productEntity.getCategory3Seqno().getCategory2Seqno().getCategory1Seqno();
    }

    //상품 파일 정보 가져오기
    @Override
    public List<ProductFileDTO> getProductFiles(Long productSeqno) throws Exception {
        return productFileRepository.findByProductSeqno_ProductSeqno(productSeqno)
                .stream()
                .map(ProductFileDTO::new)
                .collect(Collectors.toList());
    }

    //상품설명 이미지 파일 정보 가져오기
    @Override
    public List<ProductInfoFileDTO> getProductInfoFiles(Long productSeqno) throws Exception {
        return productInfoFileRepository.findByProductSeqno_ProductSeqno(productSeqno)
                .stream()
                .map(ProductInfoFileDTO::new)
                .collect(Collectors.toList());
    }

    //모든 상품 가져오기
    @Override
    public List<ProductEntity> getAllProducts() throws Exception{
        return productRepository.findAll(); 
    }

    //상품등록
    @Override
    public Long productPost(ProductDTO productDTO) throws Exception{
        ProductEntity productEntity = productDTO.dtoToEntity(productDTO);
        productEntity = productRepository.save(productEntity); 
        return productEntity.getProductSeqno();
    }

    //상품수정
    @Override
    public void productModify(ProductDTO productDTO) throws Exception{
        ProductEntity productEntity = productRepository.findById(productDTO.getProductSeqno()).get();
        productEntity.setCategory3Seqno(productDTO.getCategory3Seqno());
        productEntity.setProductName(productDTO.getProductName());
        productEntity.setProductInfo(productDTO.getProductInfo());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setStockAmount(productDTO.getStockAmount());
        productEntity.setDeliveryisFree(productDTO.getDeliveryisFree());
        productEntity.setSecretYn(productDTO.getSecretYn());
        productRepository.save(productEntity);
    }

    //상품 카테고리 저장
    @Override
    public void setProductCategory(Long category3Seqno, ProductDTO productDTO) throws Exception{
        Category3Entity category3 = category3Repository.findById(category3Seqno).get();
        productDTO.setCategory3Seqno(category3);
    }

    //상품 옵션 저장
    @Override
    public void saveProductOption(ProductOptionDTO productOptionDTO) throws Exception {
        productOptionRepository.save(productOptionDTO.dtoToEntity(productOptionDTO));
    }

    //추가상품 저장
    @Override
    public void saveRelatedProduct(RelatedProductDTO relatedProductDTO) throws Exception {
        relatedProductRepository.save(relatedProductDTO.dtoToEntity(relatedProductDTO));
    }

    //상품 이미지 파일 등록
    @Override
    public void productImgFile(ProductFileDTO productFileDTO) throws Exception{
        productFileRepository.save(productFileDTO.dtoToEntity(productFileDTO));
    }

    //상품 상세보기 이미지 파일 등록
    @Override
    public void productDetailImgFile(ProductInfoFileDTO productInfoFileDTO) throws Exception {
        productInfoFileRepository.save(productInfoFileDTO.dtoToEntity(productInfoFileDTO));
    }

    //옵션 삭제
    @Override
    public void deleteProductOption(Long optionSeqno){
        productOptionRepository.deleteById(optionSeqno);
    }

    //추가상품 삭제
    @Override
    public void deleteRelatedProduct(Long relatedProductSeqno){
        relatedProductRepository.deleteById(relatedProductSeqno);
    }

    //상품 이미지 삭제
    @Override
    public void deleteProductFile(Long productFileSeqno) throws Exception{
        productFileRepository.deleteById(productFileSeqno);
    }

    //상품 상세보기 이미지 파일 삭제
    @Override
    public void deleteProductInfoFile(Long productInfoFileSeqno) throws Exception {
        productInfoFileRepository.deleteById(productInfoFileSeqno);
    }

    //상품 비활성화
    @Override
    public void secretYNProduct(Long productSeqno) throws Exception{
        ProductEntity productEntity = productRepository.findById(productSeqno).get();
        productEntity.setSecretYn("Y"); //비활성화
        productRepository.save(productEntity);
    }
    
    //주문 리스트
    @Override
    public Page<Map<String, Object>> orderList(int pageNum, int postNum, String productname, Long category) throws Exception{
        
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "orderDate"));
        Page<OrderDetailEntity> orderDetailPage;

        //검색기능 
        if (category != null || productname != null) { //카테고리 OR 상품명으로 검색
            orderDetailPage = orderDetailRepository.findByCategoryAndProductNameContaining(category, productname, pageRequest);
        } else{ //전체 불러오기
            orderDetailPage = orderDetailRepository.findAll(pageRequest);
        }
         
        return orderDetailPage.map(orderDetail -> {
        Map<String, Object> result = new HashMap<>();

        // 주문 정보 
        OrderInfoEntity orderInfoEntity = orderDetail.getOrderSeqno(); 
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO(orderInfoEntity);
        result.put("orderInfo", orderInfoDTO); 

        // 주문 상품 정보 
        OrderProductEntity orderProductEntity = orderDetail.getOrderProductSeqno(); 
        ProductEntity productEntity = orderProductEntity.getProductSeqno();
        result.put("productName", productEntity.getProductName()); 
        result.put("productPrice", productEntity.getPrice());

        // 카테고리 정보 
        String category3Name = orderProductEntity.getProductSeqno().getCategory3Seqno().getCategory3Name();
        result.put("category3Name", category3Name);

        // 취소 여부, 환불 여부
        result.put("cancelYn", orderDetail.getCancelYn());
        result.put("refundYn", orderDetail.getRefundYn());

        // 추가 상품
        List<AddedRelatedProductEntity> relatedProducts = addedRelatedProductRepository.findByOrderProductSeqno(orderProductEntity.getOrderProductSeqno());
        if(!relatedProducts.isEmpty()){
            result.put("relatedProducts", relatedProducts);
        }

        // 옵션
        List<OrderProductOptionEntity> productOptions = orderProductOptionRepository.findByOrderProductSeqno(orderProductEntity.getOrderProductSeqno());
        if (productOptions != null && !productOptions.isEmpty()) {
            result.put("productOptions", productOptions); 
        }

        return result; // 최종 결과 반환
        });
    }
        

    //주문 상태 수정
    @Override
    public void modifyOrderStatus(Long orderSeqno, String orderStatus) throws Exception{
        OrderInfoEntity orderInfoEntity = orderInfoRepository.findById(orderSeqno).get();
        orderInfoEntity.setOrderStatus(orderStatus);
        orderInfoRepository.save(orderInfoEntity);
    }

    //OrderSeqno찾기
    @Override
    public OrderInfoEntity getOrderInfo(Long orderSeqno) throws Exception{
        return orderInfoRepository.findById(orderSeqno).get();
    }
    
    //OrderDetail찾기
    @Override
    public List<OrderDetailEntity> getOrderDetails(Long orderSeqno) throws Exception{
        OrderInfoEntity orderInfoEntity = orderInfoRepository.findById(orderSeqno).get();
        return orderDetailRepository.findByOrderSeqno_OrderSeqno(orderInfoEntity);
    }

    //OrderProduct찾기
    @Override
    public List<OrderProductEntity> getOrderProducts(Long orderSeqno) throws Exception{
        OrderInfoEntity orderInfoEntity = orderInfoRepository.findById(orderSeqno).get();
        return orderDetailRepository.findByOrderSeqno_OrderSeqno(orderInfoEntity).stream()
            .map(OrderDetailEntity::getOrderProductSeqno) 
            .collect(Collectors.toList());
    }

    //AddedRelatedProduct찾기
    @Override 
    public List<AddedRelatedProductEntity> getAddedRelatedProducts(List<OrderProductEntity> orderProducts) throws Exception{
        List<AddedRelatedProductEntity> addedRelatedProducts = new ArrayList<>();
        for (OrderProductEntity orderProduct : orderProducts) {
            addedRelatedProducts.addAll(addedRelatedProductRepository.findByOrderProductSeqno(orderProduct.getOrderProductSeqno()));
        }
        return addedRelatedProducts;
    }

    //OrderProductOption찾기
    @Override
    public List<OrderProductOptionEntity> getOrderProductOptions(List<OrderProductEntity> orderProducts) throws Exception{
        List<OrderProductOptionEntity> orderProductOptions = new ArrayList<>();
        for (OrderProductEntity orderProduct : orderProducts) {
            orderProductOptions.addAll(orderProductOptionRepository.findByOrderProductSeqno(orderProduct.getOrderProductSeqno()));
        }
        return orderProductOptions;
    }

    //product찾기
    @Override
    public List<ProductEntity> getProducts(List<OrderProductEntity> orderProducts) throws Exception{
        List<ProductEntity> products = new ArrayList<>();
        for (OrderProductEntity orderProduct : orderProducts) {
            products.add(orderProduct.getProductSeqno());
        }
        return products;
    }

    //문의 리스트
    @Override
    public Page<Map<String, Object>> questionList(int pageNum, int postNum, String queType) throws Exception{
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "queDate"));
        Page<QuestionEntity> questionEntities;

        if (!queType.isEmpty()) {
            questionEntities = questionRepository.findByQueType(queType, pageRequest); //문의 타입 검색
        } else {
            questionEntities = questionRepository.findAll(pageRequest);
        }

        return questionEntities.map(questionEntity -> {
            Map<String, Object> result = new HashMap<>();
            QuestionDTO questionDTO = new QuestionDTO(questionEntity);

            // 문의 정보
            result.put("question", questionDTO);
            
            // 첨부 파일 
            List<QuestionFileDTO> questionFiles = new ArrayList<>();
            try {
                questionFiles = questionService.fileListView(questionEntity.getQueSeqno());
            } catch (Exception e) {
                questionFiles = new ArrayList<>(); //파일 없을 때
            }
            if (!questionFiles.isEmpty()) {
                result.put("questionFiles", questionFiles);
            } else {
                result.put("questionFiles", new ArrayList<>()); 
            }
            
            return result;
        });
    }

    //문의 queSeqno가져오기
    @Override
    public QuestionEntity getQuestionSeqno(Long queSeqno) throws Exception {
        return questionRepository.findById(queSeqno).get();
    }

    //문의 답변
    @Override
    public void replyQuestion(QuestionCommentDTO commentDTO, QuestionEntity questionEntity) throws Exception{
        QuestionCommentEntity commentEntity = QuestionCommentEntity.builder()
                                                .queSeqno(questionEntity) 
                                                .email(commentDTO.getEmail())
                                                .comContent(commentDTO.getComContent())
                                                .comDate(LocalDateTime.now()) 
                                                .build();

        questionCommentRepository.save(commentEntity);
    }

    //문의 수정
    @Override
    public void replyQuestionModify(QuestionCommentDTO comment) throws Exception{
        QuestionCommentEntity questionCommentEntity = questionCommentRepository.findByQueSeqno(comment.getQueSeqno());
        questionCommentEntity.setComContent(comment.getComContent());
        questionCommentEntity.setComDate(LocalDateTime.now());
        questionCommentRepository.save(questionCommentEntity);
    }

    //문의 삭제
    @Override
    public void deleteQuestion(Long queSeqno) throws Exception{
        questionRepository.deleteById(queSeqno);
    }

    //문의 첨부파일 삭제
    @Override 
    public void deleteQuestionFile(Long queSeqno) throws Exception{
        QuestionEntity questionEntity = questionRepository.findById(queSeqno).get();
        List<QuestionFileEntity> questionFiles = questionFileRepository.findByQueSeqno(questionEntity);
        for(QuestionFileEntity questionFile : questionFiles){
             /* 
            File file = new File(questionFile.getStoredFilename()); 
            if (file.exists()) {
                file.delete(); // 파일 삭제
            }
            */
            questionFileRepository.delete(questionFile);
        }
    }

    //문의 답변 삭제   
    @Override
    public void deleteQueComment(QuestionEntity queSeqno) throws Exception {
        questionCommentRepository.deleteByQueSeqno(queSeqno);
    }

    //카테고리 리스트
    @Override
    public List<Category1Entity> getAllCategories1() {
        return category1Repository.findAll();
    }
    @Override
    public List<Category2Entity> getAllCategories2() {
        return category2Repository.findAll();
    }
    @Override
    public List<Category3Entity> getAllCategories3() {
        return category3Repository.findAll();
    }

    //카테고리 추가 및 수정
    @Transactional
    @Override
    public void saveCategories(List<Category1DTO> list1, List<Category2DTO> list2, List<Category3DTO> list3) throws Exception{
        // 1. Category1Entity 
        for (Category1DTO category1DTO : list1) {
            Category1Entity  category1Entity = category1Repository.findById(category1DTO.getCategory1Seqno())
                                                .orElse(null);
            
            category1Entity = Category1Entity.builder()
                // .category1Seqno(category1Entity != null ? category1Entity.getCategory1Seqno() : null) //기존 //새로운 입력값
                .category1Name(category1DTO.getCategory1Name())
                .build();

            category1Repository.save(category1Entity);
        }

        // 2. Category2Entity 
        for (Category2DTO category2DTO : list2) {
            Category2Entity category2Entity = category2Repository.findById(category2DTO.getCategory2Seqno())
                                                .orElse(null);
            
            if (category2Entity != null) {
                // 기존에 존재하는 카테고리
                category2Entity.setCategory2Name(category2DTO.getCategory2Name());
            } else {
                // 신규 카테고리
                category2Entity = Category2Entity.builder()
                    .category2Name(category2DTO.getCategory2Name())
                    .category1Seqno(category1Repository.findById(category2DTO.getCategory1Seqno()).get()) // 부모 Category1 유지
                    .build();
            }

            category2Repository.save(category2Entity);
        }

        // 3. Category3Entity 
        for (Category3DTO category3DTO : list3) {
            Category3Entity category3Entity = category3Repository.findById(category3DTO.getCategory3Seqno())
                                                .orElse(null);  

            if (category3Entity != null) {
                // 기존에 존재하는 카테고리
                category3Entity.setCategory3Name(category3DTO.getCategory3Name());
            } else {
                // 신규 카테고리
                category3Entity = Category3Entity.builder()
                    .category3Name(category3DTO.getCategory3Name())
                    .category2Seqno(category2Repository.findById(category3DTO.getCategory2Seqno()).get()) // 부모 Category1 유지
                    .build();
            }

            category3Repository.save(category3Entity);
        }
    }

    //카테고리 1 삭제
    @Override
    public void deleteCategory1(Long category1Seqno) throws Exception {
        Category1Entity category1Entity = category1Repository.findById(category1Seqno).get();

        category1Repository.delete(category1Entity);
    }

    // 카테고리 2 삭제
    @Override
    public void deleteCategory2(Long category2Seqno) throws Exception {
        Category2Entity category2Entity = category2Repository.findById(category2Seqno).get();

        category2Repository.delete(category2Entity);
    }

    // 카테고리 3 삭제
    @Override
    public void deleteCategory3(Long category3Seqno) throws Exception {
        Category3Entity category3Entity = category3Repository.findById(category3Seqno).get();

        Category3Entity temporaryCategory = category3Repository.findByIsTemporary("Y"); 

        if (temporaryCategory == null) { //임시 카테고리 없다면 생성. 
            temporaryCategory = new Category3Entity();
            temporaryCategory.setCategory3Name("tempCategory");
            temporaryCategory.setCategory2Seqno(category3Entity.getCategory2Seqno()); //기존 카테고리2에 연결
            temporaryCategory.setIsTemporary("Y");//임시 카테고리로 설정 Y
            category3Repository.save(temporaryCategory);
        }

        List<ProductEntity> products = productRepository.findByCategory3Seqno_Category3Seqno(category3Seqno);
        for (ProductEntity product : products) {
            product.setCategory3Seqno(temporaryCategory);  //상품 - 임시 카테고리로 설정
            product.setSecretYn("Y");
            productRepository.save(product);
        }

        category3Repository.delete(category3Entity);
    }   

    //쿠폰 리스트 
    @Override
    public Page<Map<String,Object>> couponList(int pageNum, int postNum) throws Exception{
        
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"couponStartDate"));

        Page<CouponEntity> coupons = couponRepository.findAll(pageRequest);
        
        //카테고리, 타겟 집어넣
        Map<CouponEntity, Object> couponDataMap = new HashMap<>();

        for (CouponEntity coupon : coupons) {
            //couponType : C (카테고리로 적용)
            if ("C".equals(coupon.getCouponType())) {
                List<CouponCategoryEntity> categories = couponCategoryRepository.findByCouponSeqno(coupon);
                if (!categories.isEmpty()) {
                    couponDataMap.put(coupon, categories); 
                } else {
                    couponDataMap.put(coupon, new ArrayList<>()); 
                }
            } 
            //couponType : T (단일 상품으로 적용) 
            if ("T".equals(coupon.getCouponType())) {
                List<CouponTargetEntity> targets = couponTargetRepository.findByCouponSeqno(coupon);
                if (!targets.isEmpty()) {
                    couponDataMap.put(coupon, targets); 
                } else {
                    couponDataMap.put(coupon, new ArrayList<>()); 
                }
            }
        }
        //합치기
        return coupons.map(coupon -> {

            Object couponData = couponDataMap.get(coupon);

            Map<String, Object> result = new HashMap<>();
            result.put("coupon", coupon); //기존 쿠폰 정보      
            result.put("couponData", couponData); //쿠폰 카테고리, 타겟 정보

            return result;
        });
    }

    //쿠폰 상세보기
    @Override
    public CouponEntity getCouponBySeqno(Long couponSeqno) throws Exception{
        return couponRepository.findById(couponSeqno).get();
    }

    //쿠폰 등록 (쿠폰 seqno가져오기)
    @Override
    public Long writeCoupon(CouponDTO couponDTO) throws Exception{
        String couponCode = generateCouponCode(); //쿠폰 코드 난수로 생성
        couponDTO.setCouponCode(couponCode); //쿠폰 코드 설정
        CouponEntity couponEntity = couponDTO.dtoToEntity(couponDTO);
        couponRepository.save(couponEntity);
        return couponEntity.getCouponSeqno();
    }

    //쿠폰 수정 
    @Override
    public void modifyCoupon(CouponDTO couponDTO) throws Exception{
        CouponEntity couponEntity = couponDTO.dtoToEntity(couponDTO);
        couponRepository.save(couponEntity);
    }

    //쿠폰 카테고리 저장
    @Override
    public void saveCouponCategory(Long couponSeqno, Long categorySeqno) throws Exception{
        CouponCategoryDTO couponCategoryDTO = new CouponCategoryDTO();
        //CouponEntity couponEntity = getCouponSeqno(couponSeqno);
        CouponEntity couponEntity = couponRepository.findById(couponSeqno).get();
        couponCategoryDTO.setCouponSeqno(couponEntity);
        //Category3Entity category3Entity = getCategory3Seqno(categorySeqno);
        Category3Entity category3Entity = category3Repository.findById(categorySeqno).get();
        couponCategoryDTO.setCategory3seqno(category3Entity);
        couponCategoryRepository.save(couponCategoryDTO.dtoToEntity(couponCategoryDTO));
    }

    //특정 쿠폰에 대한 카테고리 불러오기
    @Override
    public List<CouponCategoryEntity> getCouponCategories(Long couponSeqno) throws Exception{
        CouponEntity couponEntity = couponRepository.findById(couponSeqno).get();
        return couponCategoryRepository.findByCouponSeqno(couponEntity);
    }

    //쿠폰 타겟 저장
    @Override
    public void saveCouponTarget(Long couponSeqno, ProductEntity product) throws Exception{
        CouponTargetDTO couponTargetDTO = new CouponTargetDTO();
        //CouponEntity couponEntity = getCouponSeqno(couponSeqno);
        CouponEntity couponEntity = couponRepository.findById(couponSeqno).get();
        couponTargetDTO.setCouponSeqno(couponEntity);
        couponTargetDTO.setProductSeqno(product);
        couponTargetRepository.save(couponTargetDTO.dtoToEntity(couponTargetDTO));
    }

    //특정 쿠폰에 대한 타겟 불러오기
    @Override
    public List<CouponTargetEntity> getCouponTargets(Long couponSeqno) throws Exception{
        CouponEntity couponEntity = couponRepository.findById(couponSeqno).get();
        return couponTargetRepository.findByCouponSeqno(couponEntity);
    }

    //쿠폰 카테고리 삭제
    @Override
    public void deleteCouponCategory(Long couponSeqno, Long category3Seqno) throws Exception{
        couponCategoryRepository.deleteByCouponSeqnoAndCategory3Seqno(couponSeqno, category3Seqno);
    }

    //쿠폰 타겟 삭제
    @Override
    public void deleteCouponTarget(Long couponSeqno, Long productSeqno) throws Exception{
        couponTargetRepository.deleteByCouponSeqnoAndProductSeqno(couponSeqno, productSeqno);
    }

    //쿠폰 배포 (자동)
    @Override
    public void couponToUser(Long couponSeqno, boolean isAllMembers, String memberGrade, boolean isBirthday, boolean isNewMember) throws Exception{
        CouponEntity couponEntity = couponRepository.findById(couponSeqno).get();

        List<MemberEntity> members = memberRepository.findAll();
        boolean distribute;

        for(MemberEntity member : members){
            distribute = false;

            if(isAllMembers){//전체 회원 
                distribute = true;
            }else{
                if(memberGrade != null && member.getMemberGrade().equals(memberGrade)){//특정 등급 회원
                    distribute = true;
                }
                if(isBirthday && isBirthdayMember(member)){//생일 회원
                    distribute = true;
                }
                if(isNewMember && isNewMember(member)){//신규 회원
                    distribute = true;
                }
                //첫주문 회원

            }

            if(distribute){
                MemberCouponEntity memberCouponEntity = MemberCouponEntity.builder()
                                                                        .email(member)
                                                                        .couponSeqno(couponEntity)
                                                                        .isExpire("N")
                                                                        .build();
                memberCouponRepository.save(memberCouponEntity);
            }
        }  
    }

    //쿠폰 배포 (수동) - 다운로드
    @Override
    public void downloadCoupon(Long couponSeqno, String email) throws Exception{
        CouponEntity couponEntity = couponRepository.findById(couponSeqno).get();
        MemberEntity memberEntity = memberRepository.findById(email).get();

        MemberCouponEntity existingCoupon = memberCouponRepository.findByCouponSeqno_CouponSeqnoAndEmail_Email(couponSeqno, email);
        
        if(existingCoupon == null){//existingCoupon != null -> 이미 있는 쿠폰
            MemberCouponEntity memberCouponEntity = MemberCouponEntity.builder()
                                                              .email(memberEntity)
                                                              .couponSeqno(couponEntity)
                                                              .isExpire("N")
                                                              .build();
            memberCouponRepository.save(memberCouponEntity);
        }
    }

    //쿠폰 배포 (수동) - 코드 등록
    @Override
    public void getCouponCode(String couponCode, String email) throws Exception{
        CouponEntity couponEntity = couponRepository.findByCouponCode(couponCode);
        MemberEntity memberEntity = memberRepository.findById(email).get();

        MemberCouponEntity existingCoupon = memberCouponRepository.findByCouponSeqno_CouponSeqnoAndEmail_Email(couponEntity.getCouponSeqno(), email);
        
        if(existingCoupon == null){//existingCoupon != null -> 이미 있는 쿠폰
            MemberCouponEntity memberCouponEntity = MemberCouponEntity.builder()
                                                                  .email(memberEntity)
                                                                  .couponSeqno(couponEntity)
                                                                  .isExpire("N") 
                                                                  .build();
            memberCouponRepository.save(memberCouponEntity);
        }
    }

    //생일 회원
    private boolean isBirthdayMember(MemberEntity member){
        LocalDateTime today = LocalDateTime.now();
        return today.getMonth() == member.getBirthDate().getMonth() && today.getDayOfMonth() == member.getBirthDate().getDayOfMonth();
    }
    
    //신규 회원
    private boolean isNewMember(MemberEntity member){
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return member.getRegdate().isAfter(oneMonthAgo); 
    }

    //쿠폰 코드 랜덤 생성
    private String generateCouponCode() {
        return UUID.randomUUID().toString().substring(0, 15);
    }

    //리뷰 리스트 
    @Override
    public Page<Map<String, Object>> reviewList(int pageNum, int postNum, Long category) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "revDate"));
        Page<ReviewEntity> reviewEntities;

        // 카테고리 값이 있을 경우, 카테고리 기준으로 리뷰 조회
        if (category != null) {
            reviewEntities = reviewRepository.findByCategory(category, pageRequest);
        } else {
            reviewEntities = reviewRepository.findAll(pageRequest);
        }

        return reviewEntities.map(reviewEntity -> {
            Map<String, Object> result = new HashMap<>();
            ReviewDTO reviewDTO = new ReviewDTO(reviewEntity);

            // 리뷰 정보
            result.put("review", reviewDTO);

            // 첨부 파일 조회
            List<ReviewFileDTO> reviewFiles = new ArrayList<>();
            try {
                reviewFiles = reviewService.fileListView(reviewEntity.getReviewSeqno());
            } catch (Exception e) {
                reviewFiles = new ArrayList<>();
            }

            // 파일이 존재하면 추가, 없으면 빈 리스트 반환
            result.put("reviewFiles", reviewFiles.isEmpty() ? new ArrayList<>() : reviewFiles);
            return result;
        });
    }

    //리뷰 삭제
    @Override
	public void deleteReview(Long reviewSeqno) throws Exception{
        reviewRepository.deleteById(reviewSeqno);
    }

    //리뷰 첨부파일 삭제
    @Override
    public void deleteReviewFile(Long reviewSeqno) throws Exception{
        ReviewEntity reviewEntity = reviewRepository.findById(reviewSeqno).get();
        List<ReviewFileEntity> reviewFiles = reviewFileRepository.findByReviewSeqno(reviewEntity);
        for (ReviewFileEntity reviewFile : reviewFiles) {
            /* 
            File file = new File(reviewFile.getStoredFilename()); 
            if (file.exists()) {
                file.delete(); // 파일 삭제
            }
            */
            reviewFileRepository.delete(reviewFile); 
        }
    }

    //리뷰 신고 리스트 
    @Override
    public Page<ReportEntity> reviewReposrtList(int pageNum, int postNum, String reportTitle) throws Exception{
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"reportDate"));
        return reportRepository.findByReportTitleContaining(reportTitle, pageRequest);
    }

    //리뷰 신고 삭제
    @Override
    public void deleteReport(Long reportSeqno) throws Exception{
        reportRepository.deleteById(reportSeqno);
    }
    

    //전체 회원의 누적 구매금액을 조회 후 총 누적 구매금액 업데이트, 
    //최근 3년이내의 누적 구매금액을 기준으로 등급 업데이트
    @Override
    public void calculateAndUpdateCustomerGrade(LocalDateTime referenceDate) {
        List<MemberEntity> members = memberRepository.findAll();

        for (MemberEntity member : members) {
            int totalSpentLast3Years = 0;  // 최근 3년 이내의 누적구매금액
            LocalDateTime threeYearsAgo = referenceDate.minusYears(3); 
            List<OrderInfoEntity> orders = orderInfoRepository.findByEmail_Email(member.getEmail());

            for (OrderInfoEntity order : orders) {
                int orderTotalPrice = order.getTotalPrice();

                // 최근 3년 이내의 누적구매금액 구하기
                if (order.getOrderDate().isAfter(threeYearsAgo)) {
                totalSpentLast3Years += orderTotalPrice;
                }
            }
            // 회원의 총 누적구매금액 업데이트
            member.setTotalPvalue(totalSpentLast3Years);

            //최근 3년 이내의 누적구매 금액에 따라 회원등급 설정
            String grade;
            if (totalSpentLast3Years >= 1000000) {
                grade = "Platinum";
            } else if (totalSpentLast3Years >= 500000) {
                grade = "Gold";
            } else if (totalSpentLast3Years >= 100000) {
                grade = "Silver";
            } else {
                grade = "Bronze";
            }

            //회원 객체의 memberGrade 업데이트 및 저장
            member.setMemberGrade(grade);
            //memberRepository.save(member);
        }   
        // 모든 회원 정보 한 번에 저장
        memberRepository.saveAll(members); 
    }

    // 결제취소 및 환불 신청 내역 조회
    @Override
    public List<OrderDetailEntity> getCancelAndRefundDetails() {
        // '결제취소신청' 또는 '환불신청'인 주문 상태를 가진 주문 조회
        List<String> statuses = List.of("결제취소신청", "환불신청");
        List<OrderInfoEntity> orders = orderInfoRepository.findByOrderStatusIn(statuses);

        // 각 주문에 대해 orderDetail 리스트 반환
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        for (OrderInfoEntity order : orders) {
            orderDetails.addAll(orderDetailRepository.findByOrderSeqno_OrderSeqno(order));
        }

        return orderDetails;
    }

    // 결제 취소 처리 및 환불 처리(isRefund가 true면 환불, false면 결제취소 처리)
    @Override
    @Transactional
    public void cancelOrRefundOrder(Long orderDetailSeqno, Long couponSeqno, int point, boolean isRefund) {
        OrderDetailEntity orderDetail = orderDetailRepository.findById(orderDetailSeqno).get();

        // 주문 상세의 orderSeqno를 사용해서 OrderInfoEntity 조회
        OrderInfoEntity orderInfo = orderInfoRepository.findByOrderSeqno(orderDetail.getOrderSeqno());

        // 결제 취소 또는 환불에 따른 주문 상태 변경
        String statusMessage = isRefund ? "환불 완료" : "결제 취소 완료";
        if (orderInfo.getOrderStatus().equals(statusMessage)) {
            throw new RuntimeException("이미 " + statusMessage + "된 주문입니다.");
        }
        orderInfo.setOrderStatus(statusMessage);
        orderInfoRepository.save(orderInfo);

        // 주문 상품 목록 조회
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderSeqno_OrderSeqno(orderInfo);

        // 주문 상세 정보 처리 
        for (OrderDetailEntity orderDetailEntity : orderDetails) {

            if (isRefund) {
                orderDetailEntity.setRefundYn("Y");
            } else {
                orderDetailEntity.setCancelYn("Y");
            }
            orderDetailRepository.save(orderDetailEntity);

            // 주문 상품 정보
            OrderProductEntity orderProduct = orderDetailEntity.getOrderProductSeqno();

            // 주문된 상품의 재고 복구
            ProductEntity product = orderProduct.getProductSeqno();
            product.setStockAmount(product.getStockAmount() + orderProduct.getAmount());
            productRepository.save(product);
        } 

        // 회원정보 불러오기
        MemberEntity member = memberRepository.findById(orderInfo.getEmail().getEmail()).get();

        // 처리완료 후 회원의 누적 구매 금액에서 해당 주문 금액 차감처리
        int totalPrice = orderInfo.getTotalPrice();
        member.setTotalPvalue(member.getTotalPvalue() - totalPrice);

        // 사용한 포인트 및 쿠폰 복구 처리
        // 포인트 복구 처리
        if (point != 0) {
            member.setPoint(member.getPoint() + point); // 환불시 포인트 복구
        }

        // 쿠폰 복원 처리 (만료처리된 쿠폰 복원)
        if (couponSeqno != null) {
            //쿠폰 상태 업데이트 (사용 가능 상태로 변경)
            //CouponEntity coupon = couponRepository.findById(couponSeqno).get();
            //coupon.setIsExpire("N");

            //membercoupon으로 변경
            MemberCouponEntity memberCoupon = memberCouponRepository.findByCouponSeqno_CouponSeqno(couponSeqno);
            memberCoupon.setIsExpire("N");
        }
        memberRepository.save(member);  

        //회원 알림 설정
        String notificationName = isRefund ? "환불 완료" : "결제 취소 완료";
        NotificationEntity notification = NotificationEntity.builder()
                                                            .notificationName(notificationName)  
                                                            .notificationDate(LocalDateTime.now())  
                                                            .build();
    
        // 알림 저장
        notificationRepository.save(notification);

        // 회원에게 알릴 알림 저장
        MemberNotificationEntity memberNotification = MemberNotificationEntity.builder()
                                                                              .email(member)
                                                                              .notificationSeqno(notification)
                                                                              .build();
        memberNotificationRepository.save(memberNotification);

    }

    //관리자가 쿠폰종료일이 지난 쿠폰들 isExpired를 "Y"로 업데이트(memberCoupon으로 변경)
    @Override
    public void setExpiredCouponsToExpired(LocalDateTime referenceDate) {
        // 만료된 쿠폰만 조회
        List<CouponEntity> expiredCoupons = couponRepository.findExpiredCoupons(referenceDate);
    
        // 만료된 쿠폰을 업데이트
        for (CouponEntity expiredCoupon : expiredCoupons) {
            // CouponEntity에서 couponSeqno 값을 추출하여 MemberCoupon 조회
            List<MemberCouponEntity> memberCoupons = memberCouponRepository.findAllByCouponSeqno_CouponSeqno(expiredCoupon.getCouponSeqno());
            
            // 여러 개의 MemberCouponEntity가 반환되므로, 리스트 순회하며 'isExpire'를 업데이트
            for (MemberCouponEntity memberCoupon : memberCoupons) {
                memberCoupon.setIsExpire("Y");
            }
    
            // 업데이트 결과를 데이터베이스에 반영
            memberCouponRepository.saveAll(memberCoupons);
        }
    }
    
    //카테고리별 매출 통계
    @Override
    public List<CategorySalesDTO> getCategorySales() {
        return orderDetailRepository.getCategorySales();
    }

    //일별 매출 통계
    @Override
    public List<DailySalesDTO> getDailySales(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = orderInfoRepository.findDailySalesWithinDateRange(startDate, endDate);
        return results.stream()
                .map(row -> new DailySalesDTO(
                    ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate(), // Timestamp를 LocalDate로 변환
                    ((Number) row[1]).intValue()                                  // 합계 매출액
                ))
                .collect(Collectors.toList());
    }

    //월별 매출 통계
    @Override
    public List<MonthlySalesDTO> getYearlySales(int year) {
        List<Object[]> results = orderInfoRepository.findMonthlySalesByYear(year);
        return results.stream()
                .map(row -> new MonthlySalesDTO(
                    ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate().withDayOfMonth(1), // 월별로 첫날을 사용
                    ((Number) row[1]).intValue()                                                   // 합계 매출액
                ))
                .collect(Collectors.toList());
    }

    //회원별 매출 통계
    @Override
    public List<MemberSalesDTO> getMemberSales() {
        return memberRepository.findAllMemberSales();
    }

    //연령별 매출 통계
    @Override
    public List<SalesByAgeGroupDTO> getSalesByAge() {
        // findSalesByAgeGroup() 메소드가 Object[] 리스트를 반환하므로 이를 SalesByAgeGroupDTO로 변환
        List<Object[]> results = memberRepository.findSalesByAgeGroup();
        List<SalesByAgeGroupDTO> salesByAgeGroupDTOs = new ArrayList<>();
        
        // Object[]에서 값을 추출하여 SalesByAgeGroupDTO로 변환
        for (Object[] result : results) {
            String ageGroup = (String) result[0]; // 나이대
            BigDecimal totalSales = (BigDecimal) result[1]; // 판매 총액
            salesByAgeGroupDTOs.add(new SalesByAgeGroupDTO(ageGroup, totalSales));
        }
        
        return salesByAgeGroupDTOs;
    }
    
    //등급별 매출 통계
    @Override
    public List<SalesByMemberGradeDTO> getSalesByGrade() {
        return memberRepository.findSalesByGrade();
    }
    
    //상품별 매출 통계
    @Override
      public List<ProductSalesDTO> getSalesByProduct() {
        // Repository에서 데이터를 가져오고 DTO로 매핑
        return orderDetailRepository.findProductSales()
            .stream()
            .map(row -> new ProductSalesDTO((String) row[0], (BigDecimal) row[1]))
            .toList();
    }
    
    //가입일 기준 가입 통계
    @Override
    public List<SignupDateStatDTO> getSignupDateStat(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // DateTimeFormatter를 사용하여 LocalDateTime을 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDate = startDateTime.format(formatter); // LocalDateTime을 String으로 변환
        String endDate = endDateTime.format(formatter);     // LocalDateTime을 String으로 변환
    
        // 수정된 쿼리 호출
        List<Object[]> results = memberRepository.findSignupDateStat(startDate, endDate);
        List<SignupDateStatDTO> dtoList = new ArrayList<>();
    
        // 쿼리 결과를 DTO로 변환
        for (Object[] result : results) {
            // java.sql.Timestamp를 LocalDate로 변환
            LocalDate date = ((java.sql.Timestamp) result[0]).toLocalDateTime().toLocalDate(); // 날짜만 추출
    
            Long signupCount = ((Number) result[1]).longValue(); // `COUNT(m)` 결과를 Long으로 변환
    
            // DTO 객체 생성
            SignupDateStatDTO dto = new SignupDateStatDTO(date, signupCount);
            dtoList.add(dto);
        }
    
        // 최종 결과 반환
        return dtoList;
    }

    //성별 기준 가입 통계
    @Override
    public List<SignupGenderStatDTO> getSignupGenderStat(){
        return memberRepository.findSignupGenderStat();
    }

    //연령대 기준 가입 통계
    @Override
    public List<SignupAgeStatDTO> getSignupAgeStat() {
        // native query 결과를 가져옵니다.
        List<Object[]> results = memberRepository.findSignupAgeStat();
        
        // 결과를 SignupAgeStatDTO로 변환
        List<SignupAgeStatDTO> signupAgeStatDTOs = new ArrayList<>();
        
        // Object[]에서 값을 추출하여 SignupAgeStatDTO로 변환
        for (Object[] result : results) {
            String ageGroup = (String) result[0]; // 나이대
            BigDecimal ageGroupCount = (BigDecimal) result[1]; // 가입자 수
            signupAgeStatDTOs.add(new SignupAgeStatDTO(ageGroup, ageGroupCount));
        }
        
        return signupAgeStatDTOs;
    }

    //일별 방문자 통계
    @Override
    public List<DailyVisitorDTO> getDailyVisitors(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = memberLogRepository.findDailyVisitorsWithinDateRange(startDate, endDate);
        return results.stream()
                .map(row -> new DailyVisitorDTO(
                    ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate(), // Timestamp를 LocalDate로 변환
                    ((Number) row[1]).intValue()                                  // 방문자 수
                ))
                .collect(Collectors.toList());
    }
    
}

    
