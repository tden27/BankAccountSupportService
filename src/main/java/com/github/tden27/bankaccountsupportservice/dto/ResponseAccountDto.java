package com.github.tden27.bankaccountsupportservice.dto;

public class ResponseAccountDto {
    private final String name;
    private final double balance;

    public ResponseAccountDto(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }
}
