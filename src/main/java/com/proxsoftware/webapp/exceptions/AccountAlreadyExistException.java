package com.proxsoftware.webapp.exceptions;

/**
 * Created by Proxima on 24.04.2016.
 */
public class AccountAlreadyExistException extends RuntimeException{
    private String message;

    public AccountAlreadyExistException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
