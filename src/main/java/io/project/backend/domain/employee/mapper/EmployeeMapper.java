package io.project.backend.domain.employee.mapper;

import io.project.backend.domain.auth.dto.request.SignupRequest;
import io.project.backend.domain.employee.dto.response.EmployeeResponse;
import io.project.backend.domain.employee.entity.Department;
import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.entity.EmployeePosition;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

  @Mapping(target = "role", source = "signupRequest.role")
  @Mapping(target = "employeeNumber", source = "employeeNumber")
  @Mapping(target = "name", source = "signupRequest.name")
  @Mapping(target = "email", source = "signupRequest.email")
  @Mapping(target = "password", source = "encodedPassword")
  @Mapping(target = "employeePosition", source = "employeePosition")
  @Mapping(target = "hireDate", source = "hireDate")
  @Mapping(target = "department", source = "department")
  @Mapping(target = "address", source = "signupRequest.address")
  @Mapping(target = "phone", source = "signupRequest.phone")
  Employee toEntityForSignup(
      SignupRequest signupRequest,
      String encodedPassword,
      String employeeNumber,
      LocalDate hireDate,
      Department department,
      EmployeePosition employeePosition
  );

  // 조회 응답 매핑
  EmployeeResponse toEmployeeResponse(Employee employee);

  // Department와 EmployeePosition을 String으로 매핑
  default String getDepartmentName(Department department) {
    return department == null ? null : department.getName();
  }

  default String getPositionName(EmployeePosition employeePosition) {
    return employeePosition == null ? null : employeePosition.getName();
  }
}
