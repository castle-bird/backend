package io.project.backend.domain.employee.repository;

import io.project.backend.domain.employee.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
}
