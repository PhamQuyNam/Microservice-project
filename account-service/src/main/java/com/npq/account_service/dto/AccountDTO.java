package com.npq.account_service.dto;

import lombok.Data;

@Data
public class AccountDTO {
    private int id;

    private  String username;

    private  String firstname;

    private String lastname;

    private String role;

    private String departmentName;

    private int departmentId;
}
