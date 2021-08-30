package com.example.springboot.service;

public enum FileColumn {
    FIRST_NAME(1),
    MIDDLE_NAME(2),
    LAST_NAME(3),
    MOBILE_NUMBER(4),
    EMAIL_ID(5),
    DATE_OF_BIRTH(6),

    STREET(8),
    HOUSE_NUMBER(9),
    ZIP_CODE(10),
    CITY(11),

    FIELD1(13),
    FIELD2(14),
    FIELD3(15);

    public final int columnNumber;
    private FileColumn(int columnNumber) {
        this.columnNumber = columnNumber;
    }
}
