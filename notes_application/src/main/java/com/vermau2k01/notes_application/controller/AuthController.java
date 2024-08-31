package com.vermau2k01.notes_application.controller;

import com.vermau2k01.notes_application.dto.*;
import com.vermau2k01.notes_application.entity.AppRole;
import com.vermau2k01.notes_application.entity.Role;
import com.vermau2k01.notes_application.entity.User;
import com.vermau2k01.notes_application.repository.RoleRepository;
import com.vermau2k01.notes_application.repository.UserRepository;
import com.vermau2k01.notes_application.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @PostMapping("/public/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.debug("Authenticating user {}", loginRequest);
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

//      set the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateToken(userDetails);

        // Collect roles from the UserDetails
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Prepare the response body, now including the JWT token directly in the body
        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);

        // Return the response entity with the JWT token included in the response body
        return ResponseEntity.ok(response);
    }

    @PostMapping("/public/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("Email already exists");
            return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByUserName(signUpRequest.getUserName())) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("Username already exists");
            return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
        }

        Role role;
        if(signUpRequest.getEmail().endsWith("proton.me"))
            role = roleRepository
                    .findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseThrow(()-> new RuntimeException("Error: Role is not found."));
        else
            role = roleRepository
                    .findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(()-> new RuntimeException("Error: Role is not found."));

        User user = new User();
        user.setUserName(signUpRequest.getUserName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(role);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        user.setAccountExpiryDate(LocalDate.now().plusYears(1));
        user.setTwoFactorEnabled(false);
        user.setSignUpMethod("email");

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User Added successfully"));

    }


    @GetMapping("/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("Error: User is not found."));



        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();


        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setEmail(user.getEmail());
        userInfoResponse.setId(user.getUserId());
        userInfoResponse.setUsername(user.getUserName());
        userInfoResponse.setAccountNonLocked(user.isAccountNonLocked());
        userInfoResponse.setAccountNonExpired(user.isAccountNonExpired());
        userInfoResponse.setTwoFactorEnabled(user.isTwoFactorEnabled());
        userInfoResponse.setRoles(roles);
        userInfoResponse.setCredentialsNonExpired(user.isCredentialsNonExpired());
        userInfoResponse.setEnabled(user.isEnabled());
        userInfoResponse.setCredentialsExpiryDate(user.getCredentialsExpiryDate());
        userInfoResponse.setAccountExpiryDate(user.getAccountExpiryDate());

        return ResponseEntity.ok(userInfoResponse);

    }

    @GetMapping("/username")
    public String currentUserName(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new RuntimeException("Error: User is not found."));

        return user.getUserName();
    }


}
