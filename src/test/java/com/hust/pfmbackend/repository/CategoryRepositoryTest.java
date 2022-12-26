package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.Category;
import com.hust.pfmbackend.entity.OperationType;
import com.hust.pfmbackend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        Category defaultCategory = Category.builder()
                .name("default category")
                .operationType(OperationType.EXPENSE)
                .createOn(new Date(System.currentTimeMillis()))
                .build();
        repository.save(defaultCategory);
    }

    @Test
    void getAll() {
        User user = userRepository.findUserByEmail("admin");
        List<Category> categories = repository.findCategorise(OperationType.INCOME, user);
        System.out.println(String.format("Category size %d", categories.size()));
        for (Category category : categories) {
            System.out.println(category);
        }
    }

}
