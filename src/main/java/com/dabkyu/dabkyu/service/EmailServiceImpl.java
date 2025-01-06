package com.dabkyu.dabkyu.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dabkyu.dabkyu.dto.EmailDTO;
import com.dabkyu.dabkyu.dto.EmailFileDTO;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.EmailCategoryEntity;
import com.dabkyu.dabkyu.entity.EmailEntity;
import com.dabkyu.dabkyu.entity.EmailFileEntity;
import com.dabkyu.dabkyu.entity.EmailLikeListEntity;
import com.dabkyu.dabkyu.entity.MemberCouponEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.repository.EmailCategoryRepository;
import com.dabkyu.dabkyu.entity.repository.EmailFileRepository;
import com.dabkyu.dabkyu.entity.repository.EmailLikeListRepository;
import com.dabkyu.dabkyu.entity.repository.EmailRepository;
import com.dabkyu.dabkyu.entity.repository.LikeListRepository;
import com.dabkyu.dabkyu.entity.repository.MemberCategoryRepository;
import com.dabkyu.dabkyu.entity.repository.MemberCouponRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.OrderInfoRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;
    private final EmailFileRepository emailFileRepository;
    private final EmailCategoryRepository emailCategoryRepository;
    private final EmailLikeListRepository emailLikeListRepository;
    private final MemberRepository memberRepository;
    private final MemberCategoryRepository memberCategoryRepository;
    private final LikeListRepository likeListRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final MemberCouponRepository memberCouponRepository;

    // 이메일 발송 서비스 구현
    @Override
    public void sendEmail(String title, String content, 
                        MultipartFile[] mailFileList, String kind, 
                        List<Long> category3SeqnoList, List<Long> productSeqnoList, List<Long> couponSeqnoList) {

        System.out.println("이메일 발송 시작");

        // 멤버의 조건
        List<MemberEntity> members;
        if ("c".equals(kind)) {
            // 해당 리스트 중 하나라도 관심목록에 넣은 멤버들
            System.out.println("카테고리멤버 조회");
            members = memberCategoryRepository.findMembersByCategory3SeqnoList(category3SeqnoList);
        } else if ("t".equals(kind)) {
            // 해당 상품 중 하나라도 찜목록에 넣은 멤버들
            members = likeListRepository.findMembersByProductSeqnoList(productSeqnoList);
        } else if ("r".equals(kind)) {
            // 구매한 상품 중 구매한지 2년된 상품이 하나라도 있는 멤버들
            LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2);
            members = orderInfoRepository.findMembersWithEmailReceptAndOrderDateOver2Years(twoYearsAgo);
        } else if ("o".equals(kind)) {
            // 최근 1년내의 구매이력이 없는 멤버들
            LocalDateTime oneYearsAgo = LocalDateTime.now().minusYears(1);
            members = orderInfoRepository.findMembersWithNoOrderInLastYear(oneYearsAgo);
        } else {

            // 모든 멤버
            members = memberRepository.findByEmailRecept();
        }

        log.info("이메일 발송 대상 선정 완료: ({})", members.size());

        // 이메일 발송
        for (MemberEntity member : members) {
            System.out.println(member.getUsername());
            LocalDateTime receptDate = member.getEmailReceptDate() != null ? member.getEmailReceptDate() : LocalDateTime.now().minusYears(1);
   
            if ("Y".equals(member.getEmailRecept()) && receptDate.isBefore(LocalDateTime.now().minusWeeks(2))) {

                sendMailToMember(member, title, content, mailFileList, couponSeqnoList);
            
                member.setEmailReceptDate(LocalDateTime.now());
                // 변경된 엔티티를 저장
                memberRepository.save(member);
    
                } else {
                    System.err.println("이메일 발송 실패: " + member.getEmail());
                }
            }
            saveEmail(title, content);

        }

        // 이메일 발송 메서드 (통합)
        private void sendMailToMember(MemberEntity member, String title, String content, 
                                    MultipartFile[] mailFileList, List<Long> couponSeqnoList
                                    ) {
            MimeMessage message = emailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom("nayoungtest99@gmail.com");
                helper.setTo(member.getEmail());
                helper.setSubject(title);

               
                // 이메일 본문 내용 설정
                StringBuilder emailContent = new StringBuilder(content);

                // 쿠폰 정보 추가
                if (couponSeqnoList != null && !couponSeqnoList.isEmpty()) {
                    StringBuilder couponInfo = new StringBuilder("\n\n[쿠폰 정보]");
                    List<MemberCouponEntity> memberCoupons = memberCouponRepository.findByCouponSeqnoListAndEmail(couponSeqnoList, member.getEmail());
                        for (MemberCouponEntity memberCoupon : memberCoupons) {
                            String couponName = memberCoupon.getCouponSeqno().getCouponName();
                            // 날짜 형식 지정
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
                            // 시작일자와 종료일자를 형식에 맞게 변환
                            String couponStartDate = memberCoupon.getCouponSeqno().getCouponStartDate().format(formatter);
                            String couponEndDate = memberCoupon.getCouponSeqno().getCouponEndDate().format(formatter);
                            int minOrder = memberCoupon.getCouponSeqno().getMinOrder();

                            couponInfo.append("\n- 쿠폰 이름: ").append(couponName)
                                                                    .append("\n  쿠폰 시작일자: ").append(couponStartDate)
                                                                    .append("\n  쿠폰 종료일자: ").append(couponEndDate)
                                                                    .append("\n  최소 주문 금액 ").append(minOrder).append("원 이상 주문시");
            
                            // PERCENT_DISCOUNT 값이 있는 경우 추가
                            if (memberCoupon.getCouponSeqno().getPercentDiscount() != null && memberCoupon.getCouponSeqno().getAmountDiscount() == null) {
                                couponInfo.append("\n  ").append(memberCoupon.getCouponSeqno().getPercentDiscount()).append("% 할인");
                            }

                            // AMOUNT_DISCOUNT 값이 있는 경우 추가
                            if (memberCoupon.getCouponSeqno().getAmountDiscount() != null && memberCoupon.getCouponSeqno().getPercentDiscount() == null) {
                                couponInfo.append("\n  ").append(memberCoupon.getCouponSeqno().getAmountDiscount()).append("원 할인");
                            }

                            // PERCENT_DISCOUNT와 AMOUNT_DISCOUNT 값이 모두 있는 경우 추가
                            if (memberCoupon.getCouponSeqno().getAmountDiscount() != null && memberCoupon.getCouponSeqno().getPercentDiscount() != null) {
                                couponInfo.append("\n  ").append(memberCoupon.getCouponSeqno().getPercentDiscount()).append("% 할인");
                                couponInfo.append(" (최대 ").append(memberCoupon.getCouponSeqno().getAmountDiscount()).append("원)");
                            }       
                        }
                    emailContent.append(couponInfo);
                }

                // 이메일 본문 설정 (HTML 형식으로 처리)
                helper.setText(emailContent.toString(), true);

                //첨부파일이 있으면 첨부파일 메일 첨부
                if (mailFileList != null && mailFileList.length > 0) {
                    for (MultipartFile mailFile : mailFileList) {
                        try {
                            helper.addAttachment(mailFile.getOriginalFilename(), new ByteArrayResource(mailFile.getBytes()));
                            System.out.println("File attached: " + mailFile.getOriginalFilename());
                        } catch (IOException e) {
                            System.err.println("Failed to read file: " + mailFile.getOriginalFilename());
                        } catch (MessagingException e) {
                            System.err.println("Failed to attach file: " + mailFile.getOriginalFilename());
                            throw e;
                        }
                    }
                }

                emailSender.send(message);
                System.out.println("Email sent successfully to: " + member.getEmail());
            } catch (MessagingException e) {
                e.printStackTrace();
                // 실패 시 로그 추가
                System.err.println("Failed to send email to: " + member.getEmail());
            }
        }

    // 이메일 저장
    private void saveEmail(String title, String content) {
        EmailEntity emailEntity = EmailEntity.builder()
                                             .emailTitle(title)
                                             .emailContent(content)
                                             .emailSendDate(LocalDateTime.now())
                                             .build();
        emailEntity = emailRepository.save(emailEntity);
        System.out.println("이메일 저장");
    }

    // maxSeqno 찾기
    @Override
    public Long getMaxSeqno() {
        return emailRepository.findMaxSeqno();
    }

    // 관심 카테고리 저장
    @Override
    public void saveEmailCategory(Long maxSeqno, List<Long> category3SeqnoList) {
        EmailEntity emailEntity = emailRepository.findById(maxSeqno).get();
        if (category3SeqnoList != null) {
            for (Long categorySeqno : category3SeqnoList) {
                EmailCategoryEntity emailCategoryEntity = EmailCategoryEntity.builder()
                                                                             .emailSeqno(emailEntity)
                                                                             .category3Seqno(Category3Entity.builder().category3Seqno(categorySeqno).build())
                                                                             .build();
                emailCategoryRepository.save(emailCategoryEntity);
            }
        }
    }

    // 찜상품 저장
    @Override
    public void saveEmailLike(Long maxSeqno, List<Long> productSeqnoList) {
        EmailEntity emailEntity = emailRepository.findById(maxSeqno).get();
        if (productSeqnoList != null) {
            for (Long productSeqno : productSeqnoList) {
                EmailLikeListEntity emailLikeListEntity = EmailLikeListEntity.builder()
                                                                             .emailSeqno(emailEntity)
                                                                             .productSeqno(ProductEntity.builder().productSeqno(productSeqno).build())
                                                                             .build();
                emailLikeListRepository.save(emailLikeListEntity);
            }
        }
    }

    // 파일 저장
    @Override
    public void saveEmailFile(Long maxSeqno, MultipartFile[] mailFileList) {
        EmailEntity emailEntity = emailRepository.findById(maxSeqno).get();
        if (mailFileList != null && mailFileList.length > 0) {
            for (MultipartFile mailFile : mailFileList) {
                String originalFilename = mailFile.getOriginalFilename();
                String orgFileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String storedFilename = UUID.randomUUID().toString().replaceAll("-", "") + orgFileExtension;
                EmailFileEntity emailFileEntity = EmailFileEntity.builder()
                                                                 .orgFilename(originalFilename)
                                                                 .storedFilename(storedFilename)
                                                                 .build();
                emailFileEntity.setEmailSeqno(emailEntity);
                emailFileRepository.save(emailFileEntity);
            }
        }
    }

    // 메일 발송 내역 조회
    @Override
    public Page<EmailEntity> list(int pageNum, int postNum, String keyword) throws Exception {
        //페이징 기준을 설정 --> 시작점, 증가분, 정렬 방식
        // (시작페이지 --> 0부터 시작, 한 화면에 보이는 행의 수, 정렬기준(Sort.by)
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"emailSeqno"));
        return emailRepository.findByEmailTitleContainingOrEmailContentContaining(keyword, keyword, pageRequest);
    }

    //메일 내용 상세 보기
    @Override
    public EmailDTO view(Long emailSeqno) throws Exception {
        return emailRepository.findById(emailSeqno).map(view -> new EmailDTO(view)).get();
    }

    //메일 내용 이전 보기
    @Override
    public Long pre_seqno(Long emailSeqno,String keyword) throws Exception {
        return emailRepository.pre_seqno(emailSeqno, keyword, keyword)==null?0:emailRepository.pre_seqno(emailSeqno, keyword, keyword);	
    }

    //메일 내용 다음 보기
    @Override
    public Long next_seqno(Long emailSeqno,String keyword) throws Exception {
        return emailRepository.next_seqno(emailSeqno, keyword, keyword)==null?0:emailRepository.next_seqno(emailSeqno, keyword, keyword);
    }

    //첨부파일 목록 보기
    @Override
    public List<EmailFileDTO> fileListView(Long emailSeqno) throws Exception {
        List<EmailFileDTO> fileDTOs = new ArrayList<>();

        emailFileRepository.findByEmailSeqno(emailRepository.findById(emailSeqno).get()).stream().forEach(list-> fileDTOs.add(new EmailFileDTO(list)));
        return fileDTOs;
    }

    //회원가입 인증코드 메일 발송
    @Override
    public void sendAuthCode(String email, String authCode) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[다비켜] 이메일 인증번호 발송");
        message.setText("안녕하세요.\n\n인증번호는 다음과 같습니다:\n\n" 
                        + authCode + "\n\n감사합니다.");

        // 이메일 발송
        try {
            emailSender.send(message);
            log.info("인증번호 전송 성공: " + email);
        } catch (Exception e) {
            log.info("인증번호 전송 실패: " + e.getMessage());
        }
    }

    //임시 비밀번호 메일 발송
    @Override
    public void sendTempPw(String email, String tempPW) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[다비켜] 임시 비밀번호 발송");
        message.setText("안녕하세요.\n\n 임시 비밀번호는 다음과 같습니다:\n\n" 
        + tempPW + "\n\n감사합니다.");

        // 이메일 발송
        try {
            emailSender.send(message);
            log.info("인증번호 전송 성공: " + email);
        } catch (Exception e) {
            log.info("인증번호 전송 실패: " + e.getMessage());
        }

    }

}