# 🏫 백석대학교 통합 예약 시스템 (Two-Project)
> **Spring Boot 기반의 사용자 및 관리자 통합 인증 및 예약 관리 시스템**

본 프로젝트는 백석대학교 학생들을 위한 예약 서비스와 유저/관리자를 위한 관리 기능을 통합하여 제공하는 백엔드 서버입니다.
<br><small>Api 명세서 http://1.226.83.69:8080/swagger</small>
---

## 🚀 주요 기능 (Features)

### 🔐 인증 및 보안 (Authentication)
* **Dual-Role 시스템(JWT)**: `position` 클레임을 활용하여 일반 사용자(`User`)와 관리자(`Admin`)를 명확히 구분합니다.
* **JWT 기반 인증**: Access Token(20분)과 Refresh Token(40분)을 활용한 보안 세션을 유지합니다.
* **이메일 인증**: `@bu.ac.kr` 학교 공식 계정 소유 여부를 확인하는 SMTP 인증 로직을 구현하였습니다.
* **SSH 터널링**: 외부 데이터베이스 연결 시 보안 터널을 형성하여 데이터 노출을 차단합니다. <small>단, 민감 정보이므로 깃허브에는 안 올림</small>

### 👥 사용자 관리 (User Management)
* **교차 중복 체크**: 유저와 관리자 테이블 간의 이메일 중복 가입을 상호 체크하여 데이터 무결성을 보장합니다.
* **실시간 상태 체크**: 페이지 진입 시마다 `/api/public_auh/check_status`를 통해 토큰 유효성 및 권한을 검증합니다.

---

## 🛠 기술 스택 (Tech Stack)

| 구분               | 상세 내용                            |
|:-----------------|:---------------------------------|
| **Framework**    | Spring Boot 3.4.1                |
| **Swagger**      | V3 2.7.0                         |
| **Java Version** | Java 21                          |
| **Database**     | MySQL 8.0.40 (via SSH Tunneling) |
| **ORM**          | Spring Data JPA (Hibernate)      |
| **Security**     | Spring Security 7.0, JWT (JJWT)  |
| **API Docs**     | Swagger / OpenAPI 3.0            |

---

## 📂 프로젝트 구조 (Package Structure)

```text
src/main/java/com.javaExpanding.Two
├── Admin             # 관리자 도메인 (회원가입, 로그인, 로그아웃)
├── User              # 사용자 도메인 (회원가입, 로그인, 로그아웃)
├── Facility          # 시설물 관련 도메인
├── Facility_Time     # 시설물 운영 도메인
├── PublucAuth        # 공통 인증 (이메일 발송, 인증 확인, 토큰 갱신, 상태 체크)
├── Policy            # Security Config 및 JWT Filter 설정
├── Error             # 커스텀 예외 처리
└── TwoApplication    # 메인 실행 클래스