package com.vermau2k01.notes_application.repository;

import com.vermau2k01.notes_application.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
}
