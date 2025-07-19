package com.sunghak.board.apicontroller;

import com.sunghak.board.dto.PostCreateRequest;
import com.sunghak.board.dto.PostUpdateRequest;
import com.sunghak.board.dto.SessionMember;
import com.sunghak.board.entity.Comment;
import com.sunghak.board.entity.Member;
import com.sunghak.board.entity.Post;
import com.sunghak.board.service.CommentService;
import com.sunghak.board.service.MemberService;
import com.sunghak.board.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api/posts")
@RestController
public class PostApiController {

    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    public PostApiController(PostService postService, MemberService memberService, CommentService commentService) {
        this.postService = postService;
        this.memberService = memberService;
        this.commentService = commentService;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> list() {
        List<Post> posts = postService.findAll();
        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("posts", posts);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createPost(@RequestBody PostCreateRequest request, HttpSession session) {

        SessionMember loginSessionMember = (SessionMember) session.getAttribute("loginMember");
        Member loginMember = memberService.findById(loginSessionMember.getId());

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(loginMember);

        postService.save(post);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Post Created");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> post(@PathVariable Long postId) {
        Post post = postService.findById(postId);

        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("post", post);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Map<String,String>> updatePost(@PathVariable Long postId,
                             @RequestBody PostUpdateRequest request,
                             HttpSession session) {

        Post post = postService.findById(postId);
        SessionMember loginMember = (SessionMember) session.getAttribute("loginMember");
        Map<String, String> response = new HashMap<>();

        if (loginMember == null || !post.getAuthor().getId().equals(loginMember.getId())) {
            response.put("status", "error");
            response.put("error", "You do not have permission to edit this post");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        postService.save(post);

        response.put("status", "success");
        response.put("message", "Post Updated");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable Long postId , HttpSession session) {

        Post post = postService.findById(postId);
        Member author = post.getAuthor();
        SessionMember loginMember = (SessionMember) session.getAttribute("loginMember");

        Map<String, String> response = new HashMap<>();

        if (loginMember == null || !author.getId().equals(loginMember.getId())) {
            response.put("status", "error");
            response.put("error", "You do not have permission to delete this post");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        postService.delete(postId);

        response.put("status", "success");
        response.put("message", "Post deleted successfully"); // Changed
        return ResponseEntity.ok(response);
    }
}
