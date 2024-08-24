package com.partimestudy.studywork.member.exception;

public class LoginIdNotFoundException extends RuntimeException{
    public LoginIdNotFoundException(String message) {
        super(message);
    }
}
