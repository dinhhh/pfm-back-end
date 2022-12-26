package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.Category;
import com.hust.pfmbackend.entity.OperationType;
import com.hust.pfmbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query(value = "select c from Category c where c.operationType = ?1 and (c.user = null or c.user = ?2)")
    List<Category> findCategorise(OperationType ot, User user);

}
