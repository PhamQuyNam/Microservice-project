package com.npq.department_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.npq.department_service.entity.Account;
import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
public class DepartmentDTO {
    private String name;
    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;
    private List<Account> accounts;
}
