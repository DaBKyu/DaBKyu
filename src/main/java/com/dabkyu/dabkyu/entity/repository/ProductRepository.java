package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	////////////////////내가만든거///////////////////
	//searchAll에서 사용할 전체 상품 목록 보기
	public List<ProductEntity> findByProductNameContaining(String productName);
	///////////////////////////////////////////////

	// 대분류 카테고리 필터링
	//public Page<ProductEntity> findByCategory1SeqnoAndProductNameContaining(Long category1Seqno, String productName, Pageable pageable);

	//관리자 페이지 상품 리스트(상품코드, 상품명)
	public Page<ProductEntity> findByProductSeqnoOrProductNameContaining
		(Long seqno,String keyword,Pageable pageable);

	// 중분류 카테고리 필터링
	//public Page<ProductEntity> findByCategory2SeqnoAndProductNameContaining(Long category2Seqno, String productName, Pageable pageable);

	// 소분류 카테고리 필터링
    public Page<ProductEntity> findByCategory3Seqno_Category3SeqnoAndProductNameContaining(Long category3Seqno, String productName, Pageable pageable);

	//전체 상품 목록 보기
    public Page<ProductEntity> findByProductNameContaining(String productName, Pageable pageable);

	//카테고리별 상품 목록 보기
	public Page<ProductEntity> findByCategory3Seqno_Category3Seqno(Long cateSeqno, Pageable pageable);

	
    //상품 이전 보기
	@Query("select max(p.productSeqno) from product p " 
			+ "where p.productSeqno < :productSeqno and "
			+ "(p.productName like %:keyword%)")
	public Long pre_seqno(@Param("productSeqno") Long productSeqno, 
			@Param("keyword") String keyword);
	
    //상품 다음 보기
	@Query("select min(p.productSeqno) from product p "
			+ "where p.productSeqno < :productSeqno and "
			+ "(p.productName like %:keyword%)")
	public Long next_seqno(@Param("productSeqno") Long productSeqno, 
			@Param("keyword") String keyword);

	//관리자페이지 상품리스트 //카테고리, 상품명 둘 중 하나만 입력되어도 검색 가능하도록 null값 입력될 시 무시
	@Query("SELECT p FROM product p " +
           "JOIN p.category3Seqno c3 " +
           "JOIN c3.category2Seqno c2 " +
           "JOIN c2.category1Seqno c1 " +
		   "WHERE (:category1Seqno IS NULL OR c1.category1Seqno = :category1Seqno) " +
		   "AND (:category2Seqno IS NULL OR c2.category2Seqno = :category2Seqno) " +
		   "AND (:category3Seqno IS NULL OR c3.category3Seqno = :category3Seqno) " +
		   "AND (:productName = '' OR p.productName LIKE %:productName%)")
    public Page<ProductEntity> findByAllCategories(
            @Param("category1Seqno") Long category1Seqno,
            @Param("category2Seqno") Long category2Seqno,
            @Param("category3Seqno") Long category3Seqno,
            @Param("productName") String productName,
            Pageable pageable);

	public List<ProductEntity> findByCategory3Seqno_Category3Seqno(Long category3Seqno);
}
