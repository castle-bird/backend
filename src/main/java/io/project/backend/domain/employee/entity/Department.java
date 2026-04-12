package io.project.backend.domain.employee.entity;

import io.project.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department extends BaseTimeEntity {

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  // 팀장
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "manager_id")
  private Employee manager;

  @Builder
  private Department(String name, Employee manager) {
    this.name = name;
    this.manager = manager;
  }

  public void assignManager(Employee manager) {
    this.manager = manager;
  }

  public void changeName(String name) {
    this.name = name;
  }
}
