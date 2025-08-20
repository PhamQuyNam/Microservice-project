package com.npq.account_service.service;

import com.npq.account_service.entity.Account;

import java.util.List;

public interface IAccountService {
    List<Account> getListAccounts();
}
