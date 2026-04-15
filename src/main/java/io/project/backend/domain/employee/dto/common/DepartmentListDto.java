package io.project.backend.domain.employee.dto.common;

public record DepartmentListDto(
    String name
) {

  public static DepartmentListDto from(String name) {
    return new DepartmentListDto(name);
  }

}
