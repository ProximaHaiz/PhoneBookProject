package com.proxsoftware.webapp.exceptions;

/**
 * Created by Proxima on 22.04.2016.
 */
public class FileIOException extends RuntimeException {
    private String message;


    public FileIOException(String message) {
        this.message = message;
    }

    public FileIOException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
