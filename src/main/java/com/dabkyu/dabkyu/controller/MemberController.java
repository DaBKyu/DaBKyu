package com.dabkyu.dabkyu.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dabkyu.dabkyu.dto.MemberAddressDTO;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberAddressEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.QuestionFileEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;
import com.dabkyu.dabkyu.service.EmailService;
import com.dabkyu.dabkyu.service.MemberService;
import com.dabkyu.dabkyu.util.PageUtil;
import com.dabkyu.dabkyu.util.PasswordMaker;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;




@AllArgsConstructor
@Controller
@Log4j2
public class MemberController {
    
    private final MemberService memberService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder pwdEncoder;

    private final RedisTemplate<String, String> redisTemplate;
    

    // 로그인 화면
    @GetMapping("/member/login")
    public void getLogin() {}
    

    // 로그인(Spring Security)
    @PostMapping("/member/login")
    public void postLogin() {}


    // Spring Security 이전 로그인 처리
    @ResponseBody
    @PostMapping("/member/loginCheck")
    public String postLoginCheck(MemberDTO member) throws Exception {
        
        // 아이디 체크  
        if (memberService.idCheck(member.getEmail()) == 0) {
            return "{\"message\":\"ID_NOT_FOUND\"}";
        }

        // 비밀번호 체크
        if (!pwdEncoder.matches(member.getPassword(), memberService.memberInfo(member.getEmail()).getPassword().toString())) {
            return "{\"message\":\"PASSWORD_NOT_FOUND\"}";
        }

        return "{\"message\":\"good\"}";
    }
    

    // 로그아웃 전처리
    @GetMapping("/member/beforeLogout")
    public String getBeforeLogout(HttpSession session) throws Exception {
        
        String email = (String) session.getAttribute("email");
        memberService.lastdateUpdate(email, "logout");
        session.invalidate();
        return "redirect:/member/logout";
    }


    // 로그아웃(Spring Security)
    @GetMapping("/member/logout")
    public void getLogout(HttpSession session) throws Exception {}


    // 회원가입 화면
    @GetMapping("/member/signup")
    public void getSignup() {}
    

    // 회원가입, 회원 정보 수정
    @ResponseBody
    @PostMapping("/member/signup")
    public String postSignup(
        MemberDTO member,
        @RequestParam("kind") String kind
    ) throws Exception {

        // 회원가입
        if (kind.equals("I")) {
            String birthDate = member.getBirth();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            member.setBirthDate(LocalDate.parse(birthDate, formatter));
            member.setPassword(pwdEncoder.encode(member.getPassword()));
            member.setIsActive("Y");
            memberService.signup(member);
            log.info("회원등록: {\"username\":" + member.getUsername() + "\", \"status\":\"good\"}");
        }

        // 회원정보 수정
        if (kind.equals("U")) {
            memberService.modifyMemberInfo(member);
            log.info("회원정보수정: {\"username\":" + member.getUsername() + "\", \"status\":\"good\"}");
        }

        return "{\"username\":" + URLEncoder.encode(member.getUsername(), "UTF-8") + "\", \"status\":\"good\"}";
    }


    // 이메일 인증번호 발송
    @ResponseBody
    @PostMapping("/member/sendEmail")
    public String postSendEmail(@RequestBody Map<String, String> request) throws Exception {
        
        String email = request.get("email");
        
        // 이메일 중복 체크
        if (memberService.idCheck(email) == 1) {
            return "{\"status\": 1}"; // 0: 성공, 1: 이미 존재하는 이메일, 2: 서버 오류
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder authCodeBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(chars.length());
            authCodeBuilder.append(chars.charAt(index));
        }

        String authCode = authCodeBuilder.toString();
        redisTemplate.opsForValue().set(email, authCode, 5, TimeUnit.MINUTES); // Redis에 저장, 5분 동안 유효

        emailService.sendAuthCode(email, authCode); // 이메일 발송

        return "{\"status\": 0}"; // 0: 성공, 1: 이미 존재하는 이메일, 2: 서버 오류
    }


    // 이메일 인증번호 확인
    @ResponseBody
    @PostMapping("/member/checkAuth")
    public String postCheckAuth(@RequestBody Map<String, String> request) throws Exception {
        
        String email = request.get("email");
        String authNum = request.get("authNum");

        String authCode = redisTemplate.opsForValue().get(email);
        if (!authCode.equals(authNum)) {
            return "{\"status\": 1}"; // 0: 성공, 1: 일치하지 않음, 2: 서버 오류
        }

        return "{\"status\": 0}"; // 0: 성공, 1: 일치하지 않음, 2: 서버 오류
    }
    

    // OAuth2 회원가입
    @PostMapping("/member/oauth2Signup")
    public void postOAuth2Signup() throws Exception {}


    // 마이페이지
    @GetMapping("/mypage/order")
    public void getMypage(
        Model model,
        HttpSession session,
        @RequestParam(name = "keyword",defaultValue = "",required = false)
        String keyword,
        @RequestParam("page")
        int page
    ) throws Exception {

        log.info("-----------------------주문 상세 리스트 조회------------------------");
        String email = (String) session.getAttribute("email");

        int orderNum = 10;   // 한 번에 보여줄 주문 갯수
        int pagelistNum = 10;   // 페이지 리스트 갯수

        PageUtil pageUtil = new PageUtil();
        Page<OrderDetailEntity> orderDetailList = memberService.orderDetailList(email, page, orderNum, keyword);
        log.info("--------------------주문제품 갯수: {}-----------------------", orderDetailList.getSize());
        for (OrderDetailEntity orderDetailEntity : orderDetailList) {
            log.info("---------------------주문제품: {}-------------------------", orderDetailEntity.getOrderProductSeqno().getProductSeqno().getProductName());
            log.info("---------------------주문제품: {}-------------------------", orderDetailEntity.getOrderProductSeqno().getProductSeqno().getPrice());
        }
        int totalCount = (int) orderDetailList.getTotalElements();

        model.addAttribute("orderDetailList", orderDetailList);
        model.addAttribute("listIsEmpty", orderDetailList.hasContent()?"N":"Y");
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("orderNum", orderNum);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", pageUtil.getOrderList(page, orderNum, pagelistNum, totalCount, keyword));
    }
    

    // 취소, 환불, 교환 내역 조회
    @GetMapping("/mypage/cancelRefundChange")
    public void getCancelRefundChange(
        Model model,
        HttpSession session
    ) throws Exception {

        String email = (String) session.getAttribute("email");
        // 취소, 환불 교환 내역 관리 테이블 필요
    }
    

    // 배송지 리스트에서 배송지 수정 화면
    @GetMapping("/mypage/modifyMemberAddress")
    public void getAddress(Model model,
        @RequestParam(name="memberAddressSeqno") Long memberAddressSeqno) throws Exception {
        
        MemberAddressDTO viewAddr = memberService.viewAddr(memberAddressSeqno);
        model.addAttribute("viewAddr", viewAddr);
    }


    // 배송지 리스트 화면
    @GetMapping("/mypage/addrList")
    public void getAddrList(Model model, HttpSession session) throws Exception {

        String email = (String) session.getAttribute("email");

        List<MemberAddressEntity> addressList = memberService.addressList(email);
        MemberAddressDTO viewBasicAddr = memberService.viewBasicAddr(email);

        Collections.sort(addressList, Comparator.comparing(MemberAddressEntity::getMemberAddressSeqno).reversed());

        model.addAttribute("listIsEmpty", addressList.isEmpty()?"Y":"N");
        model.addAttribute("addressList", addressList);
        model.addAttribute("viewBasicAddr", viewBasicAddr);
    }
    
    
    // 배송지 추가 화면
    @GetMapping("/mypage/addAddr")
    public void getAddAdress(Model model, HttpSession session) throws Exception {
        String email = (String) session.getAttribute("email");
        model.addAttribute("member", memberService.memberInfo(email));
    }
    
    
    // 배송지 등록
    @ResponseBody
    @PostMapping("/mypage/addAddress")
    public String postAddAddress(MemberAddressDTO address) throws Exception {
        memberService.addAddress(address);
        return "{\"message\":\"good\"}";
    }
    

    // 배송지 수정
    @ResponseBody
    @PostMapping("/mypage/modifyAddress")
    public String postModifyAddress(MemberAddressDTO address) throws Exception {
        memberService.modifyAddress(address);
        return "{\"message\":\"good\"}";
    }
    

    // 배송지 삭제
    @GetMapping("/mypage/deleteAddress")
    public String getDeleteAddress(@RequestParam("seqno") Long seqno) throws Exception {
        memberService.deleteAddress(seqno);
        return "redirect:/mypage/address";
    }
    

    // 회원 정보 화면
    @GetMapping("/mypage/memberInfo")
    public void getMemberInfo(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        MemberAddressDTO viewBasicAddr = memberService.viewBasicAddr(email);

        model.addAttribute("member", memberService.memberInfo(email));
        model.addAttribute("viewBasicAddr", viewBasicAddr);
    }


    // 회원 정보 수정 화면
    @GetMapping("/mypage/modifyMemberInfo")
    public void getModifyMemberInfo(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        model.addAttribute("member", memberService.memberInfo(email));
    }


    // 관심 카테고리 화면
    @GetMapping("/mypage/myCategories")
    public void getMyCategories(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        model.addAttribute("myCategoryList", memberService.myCategoryList(email));
        model.addAttribute("isEmpty", memberService.myCategoryList(email).isEmpty()?"Y":"N");
    }
    

    // 찜한 상품 화면
    @GetMapping("/mypage/myLikeProducts")
    public void getMyLikeProducts(
        Model model,
        HttpSession session,
        @RequestParam("page")
        int page
    ) {

        String email = (String) session.getAttribute("email");

        int productNum = 10;   // 한 번에 보여줄 제품 갯수
        int pagelistNum = 10;   // 페이지 리스트 갯수

        PageUtil pageUtil = new PageUtil();
        Page<ProductEntity> myLikeList = memberService.myLikeList(email, page, productNum);
        int totalCount = (int) myLikeList.getTotalElements();

        model.addAttribute("myLikeList", myLikeList);
        model.addAttribute("listIsEmpty", myLikeList.hasContent()?"N":"Y");
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("productNum", productNum);
        model.addAttribute("pageList", pageUtil.getPageListNoKeyword(page, productNum, pagelistNum, totalCount));
    }
    
    

    // 내 리뷰 리스트 화면
    @GetMapping("/mypage/myReviews")
    public void getMyReviews(
        Model model,
        HttpSession session,
        @RequestParam("page")
        int page
    ) {
       
        String email = (String) session.getAttribute("email");

        int reviewNum = 10;   // 한 번에 보여줄 리뷰 갯수
        int pagelistNum = 10;   // 페이지 리스트 갯수

        PageUtil pageUtil = new PageUtil();
        Page<ReviewEntity> myReviewList = memberService.myReviewList(email, page, reviewNum);
        int totalCount = (int) myReviewList.getTotalElements();

        model.addAttribute("myReviewList", myReviewList);
        model.addAttribute("listIsEmpty", myReviewList.hasContent()?"N":"Y");
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("reviewNum", reviewNum);
        model.addAttribute("pageList", pageUtil.getPageListNoKeyword(page, reviewNum, pagelistNum, totalCount));

    }
    
    // 내 문의 리스트 화면
    @GetMapping("/mypage/myQuestion")
    public void getMyQuestion(
        Model model,
        HttpSession session,
        @RequestParam("page")
        int page
    ) {
        
        String email = (String) session.getAttribute("email");

        int questionNum = 10;   // 한 번에 보여줄 문의 갯수
        int pagelistNum = 10;   // 페이지 리스트 갯수

        PageUtil pageUtil = new PageUtil();
        Page<QuestionEntity> myQuestionList = memberService.myQuestionList(email, page, questionNum);
        int totalCount = (int) myQuestionList.getTotalElements();

        model.addAttribute("myQuestionList", myQuestionList);
        model.addAttribute("listIsEmpty", myQuestionList.hasContent()?"N":"Y");
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("questionNum", questionNum);
        model.addAttribute("pageList", pageUtil.getPageListNoKeyword(page, questionNum, pagelistNum, totalCount));

    }
    

    // 내 쿠폰 리스트 화면
    @GetMapping("/mypage/myCouponList")
    public void getMyCouponList(
        Model model,
        HttpSession session,
        @RequestParam("page")
        int page
    ) {
        
        String email = (String) session.getAttribute("email");

        int couponNum = 10;   // 한 번에 보여줄 쿠폰 갯수
        int pagelistNum = 10;   // 페이지 리스트 갯수

        PageUtil pageUtil = new PageUtil();
        Page<CouponEntity> myCouponList = memberService.myCouponList(email, page, couponNum);
        int totalCount = (int) myCouponList.getTotalElements();

        model.addAttribute("myCouponList", myCouponList);
        model.addAttribute("listIsEmpty", myCouponList.hasContent()?"N":"Y");
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("couponNum", couponNum);
        model.addAttribute("pageList", pageUtil.getPageListNoKeyword(page, couponNum, pagelistNum, totalCount));

    }
    

    // 내 등급 화면
    @GetMapping("/mypage/myGrade")
    public void getMyGrade(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        model.addAttribute("member", memberService.memberInfo(email));
    }
    

    // 아이디 찾기 화면
    @GetMapping("/member/searchID")
    public void getSearchID() {}
    

    // 아이디 찾기
    @ResponseBody
    @PostMapping("/member/searchID")
    public String postSearchID(
        @RequestParam("username") String username,
        @RequestParam("telno") String telno
    ) throws Exception {
        
        MemberDTO member = new MemberDTO();
        member.setUsername(username);
        member.setTelno(telno);
        String email = memberService.searchID(member) == null? "ID_NOT_FOUND":memberService.searchID(member);

        return "{\"message\":\"" + email + "\"}";
    }
    

    // 비밀번호 찾기 화면
    @GetMapping("/member/searchPassword")
    public void getSearchPassword() {}


    // 비밀번호 찾기(임시 비밀번호)
    @ResponseBody
    @PostMapping("/member/searchPassword")
    public String postSearchPassword(
        @RequestParam("email") String email
    ) throws Exception {
        
        if (memberService.idCheck(email) == 0) {
            return "{\"message\":\"ID_NOT_FOUND\"}";
        }

        PasswordMaker pwmaker = new PasswordMaker();
        String tempPW = pwmaker.tempPasswordMaker();

        MemberDTO member = new MemberDTO();
        member.setEmail(email);
        member.setPassword(pwdEncoder.encode(tempPW));
        memberService.modifyMemberPassword(member);
        memberService.lastdateUpdate(email, "password");

        //이메일 발송
        emailService.sendTempPw(email, tempPW);
        
        return "{\"message\":\"good\"}";
    }
    

    // 비밀번호 변경 화면
    @GetMapping("/mypage/modifyMemberPassword")
    public void getModifyMemberPassword() {}
    

    // 비밀번호 변경
    @PostMapping("/mypage/modifyMemberPassword")
    public String postModifyMemberPassword(
        @RequestParam("old_password") String old_password,
        @RequestParam("new_password") String new_password,
        HttpSession session
    ) throws Exception {
        
        String email = (String) session.getAttribute("email");

        if (!pwdEncoder.matches(old_password, memberService.memberInfo(email).getPassword())) {
            return "{\"message\":\"PASSWORD_NOT_FOUND\"}";
        }

        MemberDTO member = new MemberDTO();
        member.setEmail(email);
        member.setPassword(pwdEncoder.encode(new_password));
        memberService.modifyMemberPassword(member);
        memberService.lastdateUpdate(email, "password");
        return "{\"message\":\"good\"}";
    }
    

    // 30일 이후 비밀번호 변경 화면 보기
    @GetMapping("/mypage/checkPasswordNotice")
    public void getCheckPasswordNotice(
        Model model,
        HttpSession session
    ) throws Exception {
        
        String email = (String) session.getAttribute("email");
        MemberDTO member = memberService.memberInfo(email);
        int addedDate = 30 * (member.getPwcheck() + 1);
        model.addAttribute("addedDate", addedDate);
    }
    

    // 비밀번호 변경 30일 이후로 연기
    @GetMapping("/mypage/modifyPasswordAfter30")
    public String getModifyPasswordAfter30(HttpSession session) throws Exception {
        String email = (String) session.getAttribute("email");
        memberService.modifyPasswordAfter30(email);
        return "redirect:/shop/list?page=1";
    }
    

    // 아이디 중복 확인
    @ResponseBody
    @PostMapping("/member/idCheck")
    public int postIdcheck(@RequestBody String email) throws Exception {
        return memberService.idCheck(email);
    }
    

    // 회원 탈퇴
    @Transactional
    @GetMapping("/mypage/deleteID")
    public String getDeleteID(HttpSession session) throws Exception {
        
        String email = (String) session.getAttribute("email");

        // 운영체제에 따른 이미지 디렉토리 설정
        String os = System.getProperty("os.name").toLowerCase();
		String questionFilePath;
		String reviewFilePath;
		if(os.contains("win")) {
			questionFilePath ="c:\\Repository\\question\\";
			reviewFilePath ="c:\\Repository\\review\\";
		} else {
			questionFilePath = "/home/hasb/Repository/question";
			reviewFilePath = "/home/hasb/Repository/review";
		}

        // 디렉토리가 없으면 생성
        File question = new File(questionFilePath);
        if(!question.exists()) question.mkdirs();
        File review = new File(reviewFilePath);
        if(!review.exists()) review.mkdirs();
        // 운영체제에 따른 이미지 디렉토리 설정 종료

        // 회원이 첨부한 문의 파일 삭제
        List<QuestionFileEntity> questionStoredFilenameList = memberService.getStoredQuestionFilenameList(email);
        File questFilename;
        for (QuestionFileEntity questionFileEntity : questionStoredFilenameList) {
            questFilename = new File(questionFilePath + questionFileEntity.getStoredFilename());
            questFilename.delete();
        }

        // 회원이 첨부한 리뷰 파일 삭제
        List<ReviewFileEntity> reviewStoredFilenameList = memberService.getStoredReviewFilenameList(email);
        File reviewFilename;
        for (ReviewFileEntity reviewFileEntity : reviewStoredFilenameList) {
            reviewFilename = new File(reviewFilePath + reviewFileEntity.getStoredFilename());
            reviewFilename.delete();
        }

        // 회원 정보 삭제
        memberService.deleteID(email);

        // 세션 삭제
        session.invalidate();

        return "redirect:/";
    }
}
