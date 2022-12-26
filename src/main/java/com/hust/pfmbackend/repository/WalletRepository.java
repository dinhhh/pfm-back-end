package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {

    List<Wallet> findAllByUser(User user);

}
