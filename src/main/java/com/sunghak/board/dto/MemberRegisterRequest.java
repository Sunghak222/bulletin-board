package com.sunghak.board.dto;

public class MemberRegisterRequest {

    private final String email;
    private final String password;
    private final String name;

    public MemberRegisterRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
