package com.github.tden27.bankaccountsupportservice.config;

import com.github.tden27.bankaccountsupportservice.exceptions.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class BankAccountServiceAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleException(Exception e) {
        ResponseError response = new ResponseError(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ResponseError>> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<ResponseError> response = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ResponseError(error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
