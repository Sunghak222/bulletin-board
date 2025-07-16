package com.sunghak.board.dto;

public class CommentDeleteRequest {

    private Long commentId;

    public CommentDeleteRequest(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }
}
