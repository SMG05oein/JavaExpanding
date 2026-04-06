package com.javaExpanding.Two.policy;

import com.javaExpanding.Two.User.JwtUtil;
import com.javaExpanding.Two.User.Service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //비번
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 비활성화 (REST API 표준)
                .csrf(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))


                // 2. 세션 STATELESS 설정 (JWT 사용 시 필수)
                // authorizeHttpRequests 블록 밖으로 이동
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                                // 3. Swagger UI 및 API 문서 경로 접근 허용
                                .requestMatchers(
                                        "/swagger",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**"
                                ).permitAll()

                                // 4. 인증/회원가입 API 접근 허용
                                .requestMatchers(
                                        "/api/public_auh/**",
                                        "/api/auth/signup",
                                        "/api/admin/signup",
                                        "/api/auth/login",
                                        "/api/admin/login",
                                        "/api/auth/logout",
                                        "/api/admin/logout"
                                ).permitAll()

                                // 5. 단어 API 접근 허용
                                .requestMatchers(
                                        "/api/word/**"
//                                "api/que/**"
                                ).permitAll()

                                .requestMatchers(
                                        "/api/test/test1",
                                        "/api/test/test2"
                                ).permitAll()

                                // 5. 나머지 모든 요청은 인증 필요
                                .anyRequest().authenticated()
                );

        // 6. JWT 필터 등록
        http.addFilterBefore(
                new com.javaExpanding.Two.User.JwtFilter(jwtUtil, customUserDetailsService),
                UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 🚨 Vercel 배포 시 실제 프런트엔드 도메인을 여기에 추가해야 합니다.
        configuration.setAllowedOrigins(Arrays.asList(
                "https://hoseothon11.vercel.app",
                "http://localhost:3000/"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 쿠키 (리프레시 토큰) 처리를 위해 true로 설정
        configuration.setAllowCredentials(true);

        // JWT Authorization 헤더를 포함하여 모든 헤더를 허용
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 적용
        return source;
    }
}
