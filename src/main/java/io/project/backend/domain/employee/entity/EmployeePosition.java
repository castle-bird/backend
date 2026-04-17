package io.project.backend.domain.employee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_positions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeePosition {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String name;  // 사원, 주임, 대리, 과장, 차장, 부장, 이사 등

  @Column(name = "sort_order", nullable = false, unique = true)
  private Short sortOrder; // 추후 직급 나열을 위해 미리 만듦

  @Builder
  private EmployeePosition(String name, Short sortOrder) {
    this.name = name;
    this.sortOrder = sortOrder;
  }
}