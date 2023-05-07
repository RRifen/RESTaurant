package com.example.restaurant.util.exceptions;

public class LoginAlreadyTakenException extends RuntimeException {
    public LoginAlreadyTakenException(String msg) {
        super(msg);
    }

}
