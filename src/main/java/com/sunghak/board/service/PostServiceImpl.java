package com.sunghak.board.service;

import com.sunghak.board.dto.PostDTO;
import com.sunghak.board.entity.Member;
import com.sunghak.board.entity.Post;
import com.sunghak.board.repository.CommentRepository;
import com.sunghak.board.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public void incrementViews(Long postId) {
        postRepository.incrementViews(postId);
    }
    @Transactional
    public PostDTO getDetailAndMaybeIncrementViews(Long id, boolean shouldIncrement) {
        if (shouldIncrement) {
            postRepository.incrementViews(id);
        }
        return postRepository.findDetailById(id)
                .map(PostDTO::new)
                .orElse(null);
    }
}
