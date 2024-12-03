package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.MemberAddressEntity;

import java.util.List;


public interface MemberAddressRepository extends JpaRepository<MemberAddressEntity, Long> {
    public List<MemberAddressEntity> findByEmail_Email(String email);

    public MemberAddressEntity findByEmail_EmailAndIsBasic(String email, String isBasic);

    public List<MemberAddressEntity> findByMemberAddressSeqno(Long memberAddressSeqno);
}
