package io.project.backend.domain.employee.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.project.backend.domain.employee.dto.common.DashboardDepartmentStatsDto;
import io.project.backend.domain.employee.entity.QDepartment;
import io.project.backend.domain.employee.entity.QEmployee;
import io.project.backend.domain.employee.repository.DepartmentRepositoryCustom;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QDepartment d = QDepartment.department;
  private final QEmployee e = QEmployee.employee;
  private final QEmployee manager = new QEmployee("manager");

  @Override
  public List<DashboardDepartmentStatsDto> getDepartmentStats() {
    return queryFactory
        .select(
            d.name,
            e.count(),
            manager.name
        )
        .from(d)
        .leftJoin(e).on(
            e.department.eq(d)
                .and(e.deleted.isFalse())
        )
        .leftJoin(d.manager, manager)
        .groupBy(d.id, d.name, manager.name)
        .fetch()
        .stream()
        .map(tuple -> DashboardDepartmentStatsDto.of(
            tuple.get(d.name),
            tuple.get(e.count()),
            tuple.get(manager.name)
        ))
        .toList();
  }
}
