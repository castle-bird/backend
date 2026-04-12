[![codecov](https://codecov.io/gh/castle-bird/backend/graph/badge.svg?token=IW9WHKXOXQ)](https://codecov.io/gh/castle-bird/backend)
![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1?logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-7-DC382D?logo=redis)

# ERP Backend (MVP)

사내 인원 관리, 급여 관리, 회의실 예약, 알림을 통합한 **ERP MVP 백엔드 서버**입니다.

---

## 목차

1. [주요 기능](#주요-기능)
2. [기술 스택](#기술-스택)
3. [도메인 구조](#도메인-구조)
4. [아키텍처](#아키텍처)

---

## 주요 기능

| 도메인 | 기능 |
|--------|------|
| **인증 (Auth)** | JWT 로그인, Refresh Token 발급·갱신·폐기 |
| **직원 (Employee)** | 직원 등록·조회·수정, Soft Delete, 부서 배정, 직급 변경 |
| **부서 (Department)** | 부서 생성·조회, 부서장 지정 |
| **급여 (Salary)** | 월급·연봉·보너스 등록·수정 |
| **회의실 예약 (Reservation)** | 회의실 등록, 예약 생성·취소, 중복 예약 방지 |
| **알림 (Notification)** | 예약 취소·급여 변경·직급 변경 시 알림, 전사 공지 |

---

## 기술 스택

### Backend
- **Java 17** / **Spring Boot 3.5**
- **Spring Security** — JWT 기반 인증·인가
- **Spring Data JPA** + **QueryDSL** — ORM 및 동적 쿼리
- **MapStruct** — Entity ↔ DTO 매핑

### Database
- **PostgreSQL 17** — 메인 데이터베이스
- **Redis 7** — Refresh Token 저장, 캐시 (Lettuce Connection Pool)
- **Flyway** — DB 스키마 버전 관리

### 인프라 / 운영
- **Docker Compose** — 로컬 개발 환경 (PostgreSQL + Redis)
- **AWS ECS / ALB** — 운영 배포 (`/actuator/health` 헬스체크)
- **GitHub Actions** — CI/CD 자동화
- **JaCoCo + Codecov** — 코드 커버리지 측정

### 문서
- **SpringDoc (OpenAPI 3)** — Swagger UI (`/swagger-ui.html`, 개발 환경 전용)

---

## 도메인 구조

```
src/main/java/io/project/backend/
├── global/
│   ├── config/       # RedisConfig, JpaConfig 등 공통 설정
│   └── entity/       # BaseEntity, BaseTimeEntity
└── domain/
    ├── auth/          # RefreshToken Entity·Repository
    ├── employee/      # Employee·Department·Salary Entity·Repository
    ├── reservation/   # MeetingRoom·RoomReservation Entity·Repository
    └── notification/  # Notification Entity·Repository
```

---

## 아키텍처

```
Client
  │
  ▼
[Spring Security Filter]  ← JWT 검증
  │
  ▼
Controller  →  Service  →  Repository  →  PostgreSQL
                  │
                  └──────────────────────→  Redis (Cache / RefreshToken)
```

### 주요 설계 결정

- **Soft Delete**: `employees` 테이블의 `is_deleted` / `deleted_at` 컬럼으로 논리 삭제 처리
- **중복 예약 방지**: PostgreSQL `EXCLUDE USING gist` + Optimistic Lock (`version`)으로 동시성 제어
- **Redis 캐시**: 캐시별 TTL을 `application.yaml`에서 선언적으로 관리 (`app.cache.redis.caches.*`)
- **배치 최적화**: `reWriteBatchedInserts=true` + `batch_size: 20` + `default_batch_fetch_size: 100`

---
