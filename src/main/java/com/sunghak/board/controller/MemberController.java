package com.sunghak.board.controller;

import com.sunghak.board.dto.MemberLoginRequest;
import com.sunghak.board.dto.MemberRegisterRequest;
import com.sunghak.board.dto.SessionMember;
import com.sunghak.board.entity.Member;
import com.sunghak.board.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RequestMapping("/members")
@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public String list(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "member/member-list";
    }
    @GetMapping("/register")
    public String registerForm() {
        return "member/register-form";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("member") MemberRegisterRequest memberRegisterRequest) {

        String email = memberRegisterRequest.getEmail();
        String password = memberRegisterRequest.getPassword();
        String name = memberRegisterRequest.getName();

        Member member = new Member();
        member.setEmail(email);
        member.setPassword(password);
        member.setName(name);
        member.setRole("member");

        memberService.save(member);

        return "redirect:/posts";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "member/login-form";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginRequest memberLoginRequest, Model model, HttpSession session) {

        String email = memberLoginRequest.getEmail();
        String password = memberLoginRequest.getPassword();
        try {
            Member member = memberService.findByEmail(email);
            if (!member.getPassword().equals(memberLoginRequest.getPassword())) {
                model.addAttribute("loginError", "Email or password is incorrect");
                return "member/login-form";
            }
            session.setAttribute("loginMember", new SessionMember(member));
        }
        catch (EntityNotFoundException e) {
            model.addAttribute("loginError", "Email or password is incorrect");
            return "member/login-form";
        }
        return "redirect:/posts";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        log.info(">>> loginMember: {}", session.getAttribute("loginMember"));
        session.invalidate();
        return "redirect:/posts";
    }
}
