package com.github.tden27.bankaccountsupportservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tden27.bankaccountsupportservice.dto.RequestCreateAccountDto;
import com.github.tden27.bankaccountsupportservice.dto.ResponseAccountDto;
import com.github.tden27.bankaccountsupportservice.exceptions.AccountExistException;
import com.github.tden27.bankaccountsupportservice.model.Account;
import com.github.tden27.bankaccountsupportservice.services.IAccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAccountService accountService;

    @Test
    void createAccountPositiveTest() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .content(objectMapper.writeValueAsString(new RequestCreateAccountDto("account1", "1234")))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void createAccountNegativeTest() throws Exception {
        Mockito.when(this.accountService.createBankAccount("account1", "1234")).thenThrow(new AccountExistException("Аккаунт с параметрами name=account1 и pin-code=1234 уже существует"));

        mockMvc.perform(post("/api/accounts")
                        .content(objectMapper.writeValueAsString(new RequestCreateAccountDto("account1", "1234")))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllAccountsTest() throws Exception {
        mockMvc.perform(get("/api/accounts")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void depositMoneyTest() throws Exception {
        Account account1 = new Account("account1", "1234");
        ResponseAccountDto responseAccountDto = new ResponseAccountDto("account1", 100.0);
        Mockito.when(this.accountService.findAccountByName("account1")).thenReturn(account1);
        Mockito.when(this.accountService.depositAccount(account1, 100.0)).thenReturn(responseAccountDto);

        mockMvc.perform(put("/api/accounts/account1/deposit?amount=100.0")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void withdrawMoneyPositiveTest() throws Exception {
        Account account1 = new Account("account1", "1234");
        account1.setBalance(100.0);
        ResponseAccountDto responseAccountDto = new ResponseAccountDto("account1", 100.0);
        Mockito.when(this.accountService.findAccountByName("account1")).thenReturn(account1);
        Mockito.when(this.accountService.withDraw(account1, 10.0)).thenReturn(responseAccountDto);

        mockMvc.perform(put("/api/accounts/account1/withdraw?amount=10.0&pinCode=1234")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void withdrawMoneyNotCorrectPinCodeTest() throws Exception {
        Account account1 = new Account("account1", "1234");
        ResponseAccountDto responseAccountDto = new ResponseAccountDto("account1", 100.0);
        Mockito.when(this.accountService.findAccountByName("account1")).thenReturn(account1);
        Mockito.when(this.accountService.withDraw(account1, 10.0)).thenReturn(responseAccountDto);

        mockMvc.perform(put("/api/accounts/account1/withdraw?amount=10.0&pinCode=1235")
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void withdrawMoneyNotEnoughMoneyTest() throws Exception {
        Account account1 = new Account("account1", "1234");
        ResponseAccountDto responseAccountDto = new ResponseAccountDto("account1", 100.0);
        Mockito.when(this.accountService.findAccountByName("account1")).thenReturn(account1);
        Mockito.when(this.accountService.withDraw(account1, 10.0)).thenReturn(responseAccountDto);

        mockMvc.perform(put("/api/accounts/account1/withdraw?amount=10.0&pinCode=1234")
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void transferMoney() throws Exception {
        Account account1 = new Account("account1", "1234");
        account1.setBalance(100.0);
        Account account2 = new Account("account2", "1235");
        account2.setBalance(100.0);
        Mockito.when(this.accountService.findAccountByName("account1")).thenReturn(account1);
        Mockito.when(this.accountService.findAccountByName("account2")).thenReturn(account2);

        mockMvc.perform(post("/api/accounts/account1/transfer/account2?amount=80.0")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}