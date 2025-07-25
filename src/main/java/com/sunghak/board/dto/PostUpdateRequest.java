package com.sunghak.board.dto;

public class PostUpdateRequest {

    private final String title;
    private final String content;

    public PostUpdateRequest(String title, String content) {
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
