--------------------------------------------------------------------------------
-- ERP 시스템 초기 스키마 정의 (PostgreSQL)
--------------------------------------------------------------------------------

-- ENUM 타입 정의
CREATE TYPE employee_role AS ENUM ('ADMIN', 'MANAGER', 'EMPLOYEE');
CREATE TYPE reservation_status AS ENUM ('CONFIRMED', 'CANCELLED');
CREATE TYPE notification_type AS ENUM (
    'RESERVATION_CONFIRMED',
    'RESERVATION_CANCELLED',
    'FULL_NOTIFICATION'
    );

-- btree_gist 확장 (EXCLUDE USING gist 사용을 위해 필요)
CREATE EXTENSION IF NOT EXISTS btree_gist;

--------------------------------------------------------------------------------
-- departments
--------------------------------------------------------------------------------
CREATE TABLE departments
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL UNIQUE,
    manager_id INT, -- FK는 employees 생성 후 ALTER로 추가
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

--------------------------------------------------------------------------------
-- employees
--------------------------------------------------------------------------------
CREATE TABLE employees
(
    id              SERIAL PRIMARY KEY,
    employee_number VARCHAR(20)   NOT NULL UNIQUE,
    name            VARCHAR(50)   NOT NULL,
    email           VARCHAR(100)  NOT NULL UNIQUE,
    password        VARCHAR(255)  NOT NULL,
    role            employee_role NOT NULL DEFAULT 'EMPLOYEE',
    position        VARCHAR(50)   NOT NULL,
    department_id   INT           REFERENCES departments (id) ON DELETE SET NULL,
    hire_date       DATE,
    is_deleted      BOOLEAN       NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_deleted_at CHECK (
        (is_deleted = FALSE AND deleted_at IS NULL) OR
        (is_deleted = TRUE AND deleted_at IS NOT NULL)
        )
);

-- 순환 참조 FK: employees 생성 후에 추가 → 부서장때문에 순환참조가 발생해서 FK 나중에 추가함
ALTER TABLE departments
    ADD CONSTRAINT fk_departments_manager
        FOREIGN KEY (manager_id) REFERENCES employees (id) ON DELETE SET NULL;

-- 인덱스
CREATE INDEX idx_employees_active_email ON employees (email) WHERE is_deleted = FALSE;
CREATE INDEX idx_employees_department ON employees (department_id) WHERE is_deleted = FALSE;

--------------------------------------------------------------------------------
-- salaries
--------------------------------------------------------------------------------
CREATE TABLE salaries
(
    id           SERIAL PRIMARY KEY,
    employee_id  INT            NOT NULL REFERENCES employees (id) ON DELETE RESTRICT,
    month_salary NUMERIC(15, 2) NOT NULL CHECK (month_salary >= 0),    -- 월급
    year_salary  NUMERIC(15, 2) NOT NULL CHECK (year_salary >= 0),     -- 연봉
    bonus        NUMERIC(15, 2) NOT NULL DEFAULT 0 CHECK (bonus >= 0), -- 보너스
    payment_date DATE,
    created_at   TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

--------------------------------------------------------------------------------
-- meeting_rooms
--------------------------------------------------------------------------------
CREATE TABLE meeting_rooms
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL UNIQUE,
    location   VARCHAR(200) NOT NULL,
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

--------------------------------------------------------------------------------
-- room_reservations
--------------------------------------------------------------------------------
CREATE TABLE room_reservations
(
    id          SERIAL PRIMARY KEY,
    room_id     INT                NOT NULL REFERENCES meeting_rooms (id) ON DELETE RESTRICT,
    employee_id INT                NOT NULL REFERENCES employees (id) ON DELETE RESTRICT,
    purpose     VARCHAR(255)       NOT NULL,
    start_time  TIMESTAMPTZ        NOT NULL,
    end_time    TIMESTAMPTZ        NOT NULL,
    status      reservation_status NOT NULL DEFAULT 'CONFIRMED',
    version     INT                NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ        NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ        NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_reservation_time CHECK (end_time > start_time),

    CONSTRAINT no_time_overlap EXCLUDE USING gist (
        room_id WITH =,                               -- Room ID가 같을 때
        TSTZRANGE(start_time, end_time, '[)') WITH && -- 예약 시간 범위가 겹치는지
        ) WHERE (status = 'CONFIRMED')
);

CREATE INDEX idx_reservations_room_time
    ON room_reservations (room_id, start_time, end_time)
    WHERE status = 'CONFIRMED';

CREATE INDEX idx_reservations_employee
    ON room_reservations (employee_id, start_time DESC);

--------------------------------------------------------------------------------
-- notifications
--------------------------------------------------------------------------------
CREATE TABLE notifications
(
    id         SERIAL PRIMARY KEY,
    sender_id  INT               REFERENCES employees (id) ON DELETE SET NULL,
    type       notification_type NOT NULL,
    title      VARCHAR(200)      NOT NULL,
    message    TEXT,
    created_at TIMESTAMPTZ       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_notifications_sender_created
    ON notifications (sender_id, created_at DESC);

--------------------------------------------------------------------------------
-- refresh_tokens
--------------------------------------------------------------------------------
CREATE TABLE refresh_tokens
(
    id          SERIAL PRIMARY KEY,
    employee_id INT          NOT NULL REFERENCES employees (id) ON DELETE CASCADE,
    token       VARCHAR(512) NOT NULL UNIQUE,
    expires_at  TIMESTAMPTZ  NOT NULL,
    is_revoked  BOOLEAN      NOT NULL DEFAULT FALSE,
    replaced_by INT          REFERENCES refresh_tokens (id) ON DELETE SET NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);