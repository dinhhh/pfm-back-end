package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    User findUserByEmail(String email);
}
