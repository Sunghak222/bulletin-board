package com.sunghak.board.dto;

import com.sunghak.board.entity.Member;

public class SessionMember {
    private final Long id;
    private final String name;
    private final String email;
    private final String role;

    public SessionMember(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.role = member.getRole();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
