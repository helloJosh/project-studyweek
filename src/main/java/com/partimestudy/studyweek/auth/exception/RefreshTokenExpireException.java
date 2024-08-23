package com.partimestudy.studyweek.auth.exception;

public class RefreshTokenExpireException extends RuntimeException{
    public RefreshTokenExpireException(String message) {
        super(message);
    }
}
