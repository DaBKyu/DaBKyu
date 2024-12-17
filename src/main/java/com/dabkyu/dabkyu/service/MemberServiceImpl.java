package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.MemberAddressDTO;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.OrderDetailDTO;
import com.dabkyu.dabkyu.dto.OrderInfoDTO;
import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberAddressEntity;
import com.dabkyu.dabkyu.entity.MemberCategoryEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.MemberLogEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.QuestionFileEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;
import com.dabkyu.dabkyu.entity.repository.LikeListRepository;
import com.dabkyu.dabkyu.entity.repository.MemberAddressRepository;
import com.dabkyu.dabkyu.entity.repository.MemberCategoryRepository;
import com.dabkyu.dabkyu.entity.repository.MemberCouponRepository;
import com.dabkyu.dabkyu.entity.repository.MemberLogRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.OrderDetailRepository;
import com.dabkyu.dabkyu.entity.repository.OrderInfoRepository;
import com.dabkyu.dabkyu.entity.repository.ProductFileRepository;
import com.dabkyu.dabkyu.entity.repository.QuestionFileRepository;
import com.dabkyu.dabkyu.entity.repository.QuestionRepository;
import com.dabkyu.dabkyu.entity.repository.ReviewFileRepository;
import com.dabkyu.dabkyu.entity.repository.ReviewRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {
    
    private final MemberRepository memberRepository;
    private final MemberAddressRepository memberAddressRepository;
	private final QuestionFileRepository questionFileRepository;
	private final ReviewFileRepository reviewFileRepository;
    private final MemberCategoryRepository memberCategoryRepository;
    private final LikeListRepository likeListRepository;
    private final ReviewRepository reviewRepository;
    private final QuestionRepository questionRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final MemberLogRepository memberLogRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductFileRepository productFileRepository;
    
    // 회원가입
    @Override
    public void signup(MemberDTO member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss");
        MemberEntity memberEntity = MemberEntity.builder()
                                                                                  .email(member.getEmail())
                                                                                  .password(member.getPassword())
                                                                                  .telno(member.getTelno())
                                                                                  .username(member.getUsername())
                                                                                  .memberGrade("BRONZE")
                                                                                  .regdate(LocalDateTime.now())
                                                                                  .lastpwDate(LocalDateTime.now())
                                                                                  .lastpwcheckDate(LocalDateTime.now())
                                                                                  .fromSocial("N")
                                                                                  .point(0)
                                                                                  .role("USER")
                                                                                  .notificationYn(member.getNotificationYn())
                                                                                  .emailRecept(member.getEmailRecept())
                                                                                  .emailReceptDate(LocalDateTime.parse("2000-01-01, 00:00:00", formatter))
                                                                                  .gender(member.getGender())
                                                                                  .birthDate(member.getBirthDate())
                                                                                  .build();
        memberRepository.save(memberEntity);
    }

    // 회원정보수정
    @Override
    public void modifyMemberInfo(MemberDTO member) {
        MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
        memberEntity.modifyMemberInfo(member);
    }

    // 회원 주문제품 내역
    @Override
	public Page<OrderDetailEntity> orderDetailList(String email, int page, int orderNum, String keyword) {
        PageRequest pageRequest = PageRequest.of(page -1, orderNum, Sort.by(Direction.DESC, "orderProductSeqno"));
        List<OrderDetailEntity> orderDetailEntities = new ArrayList<>();

        List<OrderInfoEntity> orderInfoEntities = orderInfoRepository.findByEmail_Email(email);
        for (OrderInfoEntity orderInfoEntity : orderInfoEntities) {
            List<OrderDetailEntity> details = orderDetailRepository.findByOrderSeqno(orderInfoEntity);
            orderDetailEntities.addAll(details);
        }

        // 키워드 필터링 (Optional)
        if (keyword != null && !keyword.isEmpty()) {
            orderDetailEntities = orderDetailEntities.stream()
                                                                                    .filter(orderDetail  -> orderDetail .getOrderProductSeqno().getProductSeqno().getProductName().contains(keyword))
                                                                                    .toList();
        }

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), orderDetailEntities.size());

        if (start > end) {
            return new PageImpl<>(Collections.emptyList(), pageRequest, orderDetailEntities.size());
        }

        List<OrderDetailEntity> pagedOrderDetails  = orderDetailEntities.subList(start, end);

        return new PageImpl<>(pagedOrderDetails , pageRequest, orderDetailEntities.size());
    }

    // NEW 주문정보리스트
    @Override
    public Page<OrderInfoDTO> getOrderInfoList(String email, String keyword, Pageable pageable) {

        log.info("-----------------------OrderInfo 조회------------------------");
        // OrderInfo 조회
        Page<OrderInfoEntity> orderInfoEntities = orderInfoRepository.findByEmail_Email(email, pageable);

        // 각 OrderInfo에 해당하는 OrderDetail 조회 후 DTO로 변환
        log.info("-----------------------DTO로 변환------------------------");
        return orderInfoEntities.map(orderInfo -> {
            if (keyword != null && !keyword.isEmpty()) {
                log.info("-----------------------키워드 필터 후 변환------------------------");
                List<OrderDetailDTO> orderDetails = orderDetailRepository.findByOrderSeqno(orderInfo)
                                                                                                                .stream()
                                                                                                                .filter(orderDetail  -> orderDetail .getOrderProductSeqno().getProductSeqno().getProductName().contains(keyword))
                                                                                                                .map(orderDetail -> new OrderDetailDTO(orderDetail))
                                                                                                                .toList();
                
                log.info("----------------------- DTO 매칭 ------------------------");
                // OrderInfoDTO에 매칭
                return OrderInfoDTO.matchOrderDetailDTO(orderInfo, orderDetails);
            }
            log.info("-----------------------키워드 필터 없이 DTO로 변환------------------------");
            List<OrderDetailDTO> orderDetails = orderDetailRepository.findByOrderSeqno(orderInfo)
                                                                                                            .stream()
                                                                                                            .map(orderDetail -> new OrderDetailDTO(orderDetail))
                                                                                                            .peek(dto -> dto.setProduct(
                                                                                                                new ProductDTO(
                                                                                                                    dto.getOrderProductSeqno().getProductSeqno(),
                                                                                                                    productFileRepository.findFirstByProductSeqnoAndIsThumb(dto.getOrderProductSeqno().getProductSeqno(), "Y")
                                                                                                                )))
                                                                                                            .toList();
            
            log.info("----------------------- DTO 매칭 ------------------------");
            // OrderInfoDTO에 매칭
            return OrderInfoDTO.matchOrderDetailDTO(orderInfo, orderDetails);
        });
    }

    // 배송지 목록 조회
    @Override
	public List<MemberAddressEntity> addressList(String email) {
        return memberAddressRepository.findByEmail_Email(email);
    }
    
	/// 특정 배송지 조회
    @Override
	public MemberAddressDTO viewAddr(Long memberAddressSeqno) {
        MemberAddressEntity memberAddr = memberAddressRepository.findById(memberAddressSeqno).get();
        return new MemberAddressDTO(memberAddr);
    }

	// 기본배송지 조회
    @Override
    public MemberAddressDTO viewBasicAddr(String email) {
        String isBasic = "Y";
        MemberAddressEntity memberAddressEntity = memberAddressRepository.findByEmail_EmailAndIsBasic(email, isBasic);
        if (memberAddressEntity == null) {
            return null; 
        }
        return new MemberAddressDTO(memberAddressEntity);
    }


	// 배송지 등록
    @Override
	public void addAddress(MemberAddressDTO address) {
        // isBasic = Y로 들어왔을 때.
        if (address.getIsBasic().equals("Y")) {
            // 이미 기본 배송지가 있는지 확인
            MemberAddressEntity memberaddr = memberAddressRepository.findFirstByEmailAndIsBasic(address.getEmail(), "Y");
            if (memberaddr != null) {
                memberaddr.setIsBasic("N");
                memberAddressRepository.save(memberaddr);
            }
        }
        memberAddressRepository.save(address.dtoToEntity(address));
    }

	// 배송지 수정
    @Override
	public void modifyAddress(MemberAddressDTO address) {
        MemberAddressEntity memberAddressEntity = memberAddressRepository.findById(address.getMemberAddressSeqno()).get();
        memberAddressEntity.setAddrName(address.getAddrName());
        memberAddressEntity.setAddress(address.getAddress());
        memberAddressEntity.setZipcode(address.getZipcode());
        memberAddressEntity.setDetailAddr(address.getDetailAddr());
        memberAddressEntity.setRequest(address.getRequest());
        memberAddressEntity.setReceiverName(address.getReceiverName());
        memberAddressEntity.setReceiverTelno(address.getReceiverTelno());
        // memberAddressEntity.setIsBasic(address.getIsBasic());
        memberAddressRepository.save(memberAddressEntity);
    }

    // 배송지 삭제
    @Override
	public void deleteAddress(Long seqno) {
        MemberAddressEntity memberAddressEntity = memberAddressRepository.findById(seqno).get();
        memberAddressRepository.delete(memberAddressEntity);
    }

    // 내 관심 카테고리 조회
    @Override
	public List<Category3Entity> myCategoryList(String email) {
        
        List<MemberCategoryEntity> myCategories = memberCategoryRepository.findByEmail_Email(email);

        return myCategories.stream()
                                         .map(myCategory -> myCategory.getCategory3Seqno())
                                         .collect(Collectors.toList());
        
    }
    
	// 내 찜한 상품 조회
    @Override
	public Page<ProductEntity> myLikeList(String email, int page, int productNum) {
        PageRequest pageRequest = PageRequest.of(page -1, productNum, Sort.by(Direction.DESC, "likeDate"));
        MemberEntity member = memberRepository.findById(email).get();

        return likeListRepository.findLikedProductByEmail(member, pageRequest);
    }

	// 내 리뷰 조회
    @Override
	public Page<ReviewEntity> myReviewList(String email, int page, int reviewNum) {
        PageRequest pageRequest = PageRequest.of(page -1, reviewNum, Sort.by(Direction.DESC, "revDate"));
        MemberEntity member = memberRepository.findById(email).get();

        return reviewRepository.findByEmail(member, pageRequest);
    }

    // 내 문의 조회
    @Override
	public Page<QuestionEntity> myQuestionList(String email, int page, int questionNum) {
        PageRequest pageRequest = PageRequest.of(page -1, questionNum, Sort.by(Direction.DESC, "queDate"));
        MemberEntity member = memberRepository.findById(email).get();

        return questionRepository.findByEmail(member, pageRequest);
    }
    
	// 내 쿠폰 조회
    @Override
	public Page<CouponEntity> myCouponList(String email, int page, int couponNum) {
        PageRequest pageRequest = PageRequest.of(page -1, couponNum, Sort.by(Direction.ASC, "couponEndDate"));
        MemberEntity member = memberRepository.findById(email).get();

        return memberCouponRepository.findMyCouponByEmail(member, pageRequest);
    }

    // 회원비밀번호수정
    @Override
    public void modifyMemberPassword(MemberDTO member) {
        MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
        memberEntity.setPassword(member.getPassword());
        memberRepository.save(memberEntity);
    }

    // 30일이후 비밀번호 변경 연기
    @Override
    public void modifyPasswordAfter30(String email) {
        // memberRepository.modifyPasswordAfter30(email);
        MemberEntity memberEntity = memberRepository.findById(email).get();
        memberEntity.setLastpwcheckDate(LocalDateTime.now());
    }

    // 아이디 찾기
    @Override
    public String searchID(MemberDTO member) {
        return memberRepository.findByUsernameAndTelno(member.getUsername(), member.getTelno())
                               .map(m -> m.getEmail()).orElse("ID_NOT_FOUND");
    }

    // 아이디 중복 체크
    @Override
    public int idCheck(String email) {
        return memberRepository.findById(email).isEmpty()?0:1;
    }

    // 회원 정보
    @Override
    public MemberDTO memberInfo(String email) {
       return memberRepository.findById(email)
                                                .map((member) -> new MemberDTO(member))
                                                .get();
    }

    // authkey 회원 정보
    @Override
	public MemberEntity memberAuthkey(String authkey) {
        return memberRepository.findByAuthkey(authkey);
    }

    // authkey 업데이트
    @Override
	public void authkeyUpdate(MemberDTO member) {
        MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
        memberEntity.setAuthkey(member.getAuthkey());
        memberRepository.save(memberEntity);
    }

    // 로그인, 로그아웃, 패스워드 변경 시간 업데이트, 로그인/로그아웃 로그 등록
    @Override
    public void lastdateUpdate(String email, String status) {
        
        MemberEntity memberEntity = memberRepository.findById(email).get();

        if (status == "login" || status == "logout") {
            MemberLogEntity memberLogEntity = MemberLogEntity.builder()
                                                                                                        .email(memberEntity)
                                                                                                        .inouttime(LocalDateTime.now())
                                                                                                        .status(status)
                                                                                                        .build();
            memberLogRepository.save(memberLogEntity);
        }

        switch (status) {
            case "login":
                memberEntity.setLastloginDate(LocalDateTime.now());
                break;
            case "logout":
                memberEntity.setLastlogoutDate(LocalDateTime.now());
                break;
            case "password":
                memberEntity.setLastpwDate(LocalDateTime.now());
        }

        memberRepository.save(memberEntity);
    }

    // 회원이 등록한 문의 첨부파일 삭제
    @Override
    public List<QuestionFileEntity> getStoredQuestionFilenameList(String email) {
        return questionFileRepository.findByEmail(email);
    }

    // 회원이 등록한 리뷰 첨부파일 삭제
    @Override
    public List<ReviewFileEntity> getStoredReviewFilenameList(String email) {
        return reviewFileRepository.findByEmail(email);
    }

    // 회원정보 삭제
    @Override
    public void deleteID(String email) {
        MemberEntity memberEntity = memberRepository.findById(email).get();
        memberRepository.delete(memberEntity);
    }
}
