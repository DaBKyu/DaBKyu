package com.dabkyu.dabkyu.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.entity.AddressEntity;
import com.dabkyu.dabkyu.entity.QuestionFileEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {@Override
    public void signup(MemberDTO member) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'signup'");
    }

    @Override
    public void modifyMemberInfo(MemberDTO member) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifyMemberInfo'");
    }

    @Override
    public void modifyMemberPassword(MemberDTO member) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifyMemberPassword'");
    }

    @Override
    public void modifyPasswordAfter30(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifyPasswordAfter30'");
    }

    @Override
    public String searchID(MemberDTO member) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchID'");
    }

    @Override
    public int idCheck(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'idCheck'");
    }

    @Override
    public Page<AddressEntity> addrSearch(int pageNum, int postNum, String addrSearch) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addrSearch'");
    }

    @Override
    public MemberDTO memberInfo(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'memberInfo'");
    }

    @Override
    public void lastdateUpdate(String email, String status) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'lastdateUpdate'");
    }

    @Override
    public List<QuestionFileEntity> getStoredQuestionFilenameList(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStoredQuestionFilenameList'");
    }

    @Override
    public List<ReviewFileEntity> getStoredReviewFilenameList(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStoredReviewFilenameList'");
    }

    @Override
    public void deleteID(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteID'");
    }

}
