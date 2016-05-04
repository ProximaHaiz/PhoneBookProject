package com.proxsoftware.webapp.exceptions;

/**
 * Created by Proxima on 25.04.2016.
 */
public class FileEmptyException extends RuntimeException {
    private String message;

    public FileEmptyException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FileEmptyException(String message, Throwable cause) {

        super(message, cause);
    }
}
