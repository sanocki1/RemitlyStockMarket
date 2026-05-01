package com.example.remitlystockmarket.wallet.repository;

import com.example.remitlystockmarket.wallet.entity.WalletStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletStockRepository extends JpaRepository<WalletStockEntity, Long> {
    Optional<WalletStockEntity> findByWallet_IdAndName(String walletId, String name);
    List<WalletStockEntity> findByWallet_Id(String walletId);
}
