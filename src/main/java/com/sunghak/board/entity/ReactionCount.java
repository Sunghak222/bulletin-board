package com.sunghak.board.entity;

public class ReactionCount {
    long likes;
    long dislikes;

    public ReactionCount(long likes, long dislikes) {
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }
}
