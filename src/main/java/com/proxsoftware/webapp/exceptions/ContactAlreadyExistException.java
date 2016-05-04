package com.proxsoftware.webapp.exceptions;

/**
 * Created by Proxima on 24.04.2016.
 */
public class ContactAlreadyExistException extends RuntimeException {
    private String message;

    public ContactAlreadyExistException(String message) {
        super(message);
    }
}
