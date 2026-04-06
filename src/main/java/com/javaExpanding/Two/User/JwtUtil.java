package com.javaExpanding.Two.User;

//import com.example.hackerton_be.User.Dto.UserLoginDto;
//import com.example.hackerton_be.User.Login.CustomUserDetails;
import com.javaExpanding.Two.User.Database.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    static final String SECRET_KEY = "jwtpassword123jwtpassword123jwtpassword123jwtpassword123jwtpassword"; // 원래는 공유하면 안 됨
    static final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    // JWT 만들어주는 함수
    public String createAccessToken(String email, String role, String position) {
        long accessTokenValidTime = 20 * 60 * 1000L; // 20분
        return Jwts.builder()
                .setSubject(email)
                .claim("position", position)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 Role 정보만 쏙 뽑아오는 메서드 (권한 체크용)
    public String getRole(String token) {
        return extractToken(token).get("role", String.class);
    }

    public String getPosition(String token) {
        return extractToken(token).get("position", String.class);
    }

    // JWT 리프레시 토큰
    public String createRefreshToken(String email) {
        long refreshTokenValidTime = 40 * 60 * 1000L; // 40분

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 까주는 함수
    public Claims extractToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }
}
