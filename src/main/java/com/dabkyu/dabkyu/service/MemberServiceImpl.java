package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.entity.AddressEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.QuestionFileEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;
import com.dabkyu.dabkyu.entity.repository.AddressRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.QuestionFileRepository;
import com.dabkyu.dabkyu.entity.repository.QuestionRepository;
import com.dabkyu.dabkyu.entity.repository.ReviewFileRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {
    
    private final MemberRepository memberRepository;
	private final AddressRepository addressRepository;
    private final QuestionRepository questionRepository;
	private final QuestionFileRepository questionFileRepository;
	private final ReviewFileRepository reviewFileRepository;
    
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
                                                .fromSocial("N")
                                                .pwcheck(0)
                                                .point(0)
                                                .role("USER")
                                                .notificationYn(member.getNotificationYn())
                                                .emailRecept(member.getEmailRecept())
                                                .emailReceptDate(LocalDateTime.parse("2000-01-01, 00:00:00", formatter))
                                                .gender(member.getGender())
                                                .birthday(member.getBirthday())
                                                .build();
        memberRepository.save(memberEntity);
    }

    // 회원정보수정
    @Override
    public void modifyMemberInfo(MemberDTO member) {
        MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
        memberEntity.modifyMemberInfo(member);
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
        memberRepository.modifyPasswordAfter30(email);
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

    // 주소 검색
    @Override
    public Page<AddressEntity> addrSearch(int pageNum, int postNum, String addrSearch) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addrSearch'");
    }

    // 회원 정보
    @Override
    public MemberDTO memberInfo(String email) {
       return memberRepository.findById(email)
                              .map((member) -> new MemberDTO(member))
                              .get();
    }

    // 로그인, 로그아웃, 패스워드 변경 시간 업데이트
    @Override
    public void lastdateUpdate(String email, String status) {
        
        MemberEntity memberEntity = memberRepository.findById(email).get();

        switch (status) {
            case "login":
                memberEntity.setLastloginDate(LocalDateTime.now());
                break;
            case "logout":
                memberEntity.setLastlogoutDate(LocalDateTime.now());
            case "password":
                memberEntity.setLastpwDate(LocalDateTime.now());
        }

        memberRepository.save(memberEntity);
    }

    // 회원이 등록한 문의 첨부파일 삭제
    @Override
    public List<QuestionFileEntity> getStoredQuestionFilenameList(String email) {

        return questionFileRepository.findByQueSeqno()
    }

    // 회원이 등록한 리뷰 첨부파일 삭제
    @Override
    public List<ReviewFileEntity> getStoredReviewFilenameList(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStoredReviewFilenameList'");
    }

    // 회원정보 삭제
    @Override
    public void deleteID(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteID'");
    }

}
