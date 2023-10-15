package com.github.tden27.bankaccountsupportservice.controllers;

import com.github.tden27.bankaccountsupportservice.dto.RequestCreateAccountDto;
import com.github.tden27.bankaccountsupportservice.dto.ResponseAccountDto;
import com.github.tden27.bankaccountsupportservice.exceptions.AccountExistException;
import com.github.tden27.bankaccountsupportservice.model.Account;
import com.github.tden27.bankaccountsupportservice.services.IAccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    final
    IAccountService bankAccountService;

    public AccountController(IAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    // Создание нового аккаунта
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody RequestCreateAccountDto requestCreateAccountDto) throws AccountExistException {
        String name = requestCreateAccountDto.name;
        String pinCode = requestCreateAccountDto.pinCode;
        Account createdAccount = bankAccountService.createBankAccount(name, pinCode);
        return ResponseEntity.ok(createdAccount);
    }

    // Получение списка всех аккаунтов
    @GetMapping
    public ResponseEntity<List<ResponseAccountDto>> getAllAccounts() {
        List<ResponseAccountDto> accountList = bankAccountService.getAllAccounts();
        return ResponseEntity.ok(accountList);
    }

    // Внесение денег на счет
    @PutMapping("/{name}/deposit")
    public ResponseEntity<Object> depositMoney(
            @PathVariable String name,
            @RequestParam double amount
            ) {
        Account account = bankAccountService.findAccountByName(name);

        // Проверка наличия счета
        if (account == null) {
            return ResponseEntity.badRequest().body("Аккаунт с именем " + name + " не существует");
        }

        // Внесение денег
        ResponseAccountDto updatedAccount = bankAccountService.depositAccount(account, amount);
        return ResponseEntity.ok(updatedAccount);
    }

    // Снятие денег со счета
    @PutMapping("/{name}/withdraw")
    public ResponseEntity<Object> withdrawMoney(
            @PathVariable String name,
            @RequestParam double amount,
            @RequestParam String pinCode
    ) {
        Account account = bankAccountService.findAccountByName(name);

        // Проверка наличия счета
        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        // Проверка корректности пин-кода
        if (!Objects.equals(account.getPinCode(), pinCode)) {
            return ResponseEntity.badRequest().body("Не верный pin-code");
        }

        // Проверка достаточности средств на счете
        if (account.getBalance() < amount) {
            return ResponseEntity.badRequest().body("Недостаточно средств на счете для снятия");
        }

        // Снятие денег
        ResponseAccountDto updatedAccount = bankAccountService.withDraw(account, amount);

        return ResponseEntity.ok(updatedAccount);
    }

    // Перевод денег между счетами
    @PostMapping("/{sourceName}/transfer/{targetName}")
    public ResponseEntity<Object> transferMoney(
            @PathVariable String sourceName,
            @PathVariable String targetName,
            @RequestParam double amount
    ) {
        Account sourceAccount = bankAccountService.findAccountByName(sourceName);
        Account targetAccount = bankAccountService.findAccountByName(targetName);

        // Проверка наличия счетов
        if (sourceAccount == null) {
            return ResponseEntity.badRequest().body("Не найден аккаунт с именем " + sourceName);
        }
        if (targetAccount == null) {
            return ResponseEntity.badRequest().body("Не найден аккаунт с именем " + targetName);
        }

        // Проверка достаточности средств на счете отправителя
        if (sourceAccount.getBalance() < amount) {
            return ResponseEntity.badRequest().body("Недостаточно средств на счете для перевода");
        }

        // Выполнение перевода
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        bankAccountService.transferMoney(sourceAccount, targetAccount, amount);

        return ResponseEntity.ok().body("Перевод успешно выполнен");
    }
}
