package io.project.backend.global.security.details;

import io.project.backend.domain.employee.exception.EmployeeNotFoundException;
import io.project.backend.domain.employee.repository.EmployeeRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final EmployeeRepository employeeRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return employeeRepository.findByEmailAndDeletedFalse(email)
        .map(UserDetailsImpl::new)
        .orElseThrow(() -> new EmployeeNotFoundException(Map.of("email", email)));
  }
}

