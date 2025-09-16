package com.sunghak.board.dto;

import com.sunghak.board.entity.Comment;
import com.sunghak.board.entity.Post;

public class CommentCreateRequest {

    private final String content;
    private final Long parentId;

    public CommentCreateRequest(String content, Long parentId) {
        this.content = content;
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getContent() {
        return content;
    }
}
