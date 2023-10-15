package com.github.tden27.bankaccountsupportservice.exceptions;

public class ResponseError {

    private String message;

    public ResponseError() {
    }

    public ResponseError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
