package io.project.backend.domain.employee.mapper;

import io.project.backend.domain.employee.dto.request.SalaryRegisterRequest;
import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.entity.Salary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalaryMapper {

  @Mapping(target = "employee", source = "employee")
  Salary toEntityForRegister(Employee employee, SalaryRegisterRequest request);
}
