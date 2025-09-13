package com.sunghak.board.repository;

import com.sunghak.board.entity.Member;
import com.sunghak.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :postId")
    int incrementViews(@Param("postId") Long postId);

    //left join fetch p.author : Fetch p.author immediately, no lazy fetch
    @Query("""
       select distinct p
       from Post p
       left join fetch p.author
       where p.id = :id
       """)
    Optional<Post> findDetailById(@Param("id") Long id);
}