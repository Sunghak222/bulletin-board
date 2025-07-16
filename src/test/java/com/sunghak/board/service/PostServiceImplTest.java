package com.sunghak.board.service;

import com.sunghak.board.entity.Member;
import com.sunghak.board.entity.Post;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PostServiceImplTest {

    @Autowired
    PostService postService;

    @Autowired
    MemberService memberService;

    @Test
    void save() {
        Member member = new Member();
        memberService.save(member);

        Post post = new Post("title", "content", member);
        postService.save(post);
        Post found = postService.findById(post.getId());

        assertThat(found.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    void findAll() {
        Member member = new Member();
        memberService.save(member);

        Post post1 = new Post("title1", "content", member);
        Post post2 = new Post("title2", "content", member);
        Post post3 = new Post("title1", "content", member);

        postService.save(post1);
        postService.save(post2);
        postService.save(post3);

        List<Post> posts = postService.findAll();
        assertThat(posts.size()).isEqualTo(3);
    }

    @Test
    void delete() {
        Post post1 = new Post("title1", "content", new Member());
        Post post2 = new Post("title2", "content", new Member());
        postService.save(post1);
        postService.save(post2);
        postService.delete(post1.getId());

        assertThrows(EntityNotFoundException.class, () -> postService.findById(post1.getId()));
    }
}