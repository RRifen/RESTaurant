package com.example.restaurant.util;

public class MenuOrderNotCreatedException extends RuntimeException {
    public MenuOrderNotCreatedException(String msg) {
        super(msg);
    }
}
