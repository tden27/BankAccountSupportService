package com.github.tden27.bankaccountsupportservice.exceptions;

public class AccountExistException extends BankAccountServiceException {

    public AccountExistException(String message) {
        super(message);
    }
}
