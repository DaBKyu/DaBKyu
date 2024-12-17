package com.dabkyu.dabkyu.entity.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.dto.MemberSalesDTO;
import com.dabkyu.dabkyu.dto.SalesByMemberGradeDTO;
import com.dabkyu.dabkyu.dto.SignupGenderStatDTO;
import com.dabkyu.dabkyu.entity.MemberEntity;

import jakarta.transaction.Transactional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    public Optional<MemberEntity> findByUsernameAndTelno(String username, String telno);

    public MemberEntity findByAuthkey(String authkey);

    Page<MemberEntity> findByMemberGrade(String memberGrade, Pageable pageable); // 회원등급으로 검색


    // 패스워드 변경 30일 이후로 연기
//     @Transactional
//     @Modifying
//     @Query(value="update member set pwcheck = "
//                 + "(select nvl(pwcheck,0) from member "
//                 + "where email = :email) + 1 where email =:email",
//                 nativeQuery = true)
//     public void modifyPasswordAfter30(@Param("email") String email);

    //회원 등급/이름으로 검색
    public Page<MemberEntity> findByMemberGradeOrUsernameContaining
                (String keyword1,String keyword2,Pageable pageable);
    
    public Page<MemberEntity> findByEmailContainingOrUsernameContaining(
        String keyword1,String keyword2,Pageable pageable
    );

    //이메일 수신 동의 멤버 리스트
    @Query(value="select m from member m where m.emailRecept = 'Y'")
    public List<MemberEntity> findByEmailRecept();

    //회원별 매출액 통계
    @Query("SELECT new com.dabkyu.dabkyu.dto.MemberSalesDTO(m.username, SUM(m.totalPvalue)) " +
            "FROM member m " + 
            "GROUP BY m.username " +
            "ORDER BY SUM(m.totalPvalue) DESC")
    public List<MemberSalesDTO> findAllMemberSales();

    //연령별 매출액 통계
    @Query(value = "SELECT CONCAT(FLOOR((SYSDATE - TRUNC(m.birth_date)) / 365 / 10) * 10, '대'), " +
               "SUM(m.total_pvalue) " +
               "FROM member m " +
               "GROUP BY FLOOR((SYSDATE - TRUNC(m.birth_date)) / 365 / 10) * 10 " +
               "ORDER BY FLOOR((SYSDATE - TRUNC(m.birth_date)) / 365 / 10) * 10 ASC", nativeQuery = true)
    public List<Object[]> findSalesByAgeGroup();


    //등급별 매출액 통계
    @Query("SELECT new com.dabkyu.dabkyu.dto.SalesByMemberGradeDTO(m.memberGrade, SUM(m.totalPvalue)) " +
            "FROM member m " + 
            "GROUP BY m.memberGrade " +
            "ORDER BY SUM(m.totalPvalue) DESC")
    public List<SalesByMemberGradeDTO> findSalesByGrade();


    //가입일 기준 가입 통계
    @Query(value = "SELECT TRUNC(m.regdate) AS regdate, COUNT(*) AS signupCount " +
               "FROM member m " +
               "WHERE m.regdate BETWEEN TO_TIMESTAMP(:startDate, 'YYYY-MM-DD HH24:MI:SS') " +
               "AND TO_TIMESTAMP(:endDate, 'YYYY-MM-DD HH24:MI:SS') " +
               "GROUP BY TRUNC(m.regdate) " +
               "ORDER BY TRUNC(m.regdate)", 
       nativeQuery = true)
    public List<Object[]> findSignupDateStat(
        @Param("startDate") String startDate, 
        @Param("endDate") String endDate);

    //성별 기준 가입 통계
    @Query("SELECT new com.dabkyu.dabkyu.dto.SignupGenderStatDTO(m.gender, COUNT(m)) " +
    "FROM member m " +
    "GROUP BY m.gender")
    public List<SignupGenderStatDTO> findSignupGenderStat();

    
    //연령대 기준 가입 통계 
    @Query(value = "SELECT FLOOR(MONTHS_BETWEEN(CURRENT_DATE, member.birth_date) / 12 / 10) * 10 || '대' AS ageGroup, " +
               "COUNT(*) AS memberCount " +
               "FROM member " +
               "GROUP BY FLOOR(MONTHS_BETWEEN(CURRENT_DATE, member.birth_date) / 12 / 10) * 10 " +
               "ORDER BY FLOOR(MONTHS_BETWEEN(CURRENT_DATE, member.birth_date) / 12 / 10) * 10", 
       nativeQuery = true)
    public List<Object[]> findSignupAgeStat();

    //로그인한 회원의 연령대 
    @Query(value = "SELECT FLOOR((EXTRACT(YEAR FROM SYSDATE) - EXTRACT(YEAR FROM :memberBirthDate)) / 10) * 10 AS ageGroup " +
               "FROM dual",
       nativeQuery = true)
    int findAgeGroup(@Param("memberBirthDate") LocalDate memberBirthDate);


}
