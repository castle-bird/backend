-- salaries: bonus 컬럼 제거, payment_date(DATE) -> payment_day(SMALLINT) 변경
-- salaries 테이블 자체가 월급 정보 표의 역할을 하기 때문에
-- payment_day 컬럼은 월급이 지급되는 날짜를 나타내는 것으로 변경
-- Bonus는 매월 지급 내용과는 거리가 있다고 판단하여 제거
ALTER TABLE salaries
    DROP COLUMN bonus,
    DROP COLUMN payment_date,
    ADD COLUMN payment_day SMALLINT CHECK (payment_day BETWEEN 1 AND 31);
