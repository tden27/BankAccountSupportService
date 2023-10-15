package com.github.tden27.bankaccountsupportservice.dto;

import jakarta.validation.constraints.Pattern;

public class RequestCreateAccountDto {
    public String name;

    @Pattern(regexp = "\\d{4}", message = "Пин-код должен состоять из 4х цифр")
    public String pinCode;

    public RequestCreateAccountDto(String name, String pinCode) {
        this.name = name;
        this.pinCode = pinCode;
    }
}
