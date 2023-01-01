package com.hust.pfmbackend.service;

import com.hust.pfmbackend.model.response.statistic.ExpenseIncomeResponse;
import com.hust.pfmbackend.model.response.statistic.GeneralStatisticResponse;

public interface StatisticService {

    GeneralStatisticResponse getGeneral();

    ExpenseIncomeResponse getExpenseIncomeGraphData();

    ExpenseIncomeResponse getExpenseGraphData();

    ExpenseIncomeResponse getIncomeGraphData();

}
