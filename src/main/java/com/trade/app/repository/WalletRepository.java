package com.trade.app.repository;

import com.trade.app.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {

    Wallet findByUserId(Long userId);

}
