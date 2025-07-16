package com.sunghak.board.dto;

public class PostCreateRequest {

    private final String title;
    private final String content;

    public PostCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
