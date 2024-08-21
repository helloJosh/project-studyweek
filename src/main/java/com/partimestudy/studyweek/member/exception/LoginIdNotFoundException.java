package com.partimestudy.studyweek.member.exception;

public class LoginIdNotFoundException extends RuntimeException{
    public LoginIdNotFoundException(String message) {
        super(message);
    }
}
