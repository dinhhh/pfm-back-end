package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.UserDebtInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDebtInfoRepository extends JpaRepository<UserDebtInfo, String> {

    List<UserDebtInfo> findAllByUserNo(String userNo);

}
