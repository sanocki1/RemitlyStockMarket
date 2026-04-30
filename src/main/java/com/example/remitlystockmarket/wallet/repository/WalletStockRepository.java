package com.example.remitlystockmarket.wallet.repository;

import com.example.remitlystockmarket.wallet.entity.WalletStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletStockRepository extends JpaRepository<WalletStockEntity, Long> {
}
