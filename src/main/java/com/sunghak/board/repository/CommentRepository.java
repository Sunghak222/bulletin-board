package com.sunghak.board.repository;

import com.sunghak.board.entity.Comment;
import com.sunghak.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
