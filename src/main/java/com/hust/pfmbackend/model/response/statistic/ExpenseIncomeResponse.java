package com.hust.pfmbackend.model.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@ToString
public class ExpenseIncomeResponse {

    private List<Point> month;
    private List<Point> quarter;
    private List<Point> year;

}
