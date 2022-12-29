package com.hust.pfmbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PeriodType {
    DAY(0),
    WEEK(1),
    MONTH(2),
    QUARTER(3),
    YEAR(4);

    private final int code;

    public static PeriodType findByCode(int code) {
        for (PeriodType pt : PeriodType.values()) {
            if (pt.getCode() == code) {
                return pt;
            }
        }
        return null;
    }
}
