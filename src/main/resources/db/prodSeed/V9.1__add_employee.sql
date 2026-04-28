--------------------------------------------------------------------------------
-- 초기 사원 데이터 seed (초기 비밀번호: Test1234!)
-- 최초 로그인 시 비밀번호 변경 강제
--------------------------------------------------------------------------------

CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO employees (employee_number, name, email, password, role, position_id, department_id, hire_date, address, phone, password_change_required)
VALUES
  ('20030201', '김민준',  'minjun.kim@gmail.com',      crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 2, 3, '2020-03-02', '서울시 강남구',        '010-3847-9201', true),
  ('20081701', '이서연',  'seoyeon.lee@gmail.com',     crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 4, 2, '2020-08-17', '서울시 마포구',        '010-5612-3874', true),
  ('21010401', '박지훈',  'jihun.park@gmail.com',      crypt('Test1234!', gen_salt('bf', 10)), 'MANAGER',  7, 3, '2021-01-04', '경기도 성남시 분당구',  '010-9023-4517', true),
  ('21053101', '최수아',  'sua.choi@gmail.com',        crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 2, 5, '2021-05-31', '서울시 송파구',        '010-7845-2390', true),
  ('21101801', '정현우',  'hyunwoo.jung@gmail.com',    crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 3, 4, '2021-10-18', '서울시 서초구',        '010-4391-8762', true),
  ('22020701', '강예진',  'yejin.kang@gmail.com',      crypt('Test1234!', gen_salt('bf', 10)), 'MANAGER',  6, 6, '2022-02-07', '인천시 연수구',        '010-6127-5093', true),
  ('22062001', '윤도현',  'dohyun.yoon@gmail.com',     crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 4, 7, '2022-06-20', '서울시 용산구',        '010-8754-3219', true),
  ('22091301', '장하은',  'haeun.jang@gmail.com',      crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 2, 8, '2022-09-13', '서울시 강서구',        '010-2983-6047', true),
  ('22120501', '임성준',  'sungjun.im@gmail.com',      crypt('Test1234!', gen_salt('bf', 10)), 'MANAGER',  5, 2, '2022-12-05', '경기도 수원시 영통구',  '010-5309-7842', true),
  ('23032101', '한지수',  'jisu.han@gmail.com',        crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 3, 1, '2023-03-21', '서울시 노원구',        '010-7162-4583', true),
  ('23071001', '오재원',  'jaewon.oh@gmail.com',       crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 4, 3, '2023-07-10', '경기도 고양시 일산동구','010-3820-9147', true),
  ('23101601', '신유진',  'yujin.shin@gmail.com',      crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 2, 5, '2023-10-16', '서울시 영등포구',      '010-9234-5618', true),
  ('24010801', '권태양',  'taeyang.kwon@gmail.com',    crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 3, 6, '2024-01-08', '부산시 해운대구',      '010-4567-8923', true),
  ('24042201', '황소연',  'soyeon.hwang@gmail.com',    crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 4, 3, '2024-04-22', '대구시 수성구',        '010-8193-4520', true),
  ('24082901', '류승현',  'seunghyun.ryu@gmail.com',   crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 2, 7, '2024-08-29', '대전시 유성구',        '010-6759-2381', true),
  ('24111101', '배미래',  'mirae.bae@gmail.com',       crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 4, 4, '2024-11-11', '경기도 안양시 동안구',  '010-2315-6749', true),
  ('25021401', '조민호',  'minho.jo@gmail.com',        crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 2, 5, '2025-02-14', '서울시 관악구',        '010-7831-2495', true),
  ('25060301', '문지원',  'jiwon.moon@gmail.com',      crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 2, 6, '2025-06-03', '경기도 용인시 수지구',  '010-5402-1938', true),
  ('25092201', '남기현',  'gihyun.nam@gmail.com',      crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 4, 1, '2025-09-22', '경기도 화성시 동탄구',  '010-9871-3412', true),
  ('26011501', '석혜린',  'hyerin.seok@gmail.com',     crypt('Test1234!', gen_salt('bf', 10)), 'EMPLOYEE', 2, 8, '2026-01-15', '서울시 종로구',        '010-1397-2468', true)
ON CONFLICT DO NOTHING;
