package com.partimestudy.studywork.auth.exception;

public class AccessTokenNotValidException extends RuntimeException{
    public AccessTokenNotValidException(String message) {
        super(message);
    }
}
