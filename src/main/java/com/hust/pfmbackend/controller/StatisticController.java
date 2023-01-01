package com.hust.pfmbackend.controller;

import com.hust.pfmbackend.model.response.statistic.ExpenseIncomeResponse;
import com.hust.pfmbackend.model.response.statistic.GeneralStatisticResponse;
import com.hust.pfmbackend.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping(path = "/statistic/general")
    public ResponseEntity<GeneralStatisticResponse> getGeneral() {
        return ResponseEntity.ok(statisticService.getGeneral());
    }

    @GetMapping(path = "/statistic/expense-income-graph")
    public ResponseEntity<ExpenseIncomeResponse> getExpenseIncomeData() {
        return ResponseEntity.ok(statisticService.getExpenseIncomeGraphData());
    }

    @GetMapping(path = "/statistic/expense-graph")
    public ResponseEntity<ExpenseIncomeResponse> getExpenseData() {
        return ResponseEntity.ok(statisticService.getExpenseGraphData());
    }

    @GetMapping(path = "/statistic/income-graph")
    public ResponseEntity<ExpenseIncomeResponse> getIncomeData() {
        return ResponseEntity.ok(statisticService.getIncomeGraphData());
    }
}
