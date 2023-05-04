package com.example.restaurant.util.exceptions;

public class AuthenticationRequiredException extends RuntimeException {
    public AuthenticationRequiredException() {
        super("Authentication Required");
    }
}
