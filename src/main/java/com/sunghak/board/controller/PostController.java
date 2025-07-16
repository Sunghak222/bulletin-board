package com.sunghak.board.controller;

import com.sunghak.board.dto.PostCreateRequest;
import com.sunghak.board.dto.PostUpdateRequest;
import com.sunghak.board.dto.SessionMember;
import com.sunghak.board.entity.Comment;
import com.sunghak.board.entity.Member;
import com.sunghak.board.entity.Post;
import com.sunghak.board.service.CommentService;
import com.sunghak.board.service.MemberService;
import com.sunghak.board.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RequestMapping("/posts")
@Controller
public class PostController {

    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, MemberService memberService, CommentService commentService) {
        this.postService = postService;
        this.memberService = memberService;
        this.commentService = commentService;
    }

    @GetMapping("")
    public String list(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "post/post-list";
    }

    @GetMapping("/new")
    public String postForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("actionUrl", "/posts/new");
        return "post/post-form";
    }

    /**
     * 웹 애플리케이션에서 도메인 객체(Member)와 세션 객체(SessionMember)를 분리했을 때 자주 마주치는 상황
     *
     */
    @PostMapping("/new")
    public String createPost(@ModelAttribute PostCreateRequest postCreateRequest, HttpSession session) {
        SessionMember loginSessionMember = (SessionMember) session.getAttribute("loginMember");
        Member loginMember = memberService.findById(loginSessionMember.getId());

        Post post = new Post();
        post.setTitle(postCreateRequest.getTitle());
        post.setContent(postCreateRequest.getContent());
        post.setAuthor(loginMember);

        postService.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable Long postId,
                       @RequestParam(value = "edit", required = false) Long editingCommentId,
                       Model model) {
        Post post = postService.findById(postId);
        List<Comment> comments = commentService.findByPostId(postId);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("editingCommentId", editingCommentId);
        return "post/post";
    }

    @GetMapping("/{postId}/edit")
    public String editForm(@PathVariable Long postId, Model model, HttpSession session) {
        Post post = postService.findById(postId);
        Member author = post.getAuthor();
        SessionMember loginMember = (SessionMember) session.getAttribute("loginMember");

        if (loginMember == null || !author.getId().equals(loginMember.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to edit this post");
        }
        model.addAttribute("post", post);
        model.addAttribute("actionUrl", "/posts/" + postId + "/edit");
        return "post/post-form";
    }

    @PostMapping("/{postId}/edit")
    public String updatePost(@PathVariable Long postId, @ModelAttribute PostUpdateRequest postUpdateRequest) {
        Post post = postService.findById(postId);
        post.setTitle(postUpdateRequest.getTitle());
        post.setContent(postUpdateRequest.getContent());
        postService.save(post);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable Long postId, HttpSession session) {
        Post post = postService.findById(postId);
        Member author = post.getAuthor();
        SessionMember loginMember = (SessionMember) session.getAttribute("loginMember");

        if (loginMember == null || !author.getId().equals(loginMember.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this post");
        }
        postService.delete(postId);
        return "redirect:/posts";
    }
}
