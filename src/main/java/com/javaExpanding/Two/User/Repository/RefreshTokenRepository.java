package com.javaExpanding.Two.User.Repository;

import com.javaExpanding.Two.User.Database.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // 이메일로 리프레시 토큰 찾기
    Optional<RefreshToken> findByUserEmail(String userEmail);
}
