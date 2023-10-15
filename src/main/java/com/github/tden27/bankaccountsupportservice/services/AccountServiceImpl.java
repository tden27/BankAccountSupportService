package com.github.tden27.bankaccountsupportservice.services;

import com.github.tden27.bankaccountsupportservice.dto.ResponseAccountDto;
import com.github.tden27.bankaccountsupportservice.exceptions.AccountExistException;
import com.github.tden27.bankaccountsupportservice.model.Account;
import com.github.tden27.bankaccountsupportservice.repository.AccountRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {

    final
    AccountRepository bankAccountRepository;

    public AccountServiceImpl(AccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Account createBankAccount(String name, String pinCode) throws AccountExistException {
        Account bankAccount = new Account(name, pinCode);
        try {
            return bankAccountRepository.save(bankAccount);
        } catch (DataIntegrityViolationException e) {
            throw new AccountExistException("Аккаунт с параметрами name=" + name + " и pin-code=" + pinCode + " уже существует");
        }
    }

    @Override
    public List<ResponseAccountDto> getAllAccounts() {
        List<ResponseAccountDto> responseAccountDtoList = new ArrayList<>();
        List<Account> accountList = bankAccountRepository.findAll();
        accountList.forEach(it -> responseAccountDtoList.add(new ResponseAccountDto(it.getName(), it.getBalance())));
        return responseAccountDtoList;
    }

    @Override
    public Account findAccountByName(String name) {
        return bankAccountRepository.findAccountByName(name);
    }

    @Override
    public ResponseAccountDto depositAccount(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
        Account depositAccount = bankAccountRepository.save(account);
        return new ResponseAccountDto(depositAccount.getName(), depositAccount.getBalance());
    }

    @Override
    public ResponseAccountDto withDraw(Account account, double amount) {
        account.setBalance(account.getBalance() - amount);
        Account withDrawAccount = bankAccountRepository.save(account);
        return new ResponseAccountDto(withDrawAccount.getName(), withDrawAccount.getBalance());
    }

    @Override

    public void transferMoney(Account sourceAccount, Account targetAccount, double amount) {
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);
        bankAccountRepository.save(sourceAccount);
        bankAccountRepository.save(targetAccount);
    }
}

