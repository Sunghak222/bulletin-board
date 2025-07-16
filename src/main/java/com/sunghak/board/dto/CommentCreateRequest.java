package com.sunghak.board.dto;

import com.sunghak.board.entity.Post;

public class CommentCreateRequest {

    private final String content;

    public CommentCreateRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
