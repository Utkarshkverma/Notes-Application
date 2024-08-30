package com.vermau2k01.notes_application;

import com.vermau2k01.notes_application.entity.AppRole;
import com.vermau2k01.notes_application.entity.Role;
import com.vermau2k01.notes_application.entity.User;
import com.vermau2k01.notes_application.repository.RoleRepository;
import com.vermau2k01.notes_application.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class NotesApplication {

	public static void main(String[] args) {

		SpringApplication.run(NotesApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
					.orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

			Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
					.orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

			if (!userRepository.existsByUserName("user1")) {
				User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("user1"));
				user1.setAccountNonLocked(false);
				user1.setAccountNonExpired(true);
				user1.setCredentialsNonExpired(true);
				user1.setEnabled(true);
				user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
				user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
				user1.setTwoFactorEnabled(false);
				user1.setSignUpMethod("email");
				user1.setRole(userRole);
				userRepository.save(user1);
			}

			if (!userRepository.existsByUserName("admin")) {
				User admin = new User("admin", "admin@example.com", passwordEncoder.encode("admin"));
				admin.setAccountNonLocked(true);
				admin.setAccountNonExpired(true);
				admin.setCredentialsNonExpired(true);
				admin.setEnabled(true);
				admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
				admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
				admin.setTwoFactorEnabled(false);
				admin.setSignUpMethod("email");
				admin.setRole(adminRole);
				userRepository.save(admin);
			}
		};


	}
}
