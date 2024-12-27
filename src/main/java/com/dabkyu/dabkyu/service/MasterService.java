package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.dabkyu.dabkyu.dto.RelatedProductDTO;
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
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.OrderProductOptionEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductFileEntity;
import com.dabkyu.dabkyu.entity.ProductInfoFileEntity;
import com.dabkyu.dabkyu.entity.ProductOptionEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.ReportEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;

public interface MasterService {

    //맴버 리스트
    public Page<MemberEntity> memberList(int pageNum, int postNum, String keyword, String memberGrade) throws Exception;

    //맴버 이메일로 검색
    public MemberDTO getMemberByEmail(String email);

    //맴버 등급 수정
    public void memberModify(MemberDTO memberDTO) throws Exception;

    //맴버 삭제
    public void clientDelete(String email) throws Exception;

    //상품 리스트 
    public Page<ProductEntity> productList(Long category1Seqno, Long category2Seqno, Long category3Seqno, String productName, PageRequest pageable);

    //productSeqno로 상품 정보 가져오기
    public ProductEntity getProductBySeqno(Long productSeqno) throws Exception;

    //상품 옵션 정보 가져오기
    public List<ProductOptionDTO> getProductOptions(Long productSeqno) throws Exception;

    //추가 상품 정보 가져오기
    public List<RelatedProductDTO> getRelatedProducts(Long productSeqno) throws Exception;

    //카테고리 정보 가져오기
    public Category3Entity getCategory3ByProduct(Long productSeqno) throws Exception;
    public Category2Entity getCategory2ByProduct(Long productSeqno) throws Exception;
    public Category1Entity getCategory1ByProduct(Long productSeqno) throws Exception;

    //상품 파일 정보 가져오기
    public List<ProductFileDTO> getProductFiles(Long productSeqno) throws Exception;

    //상품설명 이미지 파일 정보 가져오기
    public List<ProductInfoFileDTO> getProductInfoFiles(Long productSeqno) throws Exception;

    //상품등록
    public ProductEntity productPost(ProductDTO productDTO) throws Exception;

    //상품수정
    public void productModify(ProductDTO productDTO) throws Exception;

    //상품 카테고리 저장
    public void setProductCategory(Long category3Seqno, ProductDTO productDTO) throws Exception;

    //상품 옵션 저장
    public void saveProductOption(ProductOptionDTO productOptionDTO) throws Exception;

    //추가상품 저장
    public void saveRelatedProduct(RelatedProductDTO relatedProductDTO) throws Exception;

    //상품 이미지 파일 등록
    public void productImgFile(ProductFileDTO productFileDTO) throws Exception;

    //상품 상세보기 이미지 파일 등록
    public void productDetailImgFile(ProductInfoFileDTO productInfoFileDTO) throws Exception;
    
    //옵션 삭제
    public void deleteProductOption(Long optionSeqno);
 
    //추가상품 삭제
    public void deleteRelatedProduct(Long relatedProductSeqno);

    //상품 이미지 삭제
    public void deleteProductFile(Long fileseqno) throws Exception;

    //상품 상세보기 이미지 삭제
    public void deleteProductInfoFile(Long productInfoFileSeqno) throws Exception;

    //상품 비활성화
    public void secretYNProduct(Long productSeqno) throws Exception;

    ////////////주문
    //주문 리스트
    public Page<Map<String, Object>> orderList(int pageNum, int postNum, String productname, Long category) throws Exception;

    //주문 상태 수정
    public void modifyOrderStatus(Long orderSeqno, String orderStatus) throws Exception;

    //OrderSeqno찾기
    public OrderInfoEntity getOrderInfo(Long orderSeqno) throws Exception;

    //OrderDetail찾기
    public List<OrderDetailEntity> getOrderDetails(Long orderSeqno) throws Exception;

    //OrderProduct찾기
    public List<OrderProductEntity> getOrderProducts(Long productSeqno) throws Exception;

    //AddedRelatedProduct찾기
    public List<AddedRelatedProductEntity> getAddedRelatedProducts(List<OrderProductEntity> orderProducts) throws Exception;

    //OrderProductOption찾기
    public List<OrderProductOptionEntity> getOrderProductOptions(List<OrderProductEntity> orderProducts) throws Exception;

    //product찾기
    public List<ProductEntity> getProducts(List<OrderProductEntity> orderProducts) throws Exception;

    //문의 리스트
    public Page<Map<String, Object>> questionList(int pageNum, int postNum, String queType) throws Exception;

    //문의 queSeqno가져오기
    public QuestionEntity getQuestionSeqno(Long queSeqno) throws Exception;

    //문의 답변
    public void replyQuestion(QuestionCommentDTO commentDTO, QuestionEntity questionEntity) throws Exception;

    //문의 수정
    public void replyQuestionModify(QuestionCommentDTO comment) throws Exception;

    //문의 삭제
    public void deleteQuestion(Long queSeqno) throws Exception;

    //문의 첨부파일 삭제
    public void deleteQuestionFile(Long queSeqno) throws Exception;

    //문의 답변 삭제   
    public void deleteQueComment(QuestionEntity queSeqno) throws Exception;

    //카테고리 리스트
    public List<Category1Entity> getAllCategories1();
    public List<Category2Entity> getAllCategories2();
    public List<Category3Entity> getAllCategories3();

    //카테고리 추가
    public void saveCategories(List<Category1DTO> list1, List<Category2DTO> list2, List<Category3DTO> list3) throws Exception;

    //카테고리 삭제
    public void deleteCategory1(Long categoryId) throws Exception;
    public void deleteCategory2(Long categoryId) throws Exception;
    public void deleteCategory3(Long categoryId) throws Exception;

    //쿠폰 리스트
    public Page<Map<String,Object>> couponList(int pageNum, int postNum) throws Exception;
    
    //쿠폰 상세보기
    public CouponEntity getCouponBySeqno(Long couponSeqno) throws Exception;

    //쿠폰 seqno가져오기
    public Long writeCoupon(CouponDTO couponDTO) throws Exception;

    //쿠폰수정
	public void modifyCoupon(CouponDTO coupon) throws Exception;

	// 모든 상품 가져오기
    public List<ProductEntity> getAllProducts() throws Exception;
  
    //쿠폰 카테고리 저장
    public void saveCouponCategory(Long couponSeqno, Long categoryId) throws Exception;

    //특정 쿠폰에 대한 카테고리 불러오기
    public List<CouponCategoryEntity> getCouponCategories(Long couponSeqno) throws Exception;

    //쿠폰 타겟 저장
    public void saveCouponTarget(Long couponSeqno, ProductEntity product) throws Exception;

    //특정 쿠폰에 대한 타겟 불러오기
    public List<CouponTargetEntity> getCouponTargets(Long couponSeqno) throws Exception;

	//쿠폰 카테고리 삭제
    public void deleteCouponCategory(Long couponSeqno, Long category3Seqno) throws Exception;

    //쿠폰 타겟 삭제
    public void deleteCouponTarget(Long couponSeqno, Long productSeqno) throws Exception;

    //쿠폰 배포 (자동-관리자가)
    public void couponToUser(Long couponSeqno, boolean isAllMembers, String memberGrade, boolean isBirthday, boolean isNewMember,
    boolean isFirstOrderMember,boolean isNoOrdersLastYearMember) throws Exception;

    //쿠폰 배포 (수동) - 회원이 다운로드
    public void downloadCoupon(Long couponSeqno, String email) throws Exception;

    //쿠폰 배포 (수동) - 코드 등록
    public void getCouponCode(String couponCode, String email) throws Exception;

    //리뷰 리스트
    public Page<Map<String, Object>> reviewList(int pageNum, int postNum, Long category) throws Exception;

    //리뷰 삭제
	public void deleteReview(Long reviewSeqno) throws Exception;

    //리뷰 첨부파일 삭제
    public void deleteReviewFile(Long reviewSeqno) throws Exception;

    //리뷰 신고 리스트 
    public Page<ReportEntity> reviewReposrtList(int pageNum, int postNum, String reportTitle) throws Exception;

    //리뷰 신고 삭제
    public void deleteReport(Long reportSeqno) throws Exception;

    //전체 회원의 누적구매금액을 조회 후 등급업데이트
    public void calculateAndUpdateCustomerGrade(LocalDateTime referenceDate);

    //결제 취소 및 환불 신청 내역 보기
    public List<OrderDetailEntity> getCancelAndRefundDetails();

    //결제 취소 및 환불 처리
    public void cancelOrRefundOrder(Long orderDetailSeqno, Long couponSeqno, int point, boolean isRefund);
    
    public MemberEntity getMemberEmail(String email);

    //관리자가 쿠폰종료일이 지난 쿠폰들을 isExpired를 "Y"로 업데이트해서 만료처리
    public void setExpiredCouponsToExpired(LocalDateTime referenceDate);

    //카테고리별 매출 통계
    public List<CategorySalesDTO> getCategorySales();

    //일별 매출 통계
    public List<DailySalesDTO> getDailySales(LocalDateTime startDateTime, LocalDateTime endDateTime);

    //월별 매출 통계
    public List<MonthlySalesDTO> getYearlySales(int year);

    //회원별 매출 통계
    public List<MemberSalesDTO> getMemberSales();

    //연령대별 매출 통계
    public List<SalesByAgeGroupDTO> getSalesByAge();

    //등급별 매출 통계
    public List<SalesByMemberGradeDTO> getSalesByGrade();

    //상품별 매출 통계
    public List<ProductSalesDTO> getSalesByProduct();

    //가입일 기준 가입 통계
    public List<SignupDateStatDTO> getSignupDateStat(LocalDateTime startDateTime, LocalDateTime endDateTime);

    //성별 기준 가입 통계
    public List<SignupGenderStatDTO> getSignupGenderStat();

    //연령대 기준 가입 통계
    public List<SignupAgeStatDTO> getSignupAgeStat();

    //일별 방문자 통계 
    public List<DailyVisitorDTO> getDailyVisitors(LocalDateTime startDate, LocalDateTime endDate);

    public List<Category1Entity> getCategories1();

    public List<Category2Entity> getCategories2ByCategory1(Long category1Seqno);

    public List<Category3Entity> getCategories3ByCategory2(Long category2Seqno);

    //카테고리3 찾기
    public Category3Entity findCategoryBySeqno(Long category3Seqno);

    //관리자페이지 메인 오늘 방문자 수 
    public int getTodayVisitorCount();

    //관리자페이지 메인 오늘 리뷰 수 
    public int getTodayReviewCount();

    // 관리자페이지 메인 오늘 문의 수
    public int getTodayQuestionCount();

    //관리자페이지 메인 답변 대기수
    public int getNumberOfPendingQuestions();

    //카테고리1 순서 구하기
    public Integer findMaxCategory1Order(); 

    //카테고리 1 저장
    public void saveCategory1(Category1Entity category1);

    //카테고리 1 찾기
    public Category1Entity findCategory1ById(Long category1Seqno);

    //카테고리2 순서 구하기
    public Integer findMaxCategory2OrderByCategory1(Long category1Seqno);

    //카테고리 2 저장
    public void saveCategory2(Category2Entity category2);

    //카테고리 2 찾기
    public Category2Entity findCategory2ById(Long category2Seqno);

    //카테고리 3 찾기
    public Category3Entity findCategory3ById(Long category3Seqno);

    //카테고리3 순서 구하기
    public Integer findMaxCategory3OrderByCategory2(Long category2Seqno);

    //카테고리 3 저장
    public void saveCategory3(Category3Entity category3);

    //카테고리 순서 변경
    public void updateCategoryOrder(int level, Long categoryId, String direction);

    //모든 쿠폰 가져오기
    public List<CouponEntity> getAllCoupons();

    //쿠폰 상세 보기
    public Map<String, Object> getAllCouponDetail(Long couponSeqno);

    //쿠폰 생성 시 카테고리 3 조회
    public Category3Entity findCategory3BySeqno(Long category3Seqno);

    //쿠폰 찾기
    public CouponEntity findCouponBySeqno(Long couponSeqno);

    //카테고리 정보 저장
    public void saveCouponCategories(Category3Entity category3Entity, CouponEntity couponEntity);
    
    //상품 찾기
    public ProductEntity findProductBySeqno(Long productSeqno);

    //타겟 정보 저장
    public void saveCouponTargets(ProductEntity productEntity, CouponEntity couponEntity);

    //기존 쿠폰에 연결된 카테고리 삭제
    public void deleteCouponCategories(Long couponSeqno);

    //기존 쿠폰에 연결된 제품 삭제
    public void deleteCouponProducts(Long couponSeqno);

    //관리자가 선택한 쿠폰을 isExpired를 "Y"로 업데이트해서 만료처리
    public void markCouponAsInactive(Long couponSeqno);

    //관리자가 선택한 여러 쿠폰을 isExpired를 "Y"로 업데이트해서 만료처리
    public void markMultipleCouponsAsInactive(List<Long> couponSeqnos);

    //회원 리스트 조회
    public List<MemberEntity> getAllMembers();

    //회원 상세 보기
    public Map<String, Object> getAllMemberDetail(String email);

    //회원 등급 수정
    public void updateMemberGrade(String email, String newGrade);

    //회원 활성화여부 수정
    public void changeMemberActive(String email, String newActive);

    //상품 이미지 찾기
    public List<ProductFileEntity> getProductImagesByProductSeqno(ProductEntity productEntity);

    //상품 이미지 삭제
    public void deleteProductImage(ProductFileEntity productFileEntity);

    //상품 상세 이미지 찾기
    public List<ProductInfoFileEntity> getProductDetailImagesByProductSeqno(ProductEntity productEntity);

    //상품 상세 이미지 삭제
    public void deleteProductDetailImage(ProductInfoFileEntity productInfoFileEntity);

    public ProductOptionEntity findProductOptionBySeqno(Long optionSeqno);

    //주문 리스트 조회
    public List<OrderInfoEntity> getAllOrders();

    //주문 상세 조회
    public Map<String, Object> getAllOrderDetail(Long orderSeqno);

}
