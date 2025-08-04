package com.sunghak.board.dto;

import com.sunghak.board.entity.Comment;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String content;
    private Long authorId;
    private String authorName;
    private String createdAt;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorId = comment.getAuthor().getId();
        this.authorName = comment.getAuthor().getName();
        this.createdAt = comment.getCreatedAt().toString();
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
