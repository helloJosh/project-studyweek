package com.partimestudy.studywork.auth.exception;

public class AccessTokenNotFoundException extends RuntimeException{
    public AccessTokenNotFoundException(String message) {
        super(message);
    }
}
