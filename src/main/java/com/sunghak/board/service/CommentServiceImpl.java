package com.sunghak.board.service;

import com.sunghak.board.entity.Comment;
import com.sunghak.board.entity.Member;
import com.sunghak.board.entity.Post;
import com.sunghak.board.repository.CommentRepository;
import com.sunghak.board.repository.MemberRepository;
import com.sunghak.board.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, MemberRepository memberRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment write(Long postId, Long authorId, String content, Long parentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found: " + postId));

        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found: " + authorId));

        Comment parent = null;
        if (parentId != null) {
            parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found: " + parentId));
            if (!parent.getPost().getId().equals(postId)) {
                throw new IllegalStateException("Parent comment belongs to a different post");
            }
            if (parent.getParent() != null) { //limit the comment depth to 2
                throw new IllegalStateException("Replies are limited to 2 levels");
            }
        }
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setPost(post);
        comment.setParent(parent);

        return commentRepository.save(comment);

    }


    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> getSortedComments(Long postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAt(postId);
    }
}
