package com.vermau2k01.notes_application.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private String jwtToken;
    private String username;
    private List<String> roles;

}
