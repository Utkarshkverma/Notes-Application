package com.vermau2k01.notes_application.repository;

import com.vermau2k01.notes_application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String user1);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
