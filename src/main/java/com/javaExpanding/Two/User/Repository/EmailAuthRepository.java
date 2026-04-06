package com.javaExpanding.Two.User.Repository;

import com.javaExpanding.Two.User.Database.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    Optional<EmailAuth> findByEmail(String email);
    void deleteByEmail(String email); // 새로운 번호 발송 시 기존 번호 삭제용
}