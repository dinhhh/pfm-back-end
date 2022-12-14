package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void insertTest() {
        User user = new User();
        user.setAddress("Hanoi");
        user.setEmail("email");
        user.setPhone("0866827702");
        user.setPassword("pw");
        repository.save(user);
    }
}