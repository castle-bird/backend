package io.project.backend.domain.employee.dto.response;

public record PositionListResponse(
    String name
) {

  public static PositionListResponse from(String name) {
    return new PositionListResponse(name);
  }
}
