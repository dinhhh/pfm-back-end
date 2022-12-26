package com.hust.pfmbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OperationType {
    EXPENSE(0),
    INCOME(1),
    BORROW(2),
    LEND(3);

    private final int code;

    public static OperationType findByCode(int  code) {
        for (OperationType ot : OperationType.values()) {
            if (ot.getCode() == code) {
                return ot;
            }
        }
        return null;
    }

}
