package com.npq.department_service.specification;

import com.npq.department_service.entity.Department;
import com.npq.department_service.form.DepartmentFilterForm;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.util.Objects;

public class DepartmentSpecification {
    public static Specification<Department> buildWhere(String search, DepartmentFilterForm filterForm) {
        Specification<Department> where = null;

        if (search != null && !search.trim().isEmpty()) {
            String keyword = "%" + search.trim().toLowerCase() + "%";
            where = (root, query, builder) ->
                    builder.like(builder.lower(root.get("name")), keyword);
        }

        if (filterForm != null) {
            if (Objects.nonNull(filterForm.getType())) {
                Specification<Department> typeSpec = (root, query, builder) ->
                        builder.equal(root.get("type"), filterForm.getType());
                where = (where == null) ? typeSpec : where.and(typeSpec);
            }

            if (Objects.nonNull(filterForm.getMinCreatedDate())) {
                Specification<Department> minDateSpec = (root, query, builder) ->
                        builder.greaterThanOrEqualTo(root.get("createdAt"), filterForm.getMinCreatedDate());
                where = (where == null) ? minDateSpec : where.and(minDateSpec);
            }

            if (Objects.nonNull(filterForm.getMaxCreatedDate())) {
                Specification<Department> maxDateSpec = (root, query, builder) ->
                        builder.lessThanOrEqualTo(root.get("createdAt"), filterForm.getMaxCreatedDate());
                where = (where == null) ? maxDateSpec : where.and(maxDateSpec);
            }
        }

        return where;
    }
}
