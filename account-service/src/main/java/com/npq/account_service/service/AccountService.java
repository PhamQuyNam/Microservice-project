package com.npq.account_service.service;

import com.npq.account_service.entity.Account;
import com.npq.account_service.repository.IAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService{
    private  final IAccountRepository acRepository;

    @Override
    public List<Account> getListAccounts() {
        return acRepository.findAll();
    }

    @Override
    public Account findAccountById(int id) {
        Optional<Account> accountOpt = acRepository.findById(id);
        return accountOpt.orElse(null);
    }
}
