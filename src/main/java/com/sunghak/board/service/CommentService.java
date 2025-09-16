package com.sunghak.board.service;

import com.sunghak.board.entity.Comment;
import com.sunghak.board.entity.Post;

import java.util.List;

public interface CommentService {

    Comment save(Comment comment);
    Comment write(Long postId, Long authorId, String content, Long parentId);
    Comment findById(Long id);
    List<Comment> findByPostId(Long postId);
    List<Comment> findAll();
    void delete(Long id);
    List<Comment> getSortedComments(Long postId);
}
