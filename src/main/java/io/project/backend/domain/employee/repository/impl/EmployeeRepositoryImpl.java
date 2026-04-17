package io.project.backend.domain.employee.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.project.backend.domain.employee.entity.Employee;
import io.project.backend.domain.employee.entity.QDepartment;
import io.project.backend.domain.employee.entity.QEmployee;
import io.project.backend.domain.employee.entity.QEmployeePosition;
import io.project.backend.domain.employee.repository.EmployeeRepositoryCustom;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Employee> findAllByCursor(String department, String position, Long cursor, int size) {
    int pageSize = size + 1;
    QEmployee e = QEmployee.employee;
    QDepartment d = QDepartment.department;
    QEmployeePosition p = QEmployeePosition.employeePosition;

    return queryFactory
        .selectFrom(e)
        .join(e.department, d).fetchJoin()
        .join(e.employeePosition, p).fetchJoin()
        .where(
            e.deleted.isFalse(),
            cursor != null ? e.id.gt(cursor) : null,
            department != null ? e.department.name.eq(department) : null,
            position != null ? e.employeePosition.name.eq(position) : null
        )
        .orderBy(e.id.asc())
        .limit(pageSize)
        .fetch();
  }
}
