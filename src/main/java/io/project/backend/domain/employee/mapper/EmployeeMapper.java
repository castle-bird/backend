package io.project.backend.domain.employee.mapper;

import io.project.backend.domain.auth.dto.request.SignupRequest;
import io.project.backend.domain.employee.entity.Department;
import io.project.backend.domain.employee.entity.Employee;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

  @Mapping(target = "employeeNumber", source = "employeeNumber")
  @Mapping(target = "name",           source = "signupRequest.name")
  @Mapping(target = "email",          source = "signupRequest.email")
  @Mapping(target = "password",       source = "encodedPassword")
  @Mapping(target = "position",       source = "signupRequest.position")
  @Mapping(target = "hireDate",       source = "hireDate")
  @Mapping(target = "department",     source = "department")
  Employee toEntityForSignup(
      SignupRequest signupRequest,
      String encodedPassword,
      String employeeNumber,
      LocalDate hireDate,
      Department department
  );
}
