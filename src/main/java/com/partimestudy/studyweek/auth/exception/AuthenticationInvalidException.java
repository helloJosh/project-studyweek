package com.partimestudy.studyweek.auth.exception;

public class AuthenticationInvalidException extends RuntimeException{
    public AuthenticationInvalidException(String message) {
        super(message);
    }
}
