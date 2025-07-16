package com.sunghak.board.dto;

public class CommentUpdateRequest {

    private final String content;

    public CommentUpdateRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
