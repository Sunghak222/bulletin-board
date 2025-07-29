package com.sunghak.board.repository;

import com.sunghak.board.entity.Member;
import com.sunghak.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

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
    List<Post> findByTitleContainingAndContentContaining(String title, String content);

    List<Post> findByAuthor_NameContaining(String name);
    List<Post> findByAuthor_NameContainingAndTitleContaining(String name, String title);
    List<Post> findByAuthor_NameContainingAndContentContaining(String name, String content);
    List<Post> findByAuthor_NameContainingAndTitleContainingAndContentContaining(String name, String title, String content);

}
