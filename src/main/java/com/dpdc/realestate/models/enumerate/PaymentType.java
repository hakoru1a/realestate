package com.dpdc.realestate.models.enumerate;


import lombok.Getter;

@Getter
public enum PaymentType {
    BOOK_APPOINTMENT(1),
    BUY_TURN(2);

    private int value;

    private PaymentType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}