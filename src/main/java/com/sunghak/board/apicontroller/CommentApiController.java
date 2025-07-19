package com.sunghak.board.apicontroller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("api/")
@RestController
public class CommentApiController {

    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    public CommentApiController(MemberService memberService, PostService postService, CommentService commentService) {
        this.memberService = memberService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Map<String, String>> createComment(@RequestBody CommentCreateRequest request,
                                                             @PathVariable Long postId,
                                                             HttpSession session) {

        SessionMember loginMember = (SessionMember) session.getAttribute("loginMember");
        Map<String, String> response = new HashMap<>();

        if (loginMember == null) {
            response.put("status", "error");
            response.put("error", "Only logged-in members can post comments");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Member member = memberService.findById(loginMember.getId());
        Post post = postService.findById(postId);

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPost(post);
        comment.setAuthor(member);

        commentService.save(comment);

        response.put("status", "success");
        response.put("message", "Comment Created Successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Map<String, String>> editComment(@PathVariable Long commentId,
                              @RequestBody CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentService.findById(commentId);
        comment.setContent(commentUpdateRequest.getContent());
        commentService.save(comment);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Comment Updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long commentId,
                                                             HttpSession session) {
        Comment comment = commentService.findById(commentId);
        SessionMember loginMember = (SessionMember) session.getAttribute("loginMember");

        Map<String, String> response = new HashMap<>();

        if (loginMember == null || !loginMember.getId().equals(comment.getAuthor().getId())) {
            response.put("status", "error");
            response.put("error", "You do not have permission to delete this comment");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        commentService.delete(commentId);
        response.put("status", "success");
        response.put("message", "Comment deleted successfully");
        return ResponseEntity.ok(response);
    }
}
