package com.kacademico.enums;

public enum EShift {
    MORNING("505"),
    AFTERNOON("506"),
    NIGHT("507");

    private final String code;

    EShift(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
