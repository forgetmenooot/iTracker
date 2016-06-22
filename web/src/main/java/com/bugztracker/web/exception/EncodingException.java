package com.bugztracker.web.exception;

public class EncodingException extends RuntimeException {

    public EncodingException() {
        super();
    }

    public EncodingException(String message) {
        super(message);
    }

    public EncodingException(String message, Throwable cause) {
        super(message, cause);
    }

}
