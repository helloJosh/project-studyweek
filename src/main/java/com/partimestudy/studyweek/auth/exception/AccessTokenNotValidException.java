package com.partimestudy.studyweek.auth.exception;

public class AccessTokenNotValidException extends RuntimeException{
    public AccessTokenNotValidException(String message) {
        super(message);
    }
}
