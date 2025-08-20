package com.npq.department_service.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public class AccountDTO {
    private int id;

    private  String username;

    private  String firstname;

    private String lastname;

    private String role;

    private String departmentName;

    private int departmentId;
}
