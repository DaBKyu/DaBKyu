package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dabkyu.dabkyu.entity.EmailSendListEntity;
import com.dabkyu.dabkyu.entity.repository.EmailSendListRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final EmailSendListRepository emailSendListRepository;

    @Override
    public void sendMail(String[] recipients, String subject, String content, MultipartFile[] attachments) {
        MimeMessage message = emailSender.createMimeMessage();
        try {

            // EmailDataProvider에서 이메일 정보 가져오기
            //EmailDetails emailDetails = EmailDataProvider.getEmailDetails();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // 이메일 내용 설정
            //helper.setTo(emailDetails.getRecipients());
            //helper.setSubject(emailDetails.getSubject());
            //helper.setText(emailDetails.getContent());

            // 이메일 내용 설정
            helper.setTo(recipients);
            helper.setSubject(subject);
            helper.setText(content);

            // 첨부파일 처리
            if (attachments != null && attachments.length > 0) {
                for (MultipartFile attachment : attachments) {
                    helper.addAttachment(attachment.getOriginalFilename(), attachment);
                }
            }

            // 이메일 발송
            emailSender.send(message);


            // 발송 기록 저장
            EmailSendListEntity emailSendListEntity = EmailSendListEntity.builder()
                                                                         .emailTitle(subject)
                                                                         .emailContent(content)
                                                                         .emailSendDate(LocalDateTime.now())
                                                                         .build();
             // 발송 기록 저장
             emailSendListRepository.save(emailSendListEntity);

        } catch (MessagingException e) {
            e.printStackTrace();
            // 예외 처리 (로깅, 에러 응답 등)
        }
    }
}
