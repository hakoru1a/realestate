package com.dpdc.realestate.models.enumerate;

import lombok.Getter;

@Getter
public enum Role {
    STAFF(2),
    ADMIN(1);

    private int value;

    private Role(int value) {
        this.value = value;
    }

}