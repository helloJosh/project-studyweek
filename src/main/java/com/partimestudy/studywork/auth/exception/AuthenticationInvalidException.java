package com.partimestudy.studywork.auth.exception;

public class AuthenticationInvalidException extends RuntimeException{
    public AuthenticationInvalidException(String message) {
        super(message);
    }
}
