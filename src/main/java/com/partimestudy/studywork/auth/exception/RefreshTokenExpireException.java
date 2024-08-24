package com.partimestudy.studywork.auth.exception;

public class RefreshTokenExpireException extends RuntimeException{
    public RefreshTokenExpireException(String message) {
        super(message);
    }
}
