package io.project.backend.domain.employee.repository;

import io.project.backend.domain.employee.entity.Employee;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>,
    EmployeeRepositoryCustom {

  boolean existsByEmail(String email);

  long countByHireDate(LocalDate hireDate);

  Optional<Employee> findByEmail(String email);

  Optional<Employee> findByEmailAndDeletedFalse(String email);

  Optional<Employee> findByIdAndDeletedFalse(Long id);

  // 전체 직원 수 (퇴사자 제외)
  long countByDeletedFalse();
}
