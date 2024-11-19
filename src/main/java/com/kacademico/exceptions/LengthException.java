package com.kacademico.exceptions;

public class LengthException extends RuntimeException {
    
    public LengthException(String message) {
        super(message);
    }

    public LengthException() {
        super("LengthException.");
    }

    private static final long serialVersionUID = 1L;

}
