package com.javaExpanding.Two.User.Service;

import com.javaExpanding.Two.Admin.Database.Admin;
import com.javaExpanding.Two.Admin.Repository.AdminRepository;
import com.javaExpanding.Two.User.Database.Users;
import com.javaExpanding.Two.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository; // 👈 관리자 리포지토리 주입

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        // 1. 먼저 일반 유저 테이블에서 찾기
        Optional<Users> user = userRepository.findByUserEmail(identifier);
        if (user.isPresent()) {
            return new CustomUserDetails(user.get());
        }

        // 2. 유저가 없다면 관리자 테이블에서 찾기
        Admin admin = adminRepository.findByAdminIdOrAdminEmail(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException("해당 계정을 찾을 수 없습니다: " + identifier));

        // 3. 관리자 정보를 CustomUserDetails로 변환해서 반환
        // (참고: CustomUserDetails 클래스가 Admin 객체도 수용할 수 있게 생성자가 수정되어야 합니다!)
        return new CustomUserDetails(admin);
    }
}