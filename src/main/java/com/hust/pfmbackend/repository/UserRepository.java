package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
