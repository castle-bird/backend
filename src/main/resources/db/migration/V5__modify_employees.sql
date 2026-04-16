--------------------------------------------------------------------------------
-- 사원에 대한 컬럼이 부족해 추가
--------------------------------------------------------------------------------
ALTER TABLE employees
    ADD COLUMN address VARCHAR(255);

ALTER TABLE employees
    ADD COLUMN phone VARCHAR(20);

UPDATE employees
SET address = '미정'
WHERE address IS NULL;

UPDATE employees
SET phone = '미정'
WHERE phone IS NULL;

ALTER TABLE employees
    ALTER COLUMN address SET NOT NULL;

ALTER TABLE employees
    ALTER COLUMN phone SET NOT NULL;;
