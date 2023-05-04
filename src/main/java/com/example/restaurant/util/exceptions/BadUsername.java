package com.example.restaurant.util.exceptions;

public class BadUsername extends RuntimeException {
    public BadUsername() {
        super("Bad Username");
    }
}
