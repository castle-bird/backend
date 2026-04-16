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
- [프로젝트 구조](#프로젝트-구조)
- [주요 설계 결정](#주요-설계-결정)
- [인증/인가 플로우](#인증인가-플로우)

---

## 주요 기능

| 도메인 | 기능 |
|-----------------------|------------------------------------------|
| **인증 (Auth)** | JWT 로그인, Refresh Token 발급, 갱신, 폐기 |
| **직원 (Employee)** | 직원 등록, 조회, 수정, Soft Delete, 부서 배정, 직급 변경 |
| **부서 (Department)** | 부서 생성, 조회, 상태 유지 |
| **급여 (Salary)** | 기본급 설정, 보너스 등록 및 수정 |
| **예약 (Reservation)** | 회의실 등록, 예약 생성 및 취소, 중복 예약 방지 |
| **알림 (Notification)** | 예약 취소, 급여 변경, 조직 변경 공지 알림 |

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

## 프로젝트 구조

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
- **Redis 사용**: Refresh Token 저장과 캐시 관리를 Redis로 처리합니다. 또한 사용자별 최대 3개 세션을 허용하는 로직을 Redis ZSet으로 구현하여 효율적으로 관리합니다.
- **멀티 세션 Refresh Token 관리**: 사용자당 최대 3개의 Refresh Token 세션을 허용하며, 초과 시 가장 오래된 세션을 제거합니다.
- **배치 최적화**: JDBC batch 옵션과 fetch size 설정으로 대량 처리 성능을 보완합니다.

---

## 인증/인가 플로우

### 직원 등록 (관리자)
관리자가 직접 직원 계정을 생성하는 ERP 도메인 방식입니다. 직원이 스스로 가입하지 않습니다.

1. 관리자가 `POST /auth/signup`으로 직원 정보(이름, 이메일, 권한, 직급, 부서)를 전달합니다.
2. Controller에서 요청 유효성을 검증한 뒤 Service에 등록을 위임합니다.
3. Service에서 이메일 중복·부서 존재 여부를 확인합니다.
4. 사원번호(입사일 + 당일 입사자 순번)를 자동 생성합니다.
5. 임시 비밀번호(영문·숫자·특수문자 포함 12자)를 자동 생성하고 `BCryptPasswordEncoder`로 암호화하여 저장합니다.
6. 직원의 `passwordChangeRequired` 플래그를 `true`로 설정합니다.
7. 응답으로 이메일과 평문 임시 비밀번호를 반환합니다. (관리자가 직원에게 별도 전달)

> 토큰은 이 단계에서 발급하지 않습니다.

#### 흐름요약
```text
Admin -> AuthController.createEmployee()
      -> AuthService.createEmployee()
      -> EmployeeRepository / DepartmentRepository
      -> generateTemporaryPassword() + PasswordEncoder
      -> Response(email + temporaryPassword)
```

---

### 로그인
1. 직원이 `POST /auth/login`으로 이메일과 비밀번호(최초 로그인 시 임시 비밀번호)를 전달합니다.
2. Controller가 요청 형식을 검증하고 Service로 전달합니다.
3. Service에서 직원을 조회하고 비밀번호를 검증합니다.
4. 검증 성공 시 Access Token/Refresh Token을 발급하고 Refresh Token을 Redis에 저장합니다.
5. 임시 비밀번호로 로그인한 경우 응답에 `passwordChangeRequired: true`가 포함됩니다. 클라이언트는 이를 확인하여 비밀번호 변경을 유도해야 합니다.
6. 실패 시 인증 오류를 반환하고 토큰은 발급하지 않습니다.

#### 흐름요약
```text
Client -> AuthController.login()
       -> AuthService.login()
       -> EmployeeRepository + PasswordEncoder
       -> JwtProvider + RefreshTokenRedisRepository
       -> Response(Access Token + Refresh Token Cookie + passwordChangeRequired)
```

---

### 토큰 갱신
1. 클라이언트가 `POST /auth/refresh`를 호출하고 쿠키의 Refresh Token을 전달합니다.
2. Controller가 Refresh Token을 추출해 Service에 전달합니다.
3. Service가 토큰 서명/만료/타입/저장 상태를 확인합니다.
4. 검증 완료 시 새 Access Token과 Refresh Token을 발급합니다.
5. 기존 Refresh Token은 정리하고 새 Refresh Token을 Redis에 저장한 뒤 응답을 반환합니다.

#### 흐름요약
```text
Client -> AuthController.refreshToken()
       -> AuthService.refreshToken()
       -> JwtProvider(검증/재발급)
       -> RefreshTokenRedisRepository(교체 저장)
       -> Response(새 Access Token + Refresh Token Cookie)
```

---

### 로그아웃
1. 인증된 사용자가 `POST /auth/logout`을 호출합니다.
2. 인증 컨텍스트에서 사용자 정보를 확인합니다.
3. Service가 Redis에 저장된 해당 사용자의 Refresh Token(또는 현재 세션 토큰)을 삭제합니다.
4. 클라이언트 쿠키의 Refresh Token을 무효화합니다.
5. 이후 토큰 갱신 요청은 실패하게 됩니다.

#### 흐름요약
```text
Client -> JwtFilter(인증 확인)
       -> AuthController.logout()
       -> AuthService.logout()
       -> RefreshTokenRedisRepository.delete(...)
       -> Response(로그아웃 완료)
```
