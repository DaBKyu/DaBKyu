// package com.dabkyu.dabkyu;

// import com.dabkyu.dabkyu.controller.EmailController;
// import com.dabkyu.dabkyu.service.EmailService;
// import com.dabkyu.dabkyu.service.EmailServiceImpl;
// import com.dabkyu.dabkyu.service.MailSendService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import javax.mail.MessagingException;
// import java.io.IOException;

// import static org.mockito.Mockito.*;

// @SpringBootTest
// public class EmailControllerTest {

//     @InjectMocks
//     private EmailController emailController;

//     @Mock
//     private EmailServiceImpl emailService;

//     @Mock
//     private MailSendService mailSendService;

//     private MockMvc mockMvc;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
//     }

//     @Test
//     public void testSendMail() throws Exception {
//         // 이메일 발송 테스트
//         String[] recipients = {"test@example.com"};
//         String subject = "Test Email";
//         String content = "This is a test email.";
        
//         doNothing().when(emailService).sendMail(recipients, subject, content, null);

//         // 실제 이메일 발송 메소드 호출
//         mockMvc.perform(post("/send")
//                 .param("recipients", String.join(",", recipients))
//                 .param("subject", subject)
//                 .param("content", content)
//                 .param("attachments", ""))  // 첨부파일은 비워둡니다.
//                 .andExpect(status().isOk());

//         // 이메일 발송 메소드가 호출됐는지 확인
//         verify(emailService, times(1)).sendMail(recipients, subject, content, null);
//     }

//     @Test
//     public void testGetMailSendList() throws Exception {
//         // 메일 발송 내역 조회 테스트
//         when(mailSendService.getTotalMailSendCount()).thenReturn(100L);
//         when(mailSendService.getMailSendList(10, 1)).thenReturn(null);  // 실제 데이터를 반환하지 않고 단순히 호출을 테스트

//         mockMvc.perform(get("/mailSendList")
//                 .param("page", "1"))
//                 .andExpect(status().isOk());

//         verify(mailSendService, times(1)).getMailSendList(10, 1);
//     }
// }
