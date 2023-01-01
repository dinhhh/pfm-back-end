package com.hust.pfmbackend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Quarter {

    Q1("Q1", 0),
    Q2("Q2", 1),
    Q3("Q3", 2),
    Q4("Q4", 3);

    private final String label;
    private final int code;

    public static Quarter findByCode(int code) {
        for (Quarter q : Quarter.values()) {
            if (q.getCode() == code) {
                return q;
            }
        }
        return null;
    }

}
