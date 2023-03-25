package com.hust.pfmbackend.service.graphbuilder;

import com.hust.pfmbackend.constant.Quarter;
import com.hust.pfmbackend.entity.ExpenseIncome;
import com.hust.pfmbackend.entity.OperationType;
import com.hust.pfmbackend.model.response.statistic.Point;
import com.hust.pfmbackend.utils.Convert;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphDataBuilder {

    public static final int NUMBER_OF_MONTH = 12;
    public static final int NUMBER_OF_QUARTER = 4;
    public static final int DIVISOR = 1000000; // convert VND to billion VND

    protected List<ExpenseIncome> expenseIncomes;
    protected Map<Integer, Long> mapExpensesIncomesByMonth;

    public GraphDataBuilder(List<ExpenseIncome> expenseIncomes) {
        this.expenseIncomes = expenseIncomes;
        this.mapExpensesIncomesByMonth = buildMapValue();
    }

    public List<Point> buildMonthData() {
        List<Point> response = new ArrayList<>();
        for (int i = 1; i <= NUMBER_OF_MONTH; i++) {
            String label = String.valueOf(i);
            if (mapExpensesIncomesByMonth.containsKey(i)) {
                response.add(Point.builder()
                        .label(label)
                        .y(Convert.convertLongToDoubleAndDivide(mapExpensesIncomesByMonth.get(i), DIVISOR))
                        .build());
            } else {
                response.add(Point.builder()
                        .label(label)
                        .y(0)
                        .build());
            }
        }
        return response;
    }

    public List<Point> buildQuarterData() {
        List<Point> response = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_QUARTER; i++) {
            int startMonth = 3 * i + 1;
            int endMonth = 3 * i + 3;
            long amount = 0L;
            for (int month = startMonth; month <= endMonth; month++) {
                if (mapExpensesIncomesByMonth.containsKey(month)) {
                    amount += mapExpensesIncomesByMonth.get(month);
                }
            }
            response.add(Point.builder()
                            .label(Quarter.findByCode(i).getLabel())
                            .y(Convert.convertLongToDoubleAndDivide(amount, DIVISOR))
                    .build());
        }
        return response;
    }

    public List<Point> buildYearData() {
        int leftYear = Year.MAX_VALUE;
        int rightYear = Year.MIN_VALUE;
        Map<Integer, Long> map = new HashMap<>();

        for (ExpenseIncome expenseIncome : expenseIncomes) {
            LocalDate createdDate = expenseIncome.getCreateOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = createdDate.getYear();
            if (leftYear > year) {
                leftYear = year;
            }
            if (rightYear < year) {
                rightYear = year;
            }

            if (map.containsKey(year)) {
                long prevAmount = map.get(year);
                if (expenseIncome.getOperationType() == OperationType.INCOME) {
                    map.put(year, prevAmount + expenseIncome.getAmount());
                }
                if (expenseIncome.getOperationType() == OperationType.EXPENSE) {
                    map.put(year, prevAmount - expenseIncome.getAmount());
                }
            } else {
                if (expenseIncome.getOperationType() == OperationType.INCOME) {
                    map.put(year, expenseIncome.getAmount());
                }
                if (expenseIncome.getOperationType() == OperationType.EXPENSE) {
                    map.put(year, -expenseIncome.getAmount());
                }
            }
        }

        List<Point> response = new ArrayList<>();
        for (int year = leftYear; year <= rightYear; year++) {
            response.add(Point.builder()
                    .label(String.valueOf(year))
                    .y(Convert.convertLongToDoubleAndDivide(map.get(year), DIVISOR))
                    .build());
        }
        return response;
    }

    private Map<Integer, Long> buildMapValue() {
        Map<Integer, Long> incomeExpenseValueInMonth = new HashMap<>();
        int currentYear = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        for (ExpenseIncome expenseIncome : expenseIncomes) {
            LocalDate createdDate = expenseIncome.getCreateOn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = createdDate.getMonthValue();
            if (createdDate.getYear() != currentYear) {
                continue;
            }

            long amount = expenseIncome.getAmount();
            if (incomeExpenseValueInMonth.containsKey(month)) {
                long prevAmount = incomeExpenseValueInMonth.get(month);
                if (expenseIncome.getOperationType() == OperationType.EXPENSE) {
                    incomeExpenseValueInMonth.put(month, prevAmount - amount);
                    // minus because this is expense
                }
                if (expenseIncome.getOperationType() == OperationType.INCOME) {
                    incomeExpenseValueInMonth.put(month, prevAmount + amount);
                    // plus because this is income
                }
            } else {
                if (expenseIncome.getOperationType() == OperationType.INCOME) {
                    incomeExpenseValueInMonth.put(month, expenseIncome.getAmount());
                }
                if (expenseIncome.getOperationType() == OperationType.EXPENSE) {
                    incomeExpenseValueInMonth.put(month, -expenseIncome.getAmount());
                }
            }
        }

        return incomeExpenseValueInMonth;
    }
}
