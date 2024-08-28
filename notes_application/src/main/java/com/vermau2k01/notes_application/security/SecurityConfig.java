package com.vermau2k01.notes_application.security;

import com.vermau2k01.notes_application.entity.AppRole;
import com.vermau2k01.notes_application.entity.Role;
import com.vermau2k01.notes_application.entity.User;
import com.vermau2k01.notes_application.repository.RoleRepository;
import com.vermau2k01.notes_application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.time.LocalDate;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/auth/public/**"))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/csrf").permitAll()
                        .anyRequest().authenticated())
                .formLogin(withDefaults())
                .httpBasic(withDefaults());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;

    }




//    @Bean
//    public CommandLineRunner commandLineRunner(RoleRepository roleRepository,
//                                               PasswordEncoder passwordEncoder,
//                                               UserRepository userRepository) {
//
//        return args -> {
//
//            Role userRole = roleRepository
//                    .findByRoleName(AppRole.ROLE_USER)
//                    .orElseGet(()->roleRepository
//                            .save(new Role(AppRole.ROLE_USER)));
//
//            Role adminRole = roleRepository
//                    .findByRoleName(AppRole.ROLE_ADMIN)
//                    .orElseGet(()->roleRepository
//                            .save(new Role(AppRole.ROLE_ADMIN)));
//
//
//
//            if(!userRepository.existsByEmail("user@example.com"))
//            {
//                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
//                user1.setAccountNonLocked(false);
//                user1.setAccountNonExpired(true);
//                user1.setCredentialsNonExpired(true);
//                user1.setEnabled(true);
//                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
//                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
//                user1.setTwoFactorEnabled(false);
//                user1.setSignUpMethod("email");
//                user1.setRole(userRole);
//                userRepository.save(user1);
//            }
//
//            if(!userRepository.existsByEmail("admin@example.com"))
//            {
//                User user = new User("admin", "admin@example.com", passwordEncoder.encode("password2"));
//                user.setAccountNonLocked(false);
//                user.setAccountNonExpired(true);
//                user.setCredentialsNonExpired(true);
//                user.setEnabled(true);
//                user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
//                user.setAccountExpiryDate(LocalDate.now().plusYears(1));
//                user.setTwoFactorEnabled(false);
//                user.setSignUpMethod("email");
//                user.setRole(adminRole);
//                userRepository.save(user);
//// This Java code configures Spring Security for a web application, implementing role-based access control, password encryption, and initial user creation.
//            }
//
//        };
//    }



}
