package com.dabkyu.dabkyu.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.entity.EmailSendListEntity;
import com.dabkyu.dabkyu.entity.repository.EmailSendListRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailSendServiceImpl implements MailSendService{

    private final EmailSendListRepository emailSendListRepository;

     // 메일 발송 내역 목록 조회
    public List<EmailSendListEntity> getMailSendList(int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        Page<EmailSendListEntity> page = emailSendListRepository.findAll(pageRequest);
        return page.getContent();
    }

    // 메일 발송 내역 총 개수 조회
    public long getTotalMailSendCount() {
        return emailSendListRepository.count();
    }
}
