package com.partimestudy.studyweek.auth.exception;

public class RefreshTokenNotValidException extends RuntimeException{
    public RefreshTokenNotValidException(String message) {
        super(message);
    }
}
