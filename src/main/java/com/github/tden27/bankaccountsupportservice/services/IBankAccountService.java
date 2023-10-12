package com.github.tden27.bankaccountsupportservice.services;

import com.github.tden27.bankaccountsupportservice.model.Account;

public interface IBankAccountService {

    /**
     * Method for create bank account
     * @param name name user
     * @param pinCode pin-code for account (4 digits)
     */
    Account createBankAccount(String name, int pinCode);
}