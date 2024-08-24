package com.partimestudy.studywork.auth.exception;

public class RefreshTokenNotValidException extends RuntimeException{
    public RefreshTokenNotValidException(String message) {
        super(message);
    }
}
