package io.project.backend.domain.employee.entity;

import io.project.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salaries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Salary extends BaseTimeEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  @Column(name = "month_salary", nullable = false, precision = 15, scale = 2)
  private BigDecimal monthSalary;

  @Column(name = "year_salary", nullable = false, precision = 15, scale = 2)
  private BigDecimal yearSalary;

  @Column(nullable = false, precision = 15, scale = 2)
  private BigDecimal bonus;

  @Column(name = "payment_date")
  private LocalDate paymentDate;

  @Builder
  private Salary(
      Employee employee,
      BigDecimal monthSalary,
      BigDecimal yearSalary,
      BigDecimal bonus,
      LocalDate paymentDate
  ) {
    this.employee = employee;
    this.monthSalary = monthSalary;
    this.yearSalary = yearSalary;
    this.bonus = bonus != null ? bonus : BigDecimal.ZERO;
    this.paymentDate = paymentDate;
  }

  public void updateBonus(BigDecimal bonus) {
    this.bonus = bonus;
  }

  public void recordPaymentDate(LocalDate paymentDate) {
    this.paymentDate = paymentDate;
  }
}
