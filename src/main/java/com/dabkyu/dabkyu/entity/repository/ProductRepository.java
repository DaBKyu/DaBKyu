package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	//public Page<ProductEntity> findByCategory1SeqnoAndProductNameContaining(Long category1Seqno, String productName, Pageable pageable);
	// 대분류 카테고리 필터링
	// Category1Seqno를 통해 ProductEntity 필터링
    @Query("SELECT p FROM product p " +
           "JOIN p.category3Seqno c3 " + // Category3Entity와 조인
           "JOIN c3.category2Seqno c2 " + // Category2Entity와 조인
           "JOIN c2.category1Seqno c1 " + // Category1Entity와 조인
           "WHERE c1.category1Seqno = :category1Seqno " + // Category1Seqno로 필터링
           "AND p.productName LIKE %:productName%") // productName 필터링
    Page<ProductEntity> findByCategory1SeqnoAndProductNameContaining(
            @Param("category1Seqno") Long category1Seqno, 
            @Param("productName") String productName, 
            Pageable pageable);

	//관리자 페이지 상품 리스트(상품코드, 상품명)
	//public Page<ProductEntity> findByProductSeqnoOrProductNameContaining
	//	(Long seqno,String keyword,Pageable pageable);
	public ProductEntity findByProductSeqno(Long productSeqno);

	//public Page<ProductEntity> findByCategory2SeqnoAndProductNameContaining(Long category2Seqno, String productName, Pageable pageable);
	// 중분류 카테고리 필터링
	@Query("SELECT p FROM product p " +
			"JOIN p.category3Seqno c3 " + // Category3Entity와 조인
			"JOIN c3.category2Seqno c2 " + // Category2Entity와 조인
			"WHERE c2.category2Seqno = :category2Seqno " + // Category2Seqno로 필터링
			"AND p.productName LIKE %:productName%") // productName 필터링
	Page<ProductEntity> findByCategory2SeqnoAndProductNameContaining(
			@Param("category2Seqno") Long category2Seqno, 
			@Param("productName") String productName, 
			Pageable pageable);

	// 소분류 카테고리 필터링
    public Page<ProductEntity> findByCategory3Seqno_Category3SeqnoAndProductNameContaining(Long category3Seqno, String productName, Pageable pageable);

	//전체 상품 목록 보기
    public Page<ProductEntity> findByProductNameContaining(String productName, Pageable pageable);
 
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


	//관리자페이지 상품리스트
	@Query("SELECT p FROM ProductEntity p " +
           "JOIN p.category3Seqno c3 " +
           "JOIN c3.category2Seqno c2 " +
           "JOIN c2.category1Seqno c1 " +
           "WHERE c1.category1Seqno = :category1Seqno " +
           "AND c2.category2Seqno = :category2Seqno " +
           "AND c3.category3Seqno = :category3Seqno " +
           "AND p.productName LIKE %:productName%")
    Page<ProductEntity> findByAllCategories(
            @Param("category1Seqno") Long category1Seqno,
            @Param("category2Seqno") Long category2Seqno,
            @Param("category3Seqno") Long category3Seqno,
            @Param("productName") String productName,
            Pageable pageable);

}
