--------------------------------------------------------------------------------
-- 기존 데이터 이관 없이 직급을 별도 테이블 + FK 구조로 전환한다.
--------------------------------------------------------------------------------
-- 직급 마스터 테이블 생성
CREATE TABLE employee_positions
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL UNIQUE,
    sort_order SMALLINT    NOT NULL UNIQUE
);

-- 기존 문자열 직급 컬럼 제거
ALTER TABLE employees
    DROP COLUMN position;

-- employees 테이블에 신규 FK 컬럼 추가
ALTER TABLE employees
    ADD COLUMN position_id BIGINT;

-- 신규 구조에서 position_id는 필수값
ALTER TABLE employees
    ALTER COLUMN position_id SET NOT NULL;

-- employees.position_id -> employee_positions.id 외래키 제약 추가
ALTER TABLE employees
    ADD CONSTRAINT fk_employees_position
        FOREIGN KEY (position_id) REFERENCES employee_positions (id) ON DELETE RESTRICT;
