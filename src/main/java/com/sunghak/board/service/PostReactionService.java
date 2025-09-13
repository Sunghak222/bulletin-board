package com.sunghak.board.service;

import com.sunghak.board.entity.PostReaction;
import com.sunghak.board.entity.ReactionCount;
import com.sunghak.board.entity.ReactionType;

import java.util.Optional;

public interface PostReactionService {
    ReactionCount saveReaction(Long postId, Long memberId, ReactionType reactionType);
    ReactionCount deleteReaction(Long postId, Long memberId);
    ReactionCount count(Long postId);
}
