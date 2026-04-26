package io.project.backend.domain.employee.dto.response;

import java.util.List;

public record EmployeeListResponse(
    List<EmployeeResponse> content,
    Long nextCursor,
    boolean hasNext
) {

  public static EmployeeListResponse from(
      List<EmployeeResponse> content,
      boolean hasNext
  ) {
    Long nextCursor = hasNext
        ? content.get(content.size() - 1).id()
        : null;

    return new EmployeeListResponse(content, nextCursor, hasNext);
  }
}
