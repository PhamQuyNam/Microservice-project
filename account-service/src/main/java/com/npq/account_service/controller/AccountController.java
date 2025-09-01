package com.npq.account_service.controller;

import com.npq.account_service.dto.AccountDTO;
import com.npq.account_service.dto.DepartmentDTO;
import com.npq.account_service.entity.Account;
import com.npq.account_service.feignclient.DepartmentFeignClient;
import com.npq.account_service.service.IAccountService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/accounts")
public class AccountController {
    @Value("${greeting.text}")
    private String greetingText;

    private final IAccountService acService;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final DepartmentFeignClient departmentFeignClient;

    @GetMapping()
    public List<AccountDTO> getListAccounts() {
        List<Account> accounts = acService.getListAccounts();
        List<AccountDTO> listAccountDTO = modelMapper.map(
                accounts,
                new TypeToken<List<AccountDTO>>() {}.getType());
        return listAccountDTO;
    }

    @CircuitBreaker(name = "departmentService", fallbackMethod = "fallbackNotCallDepartmentService")
    @GetMapping("/{id}")
    public DepartmentDTO getDepartmentById(@PathVariable final int id){
        Account account = acService.findAccountById(id);
        int departmentId = account.getDepartment().getId();
        DepartmentDTO departmentDTO = restTemplate.getForObject("http://localhost:8080/api/v1/departments/"+departmentId, DepartmentDTO.class);
        //DepartmentDTO departmentDTO = departmentFeignClient.getDepartmentById(id);
        log.info("Department DTO: {}",departmentDTO);
        return departmentDTO;
    }

    public String fallbackNotCallDepartmentService(int id, Throwable throwable){
        return "Department Service Down";
    }

    @GetMapping("hello")
    public String hello(){
        return "hello";
    }


}
