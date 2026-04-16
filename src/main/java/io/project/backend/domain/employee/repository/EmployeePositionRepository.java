package io.project.backend.domain.employee.repository;

import io.project.backend.domain.employee.entity.EmployeePosition;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {

  Optional<EmployeePosition> findByName(String name);

  Optional<EmployeePosition> findTopByOrderBySortOrderDesc();
}
