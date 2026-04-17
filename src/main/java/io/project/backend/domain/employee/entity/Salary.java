package io.project.backend.domain.employee.entity;

import io.project.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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

  @Column(name = "payment_day")
  private Short paymentDay;

  @Builder
  private Salary(
      Employee employee,
      BigDecimal monthSalary,
      BigDecimal yearSalary,
      Short paymentDay
  ) {
    this.employee = employee;
    this.monthSalary = monthSalary;
    this.yearSalary = yearSalary;
    this.paymentDay = paymentDay;
  }

  public void updatePaymentDay(Short paymentDay) {
    this.paymentDay = paymentDay;
  }
}
