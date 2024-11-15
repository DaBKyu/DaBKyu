package com.dabkyu.dabkyu.service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    
    public void sendMail(String[] recipients, String subject, String content, MultipartFile[] attachments);
}
