package com.sunghak.board.repository;

import com.sunghak.board.entity.Member;
import com.sunghak.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitle(String title);

    List<Post> findByContent(String content);

    List<Post> findByAuthor(Member author);

    List<Post> findByTitleAndContent(String title, String content);

    List<Post> findByAuthorAndTitle(Member author, String title);

    List<Post> findByAuthorAndContent(Member author, String content);

    List<Post> findByAuthorAndTitleAndContent(Member author, String title, String content);

    List<Post> findByTitleContaining(String title);
    List<Post> findByContentContaining(String content);
    List<Post> findByAuthorContaining(Member author);
    List<Post> findByTitleContainingAndContentContaining(String title, String content);
    List<Post> findByAuthorContainingAndTitleContaining(String author, String title);
    List<Post> findByAuthorContainingAndContentContaining(String author, String content);
    List<Post> findByAuthorContainingAndTitleContainingAndContentContaining(String author, String title, String content);
}