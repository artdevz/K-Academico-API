package com.kacademic.exceptions;

public class DuplicateValueException extends RuntimeException {
    
    public DuplicateValueException(String message) {
        super(message);
    }

    public DuplicateValueException() {
        super("DuplicateValueException.");
    }

    private static final long serialVersionUID = 1L;

}
