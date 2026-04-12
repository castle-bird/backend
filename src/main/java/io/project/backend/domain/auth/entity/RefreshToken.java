package io.project.backend.domain.auth.entity;

import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  @Column(nullable = false, unique = true, length = 512)
  private String token;

  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;

  @Column(name = "is_revoked", nullable = false)
  private boolean revoked = false;

  // Token Rotation 패턴: 이 토큰을 대체한 새 토큰을 추적해 탈취 감지에 활용
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "replaced_by")
  private RefreshToken replacedBy;

  @Builder
  private RefreshToken(Employee employee, String token, Instant expiresAt) {
    this.employee = employee;
    this.token = token;
    this.expiresAt = expiresAt;
    this.revoked = false;
  }

  public void revoke() {
    this.revoked = true;
  }

  public void markReplacedBy(RefreshToken next) {
    this.replacedBy = next;
  }

  public boolean isExpired() {
    return Instant.now().isAfter(this.expiresAt);
  }
}
