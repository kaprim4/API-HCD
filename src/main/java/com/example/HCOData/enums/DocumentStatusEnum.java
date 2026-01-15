package com.example.HCOData.enums;

public enum DocumentStatusEnum {

    PROCESSING("1","PROCESSING"),
    PROCESSED("3","PROCESSED"),
    HUMAN_PROCESSED("3","HUMAN_PROCESSED"),
    NON_EXPLOITABLE("1","NON_EXPLOITABLE"),
    PROCESSING_IN_MODERATION("2","PROCESSING"),
    PROCESSING_NOT_EXISTS("1","PA_DOES_NOT_EXIST"),
    WHITING_PROCESSING("0","PROCESSING");

    private final String value;
    private final String message;

    DocumentStatusEnum(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return this.value;
    }
    public String getMessage() {
        return this.message;
    }
}
