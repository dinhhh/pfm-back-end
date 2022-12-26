package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.ExpenseIncome;
import com.hust.pfmbackend.entity.OperationType;
import com.hust.pfmbackend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class ExpenseIncomeRepositoryTest {

    @Autowired
    private ExpenseIncomeRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() throws Exception {
        User admin = userRepository.findUserByEmail("admin");
        if (admin == null) {
            throw new Exception("Admin is null");
        }
        ExpenseIncome record = ExpenseIncome.builder()
                .amount(5000)
                .createOn(new Date())
                .description("miêu tả để kiểm tra utf-8")
                .user(admin)
                .operationType(OperationType.INCOME)
                .build();
        repository.save(record);
    }

    @Test
    void findAll() {
        List<ExpenseIncome> list = repository.findAll();
        for (ExpenseIncome ei : list) {
            System.out.println(ei);
        }
        System.out.println("Done");
    }
}
