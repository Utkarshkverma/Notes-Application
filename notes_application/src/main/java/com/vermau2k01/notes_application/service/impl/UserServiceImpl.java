package com.vermau2k01.notes_application.service.impl;

import com.vermau2k01.notes_application.dto.UserDTO;
import com.vermau2k01.notes_application.entity.AppRole;
import com.vermau2k01.notes_application.entity.Role;
import com.vermau2k01.notes_application.entity.User;
import com.vermau2k01.notes_application.repository.RoleRepository;
import com.vermau2k01.notes_application.repository.UserRepository;
import com.vermau2k01.notes_application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public void updateUserRole(String userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        AppRole appRole = AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getUsers() {
        List<User> all = userRepository.findAll();
        return all
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public UserDTO getUser(String userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);

    }

    private UserDTO convertToDto(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.getTwoFactorSecret(),
                user.isTwoFactorEnabled(),
                user.getSignUpMethod(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
