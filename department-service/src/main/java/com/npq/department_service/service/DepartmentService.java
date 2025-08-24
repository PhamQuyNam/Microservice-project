package com.npq.department_service.service;


import com.npq.department_service.entity.Department;
import com.npq.department_service.form.DepartmentFilterForm;
import com.npq.department_service.repository.IDepartmentRepository;
import com.npq.department_service.specification.DepartmentSpecification;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {

    private final IDepartmentRepository repository;

    @Override
    public Page<Department> getAllDepartments (Pageable pageable, String search, DepartmentFilterForm filterForm) {
        Specification<Department> where = DepartmentSpecification.buildWhere(search, filterForm);
        return repository.findAll(where, pageable);
    }
    @Override
    public Department getDepartmentById ( int id){
        Optional<Department> departmentOpt = repository.findById(id);
        return departmentOpt.orElseGet(() -> repository.findById(id).isPresent() ? repository.findById(id).get() : null);
    }
}
