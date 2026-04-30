package com.example.remitlystockmarket.stock.repository;

import com.example.remitlystockmarket.stock.entity.BankStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankStockRepository extends JpaRepository<BankStockEntity, Long> {
}
