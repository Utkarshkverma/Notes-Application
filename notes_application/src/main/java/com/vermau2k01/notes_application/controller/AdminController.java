package com.vermau2k01.notes_application.controller;

import com.vermau2k01.notes_application.dto.UserDTO;
import com.vermau2k01.notes_application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/get-users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("get-users");
        return new ResponseEntity<>(userService.getUsers(),
                HttpStatus.OK);
    }

    @PutMapping("/update-role")
    public ResponseEntity<String> updateUserRole(@RequestParam String userId,
                                                 @RequestParam String roleName) {
        userService.updateUserRole(userId, roleName);
        return ResponseEntity.ok("User role updated");
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        return new ResponseEntity<>(userService.getUser(id),
                HttpStatus.OK);
    }


}
