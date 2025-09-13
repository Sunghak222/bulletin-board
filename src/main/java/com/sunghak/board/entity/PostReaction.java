package com.sunghak.board.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_reaction",
        uniqueConstraints = @UniqueConstraint(name="uq_post_member", columnNames={"post_id","member_id"}))
public class PostReaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ReactionType type;

    public PostReaction() {}

    public PostReaction(Post post, Member member, ReactionType type) {
        this.post = post;
        this.member = member;
        this.type = type;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public ReactionType getType() {
        return type;
    }

    public void setType(ReactionType type) {
        this.type = type;
    }
}
