package com.example.restaurant.util.exceptions;

public class BadRequest extends RuntimeException{
    public BadRequest(String msg) {
        super(msg);
    }

}
