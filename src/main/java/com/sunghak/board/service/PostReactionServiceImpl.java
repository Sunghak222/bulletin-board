package com.sunghak.board.service;

import com.sunghak.board.entity.*;
import com.sunghak.board.repository.MemberRepository;
import com.sunghak.board.repository.PostReactionRepository;
import com.sunghak.board.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostReactionServiceImpl implements PostReactionService {

    PostReactionRepository postReactionRepository;
    PostRepository postRepository;
    MemberRepository memberRepository;

    public PostReactionServiceImpl(PostReactionRepository postReactionRepository, PostRepository postRepository, MemberRepository memberRepository) {
        this.postReactionRepository = postReactionRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public ReactionCount saveReaction(Long postId, Long memberId, ReactionType reactionType) {

        Optional<PostReaction> findPostReaction = postReactionRepository.findByPostIdAndMemberId(postId, memberId);

        if (findPostReaction.isEmpty()) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found"));
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new EntityNotFoundException("Member not found"));
            PostReaction pr = new PostReaction(post,member,reactionType);

            try {
                postReactionRepository.save(pr);
            } catch (DataIntegrityViolationException e) {
                PostReaction existingAfterRace = postReactionRepository
                        .findByPostIdAndMemberId(postId, memberId)
                        .orElseThrow(() -> e);
                if (existingAfterRace.getType() == reactionType) {
                    postReactionRepository.delete(existingAfterRace);
                } else {
                    existingAfterRace.setType(reactionType);
                }
            }

        }
        else if (findPostReaction.get().getType() == reactionType) {
            postReactionRepository.delete(findPostReaction.get());
        }
        else {
            findPostReaction.get().setType(reactionType);
        }
        return count(postId);
    }

    @Override
    @Transactional
    public ReactionCount deleteReaction(Long postId, Long memberId) {
        postReactionRepository.deleteByPostIdAndMemberId(postId, memberId);
        return count(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public ReactionCount count(Long postId) {
        return new ReactionCount(postReactionRepository.countLikes(postId), postReactionRepository.countDislikes(postId));
    }
}
