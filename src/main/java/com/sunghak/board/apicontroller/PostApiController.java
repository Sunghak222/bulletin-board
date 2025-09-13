package com.sunghak.board.apicontroller;

import com.sunghak.board.dto.PostCreateRequest;
import com.sunghak.board.dto.PostDTO;
import com.sunghak.board.dto.PostUpdateRequest;
import com.sunghak.board.dto.SessionMember;
import com.sunghak.board.entity.*;
import com.sunghak.board.service.CommentService;
import com.sunghak.board.service.MemberService;
import com.sunghak.board.service.PostReactionService;
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
    private final PostReactionService postReactionService;

    public PostApiController(MemberService memberService, PostService postService, PostReactionService postReactionService) {
        this.memberService = memberService;
        this.postService = postService;
        this.postReactionService = postReactionService;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> list() {
        List<Post> findPosts = postService.findAll();
        List<PostDTO> posts = findPosts.stream().map(PostDTO::new).toList();
        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("posts", posts);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createPost(@RequestBody PostCreateRequest request, HttpSession session) {

        SessionMember loginSessionMember = (SessionMember) session.getAttribute("loginMember");

        Map<String, String> response = new HashMap<>();
        if (loginSessionMember == null) {
            response.put("status", "fail");
            response.put("error", "You do not have permission to create post");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Member loginMember = memberService.findById(loginSessionMember.getId());

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(loginMember);

        postService.save(post);

        response.put("status", "success");
        response.put("message", "Post Created");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> post(@PathVariable Long postId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String key = "viewed_post_" + postId;
        boolean hasSession = (session.getAttribute(key) == null);
        if (hasSession) session.setAttribute(key, Boolean.TRUE);

        PostDTO postDto = postService.getDetailAndMaybeIncrementViews(postId, hasSession);
        ReactionCount rc = postReactionService.count(postId);

        if (postDto == null) {
            response.put("status", "error");
            response.put("error", "Post Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("status", "success");
        response.put("post", postDto);
        response.put("reactions", Map.of(
                "likeCount", rc.getLikes(),
                "dislikeCount", rc.getDislikes()
        ));
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
        response.put("message", "Post deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> likePost(@PathVariable Long postId, HttpSession session) {
        SessionMember loginMember = (SessionMember) session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "error", "You do not have permission to like this post"
            ));
        }
        ReactionCount rc = postReactionService.saveReaction(postId,loginMember.getId(), ReactionType.LIKE);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "likeCount", rc.getLikes(),
                "dislikeCount", rc.getDislikes()
        ));
    }
    @PostMapping("/{postId}/dislike")
    public ResponseEntity<Map<String, Object>> dislikePost(@PathVariable Long postId, HttpSession session) {
        SessionMember loginMember = (SessionMember) session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "error", "You do not have permission to like this post"
            ));
        }
        ReactionCount rc = postReactionService.saveReaction(postId,loginMember.getId(), ReactionType.DISLIKE);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "likeCount", rc.getLikes(),
                "dislikeCount", rc.getDislikes()
        ));
    }
}
