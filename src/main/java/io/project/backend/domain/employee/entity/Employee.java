package io.project.backend.domain.employee.entity;

import io.project.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "employees")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee extends BaseTimeEntity {

  @Column(name = "employee_number", nullable = false, unique = true, length = 20)
  private String employeeNumber;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(nullable = false, length = 255)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "employee_role")
  @ColumnTransformer(read = "role::text", write = "?::employee_role")
  private EmployeeRole role;

  // 첫 로그인시 비번 강제 초기화를 위함
  @Column(name = "password_change_required", nullable = false)
  boolean passwordChangeRequired = true;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "position_id",nullable = false)
  private EmployeePosition employeePosition;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id")
  private Department department;

  @Column(name = "hire_date")
  private LocalDate hireDate;

  @Column(name = "is_deleted", nullable = false)
  private boolean deleted = false;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @Builder
  private Employee(
      String employeeNumber,
      String name,
      String email,
      String password,
      EmployeeRole role,
      EmployeePosition employeePosition,
      Department department,
      LocalDate hireDate
  ) {
    this.employeeNumber = employeeNumber;
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
    this.employeePosition = employeePosition;
    this.department = department;
    this.hireDate = hireDate;
    this.deleted = false;
  }

  public void softDelete() {
    this.deleted = true;
    this.deletedAt = Instant.now();
  }

  public void changePassword(String encodedPassword) {
    this.password = encodedPassword;
    this.passwordChangeRequired = false;
  }

  public void assignDepartment(Department department) {
    this.department = department;
  }

  public void assignEmployeePosition(EmployeePosition employeePosition) {
    this.employeePosition = employeePosition;
  }

  public void changeRole(EmployeeRole role) {
    this.role = role;
  }
}
