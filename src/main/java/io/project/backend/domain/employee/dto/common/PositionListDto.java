package io.project.backend.domain.employee.dto.common;

public record PositionListDto(
    String name
) {

  public static PositionListDto from(String name) {
    return new PositionListDto(name);
  }

}
