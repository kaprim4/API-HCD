package com.example.HCOData.enums;

public enum OperationEnums {

    OPERATION_TEST_PROCHECK("TEST_PROCHECK"),
    OPERATION_TEST_HIGHCO("FR000000"),
    OPERATION_VEEPPE("VEEPEE"),
    OPERATION_PG("FR111111");

    private final String value;

    OperationEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
