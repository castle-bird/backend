package io.project.backend.domain.employee.repository;

import io.project.backend.domain.employee.entity.Salary;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

  Optional<Salary> findByEmployeeIdAndEmployeeDeletedIsFalse(Long employeeId);
}
