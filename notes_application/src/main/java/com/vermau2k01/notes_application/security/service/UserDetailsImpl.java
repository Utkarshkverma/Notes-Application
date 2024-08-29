package com.vermau2k01.notes_application.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vermau2k01.notes_application.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {

    private String id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean is2faEnabled;

    public UserDetailsImpl(String id,
                           String username,
                           String email,
                           String password,
                           Collection<? extends GrantedAuthority> authorities,
                           boolean is2faEnabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.is2faEnabled = is2faEnabled;
    }

    public static UserDetailsImpl build(User user) {

        GrantedAuthority authority =
                new SimpleGrantedAuthority(user.getRole()
                .getRoleName()
                .name());

        return new UserDetailsImpl(user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                List.of(authority),
                user.isTwoFactorEnabled());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
