package io.project.backend.global.security.details;

import io.project.backend.domain.employee.entity.Employee;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

  private final Employee employee;

  public Long getUserId() {
    return employee.getId();
  }

  @Override
  public String getUsername() {
    return employee.getEmail();
  }

  @Override
  public String getPassword() {
    return employee.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + employee.getRole()));
  }

  // 계정 만료 여부. e.g. 장기 미접속 등
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // 계정 잠금 여부. e.g. 비밀번호 여러 번 틀림 등
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  // 자격 증명 만료 여부. e.g. 비밀번호 유효 기간 만료 등
  @Override
  public boolean isCredentialsNonExpired() {
    return !employee.isPasswordChangeRequired();
  }

  // 계정 활성화 여부. e.g. 이메일 인증 완료 여부, 회원 탈퇴 여부 등
  @Override
  public boolean isEnabled() {
    return true;
  }
}
