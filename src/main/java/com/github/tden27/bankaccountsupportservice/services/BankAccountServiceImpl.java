package com.github.tden27.bankaccountsupportservice.services;

import com.github.tden27.bankaccountsupportservice.model.Account;
import com.github.tden27.bankaccountsupportservice.repository.AccountRepository;
import org.springframework.stereotype.Component;

@Component
public class BankAccountServiceImpl implements IBankAccountService {

    final
    AccountRepository bankAccountRepository;

    public BankAccountServiceImpl(AccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Account createBankAccount(String name, int pinCode) {
        Account bankAccount = new Account(name, pinCode);
        return bankAccountRepository.save(bankAccount);
    }
}

