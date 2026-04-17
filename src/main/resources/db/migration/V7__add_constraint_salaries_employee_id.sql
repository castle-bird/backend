-- salaries: 직원당 급여 정보는 1건만 허용
ALTER TABLE salaries
    ADD CONSTRAINT unique_employee_id UNIQUE (employee_id);
