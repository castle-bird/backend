[![codecov](https://codecov.io/gh/castle-bird/backend/graph/badge.svg?token=IW9WHKXOXQ)](https://codecov.io/gh/castle-bird/backend)
![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1?logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-7-DC382D?logo=redis)

# ERP Backend (MVP)

사내 인원 관리, 급여 관리, 회의실 예약, 알림 기능을 포함한 ERP MVP 백엔드 서버입니다.

---

## 목차

- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [도메인 구조](#도메인-구조)
- [주요 설계 결정](#주요-설계-결정)
- [인증/인가 플로우](#인증인가-플로우)

---

## 주요 기능

| 도메인                   | 기능                                       |
|-----------------------|------------------------------------------|
| **인증 (Auth)**         | JWT 로그인, Refresh Token 발급, 갱신, 폐기        |
| **직원 (Employee)**     | 직원 등록, 조회, 수정, Soft Delete, 부서 배정, 직급 변경 |
| **부서 (Department)**   | 부서 생성, 조회, 상태 유지                         |
| **급여 (Salary)**       | 기본급, 수당, 보너스 등록 및 수정                     |
| **예약 (Reservation)**  | 회의실 등록, 예약 생성 및 취소, 중복 예약 방지             |
| **알림 (Notification)** | 예약 취소, 급여 변경, 조직 변경, 공지 알림               |

---

## 기술 스택

### Backend

- **Java 17** / **Spring Boot 3.5**
- **Spring Security** - JWT 기반 인증/인가
- **Spring Data JPA** - 기본 ORM
- **MapStruct** - Entity / DTO 매핑

### Database

- **PostgreSQL 17** - 메인 데이터베이스
- **Redis 7** - Refresh Token 저장, 캐시
- **Flyway** - DB 스키마 버전 관리

### 인프라 / 운영

- **Docker** - 컨테이너화
- **AWS** - 운영 및 배포
- **GitHub Actions** - CI/CD 자동화
- **JaCoCo + Codecov** - 코드 커버리지 측정

### 문서

- **SpringDoc (OpenAPI 3)** - Swagger UI (`/swagger-ui.html`, 개발 환경 기준)

---

## 도메인 구조

Layered Architecture를 기반으로 도메인별 패키지를 구성했습니다.

```text
src/main/java/io/project/backend/
├── global/
│   ├── config/         # RedisConfig, SecurityConfig 등 공통 설정
│   ├── entity/         # BaseEntity, BaseTimeEntity
│   ├── exception/      # GlobalExceptionHandler, CustomException
│   ├── response/       # ApiResponse, ErrorResponse
│   └── security/       # JWT, 인증/인가 관련 구성
├── infra/
│   └── s3/             # S3Client, S3Service
└── domain/
    ├── auth/           # 인증, RefreshToken Redis 처리
    ├── employee/       # Employee, Department, Salary
    ├── reservation/    # MeetingRoom, RoomReservation
    └── notification/   # Notification
```

---

### 주요 설계 결정

- **Soft Delete**: `employees` 테이블의 `is_deleted`, `deleted_at` 컬럼으로 논리 삭제를 처리합니다.
- **중복 예약 방지**: PostgreSQL 제약 조건과 애플리케이션 레벨 검증을 함께 사용합니다.
- **Redis 사용**: Refresh Token 저장과 캐시 관리를 Redis로 처리합니다.
- **배치 최적화**: JDBC batch 옵션과 fetch size 설정으로 대량 처리 성능을 보완합니다.

---

## 인증/인가 플로우

### 회원가입

1. 클라이언트가 `POST /auth/signup` 요청을 보냅니다.
2. `AuthController.signup()`
    - 요청 본문을 `SignupRequest`로 받습니다.
    - `@Valid`로 기본 유효성을 검증합니다.
    - 회원가입 처리를 `AuthService.signup()`에 위임합니다.
3. `AuthServiceImpl.signup()`
    - 이메일 중복 여부를 확인합니다.
    - 부서 존재 여부를 확인합니다.
    - 입사일 기준 사번을 생성합니다.
    - 비밀번호를 암호화합니다.
    - `Employee` 엔티티를 생성하고 저장합니다.
    - `JwtProvider`로 Access Token과 Refresh Token을 생성합니다.
    - `RefreshTokenRedisRepository`를 통해 Refresh Token을 Redis에 저장합니다.
4. `JwtProvider`
    - 사용자 ID를 기준으로 JWT를 생성합니다.
    - Access Token에는 권한 정보를 포함합니다.
    - Refresh Token은 재발급용 최소 정보만 담아 생성합니다.
5. `RefreshTokenRedisRepository`
    - Redis에 Refresh Token을 저장합니다.
    - 현재 구현 기준 키 형식은 `refresh_token:{userId}` 입니다.
    - TTL은 Refresh Token 만료 시간과 동일하게 설정합니다.
6. `AuthController.signup()`
    - Access Token은 응답 바디(`AuthResponse`)로 반환합니다.
    - Refresh Token은 `HttpOnly Cookie`로 반환합니다.
    - 응답 상태 코드는 `201 Created`입니다.
7. 클라이언트는 Access Token과 Refresh Token을 받아 인증 상태를 유지합니다.

### 흐름 요약

```text
Client
  -> AuthController.signup()
  -> AuthServiceImpl.signup()
      -> EmployeeRepository / DepartmentRepository
      -> JwtProvider
      -> RefreshTokenRedisRepository
  -> AuthController
Client
```

### 로그인

로그인 플로우는 회원가입과 거의 유사하지만,
기존 사용자 검증과 비밀번호 확인이 추가됩니다. 로그인 성공 시에도 Access Token과 Refresh Token이 발급되고 저장됩니다.

거의 유사한 이유는 현재 회원가입 완료 → 즉시 로그인 상태로 전환되는 시나리오를 지원하기 때문입니다. 
따라서 회원가입과 로그인 모두에서 동일한 토큰 발급 및 저장 로직이 재사용됩니다.

```text
Client
  -> AuthController.login()
  -> AuthServiceImpl.login()
      -> EmployeeRepository / PasswordEncoder
      -> JwtProvider
      -> RefreshTokenRedisRepository
  -> AuthController
Client
```