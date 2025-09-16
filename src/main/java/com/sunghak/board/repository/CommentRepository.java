package com.sunghak.board.repository;

import com.sunghak.board.entity.Comment;
import com.sunghak.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    @Modifying
    @Query("delete from Comment c where c.post.id = :postId")
    int deleteByPostId(@Param("postId") Long postId);

    @Query("select c from Comment c where c.post.id = :postId order by c.createdAt asc")
    List<Comment> findAllByPostIdOrderByCreatedAt(Long postId);
}
