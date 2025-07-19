package com.sunghak.board.apicontroller;

import com.sunghak.board.dto.MemberLoginRequest;
import com.sunghak.board.dto.MemberRegisterRequest;
import com.sunghak.board.dto.SessionMember;
import com.sunghak.board.entity.Member;
import com.sunghak.board.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("api/members")
@RestController
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> list() {
        List<Member> members = memberService.findAll();
        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("members", members);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody MemberRegisterRequest request) {
        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPassword(request.getPassword());
        member.setName(request.getName());
        member.setRole("member");

        memberService.save(member);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Registration successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody MemberLoginRequest request, HttpSession session) {

        Map<String, String> response = new HashMap<>();

        try {
            Member member = memberService.findByEmail(request.getEmail());

            if (!member.getPassword().equals(request.getPassword())) {
                response.put("status", "error");
                response.put("error", "Invalid Email or Password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            session.setAttribute("loginMember", new SessionMember(member));
            response.put("status", "success");
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        }
        catch (EntityNotFoundException e) {
            response.put("status", "error");
            response.put("message", "Invalid Email or Password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Logout successful");
        return ResponseEntity.ok(response);
    }
}
