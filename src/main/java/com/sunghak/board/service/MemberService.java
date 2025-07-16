package com.sunghak.board.service;

import com.sunghak.board.entity.Member;

import java.util.List;

public interface MemberService {

    Member save(Member member);
    Member findById(Long id);
    Member findByEmail(String email);
    List<Member> findAll();
    void delete(Long id);
}
