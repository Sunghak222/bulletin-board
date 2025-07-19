package com.sunghak.board.controller;

import com.sunghak.board.dto.CommentCreateRequest;
import com.sunghak.board.dto.CommentUpdateRequest;
import com.sunghak.board.dto.SessionMember;
import com.sunghak.board.entity.Comment;
import com.sunghak.board.entity.Member;
import com.sunghak.board.entity.Post;
import com.sunghak.board.service.CommentService;
import com.sunghak.board.service.MemberService;
import com.sunghak.board.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequestMapping("posts/{postId}/comments")
@Controller
public class CommentController {
    // 댓글의 CRUD 는 여기서, 댓글의 list는 PostController에서

    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    public CommentController(MemberService memberService, PostService postService, CommentService commentService) {
        this.memberService = memberService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping
    public String createComment(@PathVariable Long postId, HttpSession session,
                                @ModelAttribute CommentCreateRequest commentCreateRequest) {
        SessionMember loginMember = (SessionMember) session.getAttribute("loginMember");
        if (loginMember == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only logged-in members can post comments.");
        }
        Member member = memberService.findById(loginMember.getId());
        Post post = postService.findById(postId);

        Comment comment = new Comment();
        comment.setContent(commentCreateRequest.getContent());
        comment.setPost(post);
        comment.setAuthor(member);

        commentService.save(comment);

        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{commentId}/edit")
    public String editForm(@PathVariable Long commentId, HttpSession session) {
        Comment comment = commentService.findById(commentId);
        Member author = comment.getAuthor();
        SessionMember loginMember = (SessionMember) session.getAttribute("loginMember");
        if (loginMember == null || !loginMember.getId().equals(comment.getAuthor().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to edit this comment");
        }

        return "comment/edit-form";
    }

    /**
     * /posts/1에서 commentId=3인 댓글의 수정버튼 누름
     * /posts/1?edit=7 요청
     * PostController의 @GetMapping("/{postId}") 호출
     * RequestParam의 editingCommentId에 값이 들어감
     * post.html에서 해당 댓글만 edit-form 렌더링
     */
    @PostMapping("/{commentId}/edit")
    public String editComment(@PathVariable Long commentId,
                              @ModelAttribute CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentService.findById(commentId);
        comment.setContent(commentUpdateRequest.getContent());
        commentService.save(comment);

        return "redirect:/posts/" + comment.getPost().getId();
    }
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                HttpSession session) {
        Post post = postService.findById(postId);
        Comment comment = commentService.findById(commentId);
        SessionMember loginMember = (SessionMember) session.getAttribute("loginMember");
        if (loginMember == null || !loginMember.getId().equals(comment.getAuthor().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this comment");
        }

        commentService.delete(commentId);
        return "redirect:/posts/" + postId;
    }
}
