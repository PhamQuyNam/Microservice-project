package com.npq.department_service.service;

import com.npq.department_service.entity.Department;
import com.npq.department_service.form.DepartmentFilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDepartmentService {
    public Page<Department> getAllDepartments(Pageable pageable, String search, DepartmentFilterForm filterForm);

    public Department getDepartmentByID(int id);
}
