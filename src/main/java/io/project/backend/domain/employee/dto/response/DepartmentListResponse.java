package io.project.backend.domain.employee.dto.response;

public record DepartmentListResponse(
    String name
) {

  public static DepartmentListResponse from(String name) {
    return new DepartmentListResponse(name);
  }
}
