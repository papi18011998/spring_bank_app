package com.example.digital_banking_backend.exception;

public class BalanceAccountInsufficentException extends Exception {
    public BalanceAccountInsufficentException(String message) {
        super(message);
    }
}
