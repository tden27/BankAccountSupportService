package com.github.tden27.bankaccountsupportservice.services;

import com.github.tden27.bankaccountsupportservice.dto.ResponseAccountDto;
import com.github.tden27.bankaccountsupportservice.exceptions.AccountExistException;
import com.github.tden27.bankaccountsupportservice.model.Account;

import java.util.List;

public interface IAccountService {

    /**
     * Method for create bank account
     * @param name name user
     * @param pinCode pin-code for account (4 digits)
     * @return created account
     */
    Account createBankAccount(String name, String pinCode) throws AccountExistException;

    /**
     * Method for getting list of all bank accounts
     * @return list of account
     */
    List<ResponseAccountDto> getAllAccounts();

    /**
     * Method for search account by name
     * @param name name user
     * @return account
     */
    Account findAccountByName(String name);

    /**
     * Method for deposit amount to bank account
     * @param account account
     * @param amount amount money
     * @return account
     */
    ResponseAccountDto depositAccount(Account account, double amount);

    /**
     * Method for withdraw money from bank account
     * @param account bank account
     * @param amount amount money
     * @return bank account
     */
    ResponseAccountDto withDraw(Account account, double amount);

    /**
     * Method for transfer money between bank accounts
     * @param sourceAccount source bank account
     * @param targetAccount target bank account
     * @param amount transfer amount
     */
    void transferMoney(Account sourceAccount, Account targetAccount, double amount);
}