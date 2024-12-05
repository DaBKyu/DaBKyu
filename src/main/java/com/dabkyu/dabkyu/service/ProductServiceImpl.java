package com.dabkyu.dabkyu.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.dto.ProductOptionDTO;
import com.dabkyu.dabkyu.dto.RelatedProductDTO;
import com.dabkyu.dabkyu.dto.ReportDTO;
import com.dabkyu.dabkyu.dto.TopSellingProductDTO;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductFileEntity;
import com.dabkyu.dabkyu.entity.ProductInfoFileEntity;
import com.dabkyu.dabkyu.entity.repository.Category1Repository;
import com.dabkyu.dabkyu.entity.repository.Category2Repository;
import com.dabkyu.dabkyu.entity.repository.Category3Repository;
import com.dabkyu.dabkyu.entity.ProductOptionEntity;
import com.dabkyu.dabkyu.entity.RelatedProductEntity;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.OrderDetailRepository;
import com.dabkyu.dabkyu.entity.repository.OrderProductRepository;
import com.dabkyu.dabkyu.entity.repository.ProductFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductInfoFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductOptionRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.entity.repository.RelatedProductRepository;
import com.dabkyu.dabkyu.entity.repository.ReportRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ReportRepository reportRepository;
	private final Category1Repository category1Repository;
	private final Category2Repository category2Repository;
	private final Category3Repository category3Repository;
	private final ProductFileRepository productFileRepository;
	private final ProductInfoFileRepository productInfoFileRepository;
	private final ProductOptionRepository productOptionRepository;
	private final RelatedProductRepository relatedProductRepository;
	private final MemberRepository memberRepository;
	private final OrderDetailRepository orderDetailRepository;
    private final OrderProductRepository orderProductRepository;

	////////////내가만든거/////////////
	// seachAll에서 사용할 전체 상품 보기
	@Override
	public Page<ProductEntity> productAllList(int pageNum, int postNum, String keyword) throws Exception{
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"productSeqno"));
		return productRepository.findByProductNameContaining(keyword, pageRequest);
	}

    // main 썸네일 리스트
    @Override
	public List<ProductFileEntity> productFileMainList(List<ProductEntity> productList) {
        List<ProductFileEntity> productFileMainList = new ArrayList<>();
        String isThumb = "Y";
        for (ProductEntity product : productList) {

            productFileMainList.add(productFileRepository.findFirstByProductSeqnoAndIsThumb(product, isThumb));
        }
        return productFileMainList;
    }

    // 썸네일 주소 가져오기
    @Override
	public String getProductThumbnail(ProductEntity productEntity) {
        ProductFileEntity productFileEntity = productFileRepository.findFirstByProductSeqnoAndIsThumb(productEntity, "Y");
        log.info("--------------------productFileEntity: {}-----------------------", productFileEntity);
        return productFileEntity==null?"isNull":productFileEntity.getStoredFilename();
    }

	//상품 이미지 보기////
	public List<ProductFileEntity> productFileList(Long productSeqno) throws Exception {
		return productFileRepository.findByProductSeqno_ProductSeqno(productSeqno);
	 
	}
	/////////////////////////////////
	//제품 설명 첨부 파일 보기
	public List<ProductInfoFileEntity> productInfoFileList(Long productSeqno) throws Exception {
		return productInfoFileRepository.findByProductSeqno_ProductSeqno(productSeqno);
	}

	//전체 카테고리1 목록 보기
	@Override
	public List<Category1Entity> category1List() throws Exception {
		return category1Repository.findAll();
	}

	//전체 카테고리2 목록 보기
	@Override
	public List<Category2Entity> category2List() throws Exception {
		return category2Repository.findAll();
	}

	//전체 카테고리3 목록 보기
	@Override
	public List<Category3Entity> category3List() throws Exception {
		return category3Repository.findAll();
	}

	// 전체 상품 보기
	public List<ProductEntity> productList() throws Exception {
		return productRepository.findAll();
	}



	//상품 목록 보기(카테고리 조회)
	@Override
	public Page<ProductEntity> list(int pageNum, int postNum, String keyword, Long cateSeqno) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"productSeqno"));
		 // 대분류, 중분류, 소분류 순으로 필터링
		 //if (category1Seqno != null) {
            // 대분류 카테고리가 선택된 경우
           // return productRepository.findByCategory1SeqnoAndProductNameContaining(category1Seqno, keyword, pageRequest);
        //} else if (category2Seqno != null) {
            // 중분류 카테고리가 선택된 경우
            //return productRepository.findByCategory2SeqnoAndProductNameContaining(category2Seqno, keyword, pageRequest);
        //} else if (category3Seqno != null) {
		if (cateSeqno != null) {
            // 소분류 카테고리가 선택된 경우
            return productRepository.findByCategory3Seqno_Category3SeqnoAndProductNameContaining(cateSeqno, keyword, pageRequest);
        } else {
            // 카테고리가 선택되지 않은 경우 전체 상품 목록 조회
            return productRepository.findByProductNameContaining(keyword, pageRequest);
   		}		
	}

   //가장 많이 팔린 상품 10개 조회
    @Override
    public List<TopSellingProductDTO> getTop10BestSellingProducts() throws Exception {
        return orderProductRepository.findTop10SellingProducts();
    }
    
    // 로그인한 사용자의 연령대별 가장 많이 팔린 상품 10개 조회 
    @Override
    public Map<String, List<TopProduct>> getTopProductsByAgeForUser(String email) throws Exception {
        List<MemberEntity> members = memberRepository.findAll();
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findAll();

        Map<String, List<TopProduct>> result = new HashMap<>();

        // 특정 이메일을 가진 회원만 처리
        Optional<MemberEntity> orderMemberEntityOptional = members.stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst();

        if (orderMemberEntityOptional.isPresent()) {
            MemberEntity orderMember = orderMemberEntityOptional.get();

            // 나이 계산
            LocalDate orderBirthDate = orderMember.getBirthDate();
            int orderAge = Period.between(orderBirthDate, LocalDate.now()).getYears();
            System.out.println("회원 연령: " + orderAge); // 나이 확인 로그

            // 연령대 계산
            String orderAgeGroup = getAgeGroup(orderAge);
            System.out.println("회원 연령대: " + orderAgeGroup); // 연령대 확인 로그

            // 해당 연령대의 모든 회원 주문 내역에서 상품 처리
            List<TopProduct> topProducts = new ArrayList<>();

            for (OrderDetailEntity orderDetail : orderDetails) {
                MemberEntity orderDetailMember = orderDetail.getOrderSeqno().getEmail();

                // 주문 회원의 연령대 확인
                LocalDate birthDate = orderDetailMember.getBirthDate();
                int memberAge = Period.between(birthDate, LocalDate.now()).getYears();
                String memberAgeGroup = getAgeGroup(memberAge);

                // 로그인된 회원의 연령대와 동일한 연령대의 주문만 처리
                if (memberAgeGroup.equals(orderAgeGroup)) {  // 로그인한 회원의 연령대와 일치하는 주문만 처리
                    // 연령대가 일치하고, 취소나 환불되지 않은 상품만 처리
                    if (!"Y".equals(orderDetail.getCancelYn()) && !"Y".equals(orderDetail.getRefundYn())) {
                        OrderProductEntity orderProduct = orderDetail.getOrderProductSeqno();

                        // 상품 정보가 null인 경우 처리
                        if (orderProduct != null) {
                            String productName = orderProduct.getProductSeqno().getProductName();
                            String storedFilename = getStoredFilename(orderProduct.getProductSeqno()); // 수정된 부분
                            int amount = orderProduct.getAmount();
                            Long productSeqno = orderProduct.getProductSeqno().getProductSeqno(); // 추가된 부분
                            addProductToTopList(topProducts, productName, storedFilename, amount, productSeqno); // productSeqno 추가
                        } else {
                            System.out.println("상품 정보가 없습니다. 주문 세부 정보: " + orderDetail); // 상품 정보가 없을 경우 로그
                        }
                    }
                }
            }

            // 연령대별로 구매 횟수 기준 상위 10개 상품 추출
            topProducts.sort((p1, p2) -> Integer.compare(p2.getPurchaseCount(), p1.getPurchaseCount()));
            result.put(orderAgeGroup, topProducts.size() > 10 ? topProducts.subList(0, 10) : topProducts);
        } else {
            System.out.println("회원이 존재하지 않습니다."); // 회원이 없는 경우 로그
        }

        return result;
    }

    // 상품에 해당하는 썸네일 파일명과 productSeqno 가져오기
    private String getStoredFilename(ProductEntity product) {
        // 상품이 null이 아니고, 관련된 파일들이 있을 경우
        if (product != null && product.getProductFiles() != null) {
            // 썸네일(is_thumb == "Y")인 파일을 찾아서 해당 파일의 storedFilename을 반환
            return product.getProductFiles().stream()
                    .filter(productFile -> "Y".equals(productFile.getIsThumb())) // 썸네일만 선택
                    .map(ProductFileEntity::getStoredFilename) // stored_filename을 가져옴
                    .findFirst() // 첫 번째 썸네일만 반환
                    .orElse(null); // 썸네일이 없으면 null 반환
        }
        return null; // 상품이나 파일이 없으면 null 반환
    }

    // 나이를 기준으로 연령대 구하는 메서드
    private String getAgeGroup(int age) {
        if (age >= 20 && age < 30) {
            return "20대";
        } else if (age >= 30 && age < 40) {
            return "30대";
        } else if (age >= 40 && age < 50) {
            return "40대";
        } else if (age >= 50) {
            return "50대 이상";
        } else {
            return "미성년자";  // 20세 미만은 제외하거나 다른 그룹으로 처리 가능
        }
    }

    // 상품을 구매 횟수 리스트에 추가하는 메서드
    private void addProductToTopList(List<TopProduct> topProducts, String productName, String storedFilename, int amount, Long productSeqno) {
        Optional<TopProduct> existingProduct = topProducts.stream()
            .filter(product -> product.getProductName().equals(productName))
            .findFirst();

        if (existingProduct.isPresent()) {
            existingProduct.get().addPurchaseCount(amount);
        } else {
            topProducts.add(new TopProduct(productName, amount, storedFilename, productSeqno)); // productSeqno 추가
        }
    }

    // 상품의 이름과 구매 횟수를 나타내는 클래스
    public static class TopProduct {
        private String productName;
        private int purchaseCount;
        private String storedFilename;  // 추가된 필드
        private Long productSeqno;    // 추가된 필드

        public TopProduct(String productName, int purchaseCount, String storedFilename, Long productSeqno) {
            this.productName = productName;
            this.purchaseCount = purchaseCount;
            this.storedFilename = storedFilename;
            this.productSeqno = productSeqno; // 추가된 필드
        }

        public String getProductName() {
            return productName;
        }

        public int getPurchaseCount() {
            return purchaseCount;
        }

        public String getStoredFilename() {
            return storedFilename;  // 추가된 getter
        }

        public Long getProductSeqno() {
            return productSeqno; // 추가된 getter
        }

        public void addPurchaseCount(int amount) {
            this.purchaseCount += amount;
        }
    }


	// //카테고리별 상품 목록 보기
	// @Override
	// public Page<ProductEntity> categoryProduct(int pageNum, int postNum, Long cateSeqno, String keyword) throws Exception {
	// 	// 정렬 기본 순서를 무엇으로 할지 고민 필요: ex. 판매량, 조회, 가격, 찜 ...
	// 	PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"productSeqno"));
	// 	return productRepository.findByCategory3Seqno_Category3Seqno(cateSeqno, pageRequest);

	// }

  	// 상품 상세 보기
	@Override
	public ProductDTO view(Long productSeqno) throws Exception {
		return productRepository.findById(productSeqno).map(view -> new ProductDTO(view)).get();
	}

	//상품 옵션 보기
	@Override
	public List<ProductOptionDTO> getProductOptions(Long productSeqno) {
    List<ProductOptionEntity> options = productOptionRepository.findByProductSeqno_ProductSeqno(productSeqno);
    return options.stream()
            .map(option -> new ProductOptionDTO(option))
            .collect(Collectors.toList());
	}

	//상품 추가상품 보기
	@Override
	public List<RelatedProductDTO> getRelatedProducts(Long productSeqno) {
		List<RelatedProductEntity> relatedProducts = relatedProductRepository.findByProductSeqno_ProductSeqno(productSeqno);
		return relatedProducts.stream()
				.map(relatedProduct -> new RelatedProductDTO(relatedProduct))
				.collect(Collectors.toList());
	}

	// 상품 이전 보기
	@Override
	public Long pre_seqno(Long productSeqno,String keyword) throws Exception {
		return productRepository.pre_seqno(productSeqno, keyword)==null?0:productRepository.pre_seqno(productSeqno, keyword);	
	}

	// 상품 다음 보기
	@Override
	public Long next_seqno(Long productSeqno,String keyword) throws Exception {
		return productRepository.next_seqno(productSeqno, keyword)==null?0:productRepository.next_seqno(productSeqno, keyword);
	}

	// 상품 첨부파일 목록 보기
	@Override
	public List<ProductFileDTO> fileListView(Long productSeqno) throws Exception {
		//throw new UnsupportedOperationException("Unimplemented method 'fileListView'");
		List<ProductFileDTO> productFileDTOs = new ArrayList<>();
		productFileRepository.findByProductSeqno_ProductSeqno(productSeqno).stream().forEach(list-> productFileDTOs.add(new ProductFileDTO(list)));
		return productFileDTOs;
	}

	// 리뷰 신고
	@Override
	public void report(ReportDTO report) throws Exception {
		report.setReportDate(LocalDateTime.now());
		reportRepository.save(report.dtoToEntity(report));
	}

	
}
