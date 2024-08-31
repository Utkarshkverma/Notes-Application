package com.vermau2k01.notes_application.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
public class LoginResponse {

    private String jwtToken;

    private String username;
    private List<String> roles;

    public LoginResponse(String username, List<String> roles, String jwtToken) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
    }


}
