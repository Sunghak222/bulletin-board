package com.sunghak.board.repository;

import com.sunghak.board.entity.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Optional<PostReaction> findByPostIdAndMemberId(Long postId, Long memberId);
    void deleteByPostIdAndMemberId(Long postId, Long memberId);

    @Query("select count(r) from PostReaction r where r.post.id = :postId and r.type = 'LIKE'")
    long countLikes(@Param("postId") Long postId);

    @Query("select count(r) from PostReaction r where r.post.id = :postId and r.type = 'DISLIKE'")
    long countDislikes(@Param("postId") Long postId);
}
