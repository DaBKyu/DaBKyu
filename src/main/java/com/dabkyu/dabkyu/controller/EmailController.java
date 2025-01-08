package com.dabkyu.dabkyu.controller;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dabkyu.dabkyu.service.EmailService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@AllArgsConstructor
@RequestMapping("/master")
public class EmailController {

    private final EmailService emailService;
   
    //이메일 발송 화면 보기
	@GetMapping("/mail/sendEmail")
	public void getWrite() { }

    // 이메일 발송
    @ResponseBody
    @PostMapping("/mail/sendEmail")
    public String sendCategoryInterestMail(
        @RequestParam(name = "category3SeqnoList", required = false) List<Long> category3SeqnoList,
        @RequestParam(name = "productSeqnoList", required = false) List<Long> productSeqnoList,
        @RequestParam(name = "mailFileList", required = false) MultipartFile[] mailFileList,
        @RequestParam(name = "kind") String kind,
        @RequestParam(name = "title") String title,
        @RequestParam(name = "content") String content,
        @RequestParam(name = "couponSeqnoList", required = false) List<Long> couponSeqnoList
    ) throws Exception {

        // 운영체제에 따라 이미지 저장 경로 설정
        String os = System.getProperty("os.name").toLowerCase();
        String fileSavePath = os.contains("win") 
            ? "c:\\Repository\\dabkyu\\mail\\images\\" 
            : "/home/gladius/Repository/dabkyu/mail/images/";

        // 디렉토리 존재 여부 확인 후, 없으면 생성
        File saveDir = new File(fileSavePath);
        if (!saveDir.exists()) {
            System.out.println("디렉토리 존재하지 않음. 생성 시도...");
            if (saveDir.mkdirs()) {
                System.out.println("디렉토리 생성 성공");
            } else {
                System.err.println("첨부파일 저장 디렉토리 생성 실패. 경로: " + saveDir.getAbsolutePath());
            }
        } else {
            System.out.println("디렉토리가 이미 존재합니다.");
        }


        //이메일 발송
        emailService.sendEmail(title, content, mailFileList, kind, category3SeqnoList, productSeqnoList, couponSeqnoList);

        // maxSeqno 구하기
        Long maxSeqno = emailService.getMaxSeqno();
        
        //카테고리저장 
        if(category3SeqnoList != null) {
          emailService.saveEmailCategory(maxSeqno, category3SeqnoList);
        }
        
        //찜상품저장
        if(productSeqnoList != null) {
           emailService.saveEmailLike(maxSeqno, productSeqnoList);
        }
        
        //파일 저장
        if (mailFileList != null && mailFileList.length > 0) {
            for (MultipartFile file : mailFileList) {
                if (!file.isEmpty()) {
                    emailService.saveEmailFile(maxSeqno, fileSavePath, new MultipartFile[]{file});
                }
            }
        }
        
        return "{\"message\":\"good\"}";

    } 

    /*
    // 메일 발송 내역 보기
	@GetMapping("/mail/mailSendList")
	public void getReviewList(Model model,@RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword) throws Exception {
		
		int postNum = 10; //한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 10; //화면 하단에 보여지는 페이지리스트의 페이지 갯수	
		
		PageUtil page = new PageUtil();
		Page<EmailEntity> list = emailService.list(pageNum, postNum, keyword);
		int totalCount = (int)list.getTotalElements();

		model.addAttribute("list", list);
		model.addAttribute("listIsEmpty", list.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList", page.getMailSendPageList(pageNum, postNum, pageListCount,totalCount,keyword));
	}
         

	// 메일 내용 상세 보기
	@GetMapping("/mail/mailView")
	public void getReviewView(@RequestParam("emailSeqno") Long emailSeqno, @RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword,
			Model model, HttpSession session) throws Exception {
		
		model.addAttribute("view", emailService.view(emailSeqno));
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pre_seqno", emailService.pre_seqno(emailSeqno,keyword));		
		model.addAttribute("next_seqno", emailService.next_seqno(emailSeqno,keyword));
		model.addAttribute("fileListView", emailService.fileListView(emailSeqno));	
	}
    */
        
}
