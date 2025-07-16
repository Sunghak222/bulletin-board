package com.sunghak.board.service;

import com.sunghak.board.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @Test
    void save() {
        Member member = new Member("example@domain.com","1234ab","man","member");
        memberService.save(member);
        Member savedMember = memberService.findById(member.getId());
        assertNotNull(savedMember);
    }

    @Test
    void findByEmail() {
        Member member = new Member("example@domain.com","1234ab","man","member");
        memberService.save(member);
        Member savedMember = memberService.findByEmail(member.getEmail());
        assertNotNull(savedMember);
    }


    @Test
    void findAll() {
        Member member1 = new Member("example1@domain.com","1234ab","man","member");
        Member member2 = new Member("example2@domain.com","1234ab","man","member");
        Member member3 = new Member("example3@domain.com","1234ab","man","member");

        memberService.save(member1);
        memberService.save(member2);
        memberService.save(member3);

        List<Member> members = memberService.findAll();
        assertThat(members)
                .extracting("email")
                .containsExactly("example1@domain.com", "example2@domain.com","example3@domain.com");
    }

    @Test
    void delete() {
        Member member = new Member("example1@domain.com","1234ab","man","member");
        memberService.save(member);
        memberService.delete(member.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            memberService.findById(member.getId());
        });
    }
}