package com.crm.exception;

public class LoginException extends Exception{

    private static final long serialVersionUID = 5482108718702426182L;

    public LoginException() {
    }

    public LoginException(String message) {
        super(message);
    }
}
