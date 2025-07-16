package com.sunghak.board.service;

import com.sunghak.board.entity.Post;

import java.util.List;

public interface PostService {

    Post save(Post post);
    Post findById(Long id);
    List<Post> findAll();
    void delete(Long id);
}
