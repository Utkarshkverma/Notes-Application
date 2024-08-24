package com.vermau2k01.notes_application.service;

import com.vermau2k01.notes_application.dto.UserDTO;
import com.vermau2k01.notes_application.entity.User;

import java.util.List;

public interface UserService {

    void updateUserRole(String userId, String roleName);

    List<UserDTO> getUsers();

    UserDTO getUser(String userId);


}
