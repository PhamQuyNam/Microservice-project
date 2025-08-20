package com.npq.department_service.service;

import com.npq.department_service.dto.AccountDTO;
import com.npq.department_service.dto.DepartmentDTO;
import com.npq.department_service.entity.Account;
import com.npq.department_service.entity.Department;
import com.npq.department_service.form.DepartmentFilterForm;
import com.npq.department_service.repository.IDepartmentRepository;
import com.npq.department_service.specification.DepartmentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;



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
    public Department getDepartmentByID ( int id){
        return repository.findById(id).orElse(null);
    }
}
