package com.dabkyu.dabkyu.service;

import java.util.List;

import com.dabkyu.dabkyu.entity.EmailSendListEntity;

public interface MailSendService {

    // 메일 발송 내역 목록 조회
    public List<EmailSendListEntity> getMailSendList(int pageNum, int pageSize);

     // 메일 발송 내역 총 개수 조회
     public long getTotalMailSendCount();

}
