package io.project.backend.domain.employee.dto.response;

import io.project.backend.domain.employee.entity.EmployeeRole;

public record EmployeeResponse(
    Long id,
    String name,
    String email,
    EmployeeRole role,
    String employeePosition,
    String department,
    String address,
    String phone
) {

}
