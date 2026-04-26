--------------------------------------------------------------------------------
-- Dashboard 집계 테이블
--
-- 목적: API 요청마다 실시간 집계를 수행하는 대신, Batch가 주기적으로
--       미리 집계한 결과를 여기에 저장해 두고 Dashboard API는 단순 SELECT만 실행한다.
--
-- 갱신 주기:
--   dashboard_daily_stats      → 매일 자정 (직원 현황)
--   dashboard_department_stats → 매일 자정 (부서별 인원 현황)
--
-- 당일 예약 현황(today_confirmed 등)은 Batch 부적합 → API 호출 시 실시간 조회
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
-- dashboard_daily_stats
-- 직원 현황 (자주 변하지 않아 Batch에 적합)
--------------------------------------------------------------------------------
CREATE TABLE dashboard_daily_stats
(
    id                   BIGSERIAL PRIMARY KEY,
    snapshot_date        DATE        NOT NULL UNIQUE,    -- 집계 기준 날짜 (하루 1건)

    total_employees      INT         NOT NULL DEFAULT 0, -- 전체 활성 직원 수
    new_hires_this_month INT         NOT NULL DEFAULT 0, -- 해당 월 신규 입사자 수
    manager_count        INT         NOT NULL DEFAULT 0, -- MANAGER 직원 수
    employee_count       INT         NOT NULL DEFAULT 0, -- EMPLOYEE 직원 수

    created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

--------------------------------------------------------------------------------
-- dashboard_department_stats
-- 부서별 직원 수, 부서장 이름 등 부서별 현황을 일별로 집계
--------------------------------------------------------------------------------
CREATE TABLE dashboard_department_stats
(
    id              BIGSERIAL PRIMARY KEY,
    snapshot_date   DATE         NOT NULL,
    department_name VARCHAR(100) NOT NULL,           -- departments.name
    employee_count  INT          NOT NULL DEFAULT 0, -- 해당 부서 활성 직원 수
    manager_name    VARCHAR(50),                     -- 부서장 이름, 미지정이면 NULL

    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_dept_stats_date_dept UNIQUE (snapshot_date, department_name)
);
