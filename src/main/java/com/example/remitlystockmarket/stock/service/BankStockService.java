package com.example.remitlystockmarket.stock.service;

import com.example.remitlystockmarket.stock.dto.StockDto;
import com.example.remitlystockmarket.stock.entity.BankStockEntity;
import com.example.remitlystockmarket.stock.repository.BankStockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankStockService {
    private final BankStockRepository bankStockRepository;

    public BankStockService(BankStockRepository bankStockRepository) {
        this.bankStockRepository = bankStockRepository;
    }

    public StockDto getBankState() {
        List<StockDto.StockItem> stocks = bankStockRepository.findAll().stream()
                .map(entity -> new StockDto.StockItem(entity.getName(), entity.getQuantity()))
                .toList();
        return new StockDto(stocks);
    }

    public StockDto setBankState(StockDto stockDto) {
        bankStockRepository.deleteAll();
        List<BankStockEntity> entities = stockDto.stocks().stream()
                .map(item -> {
                    BankStockEntity entity = new BankStockEntity();
                    entity.setName(item.name());
                    entity.setQuantity(item.quantity());
                    return entity;
                })
                .toList();
        bankStockRepository.saveAll(entities);
        return getBankState();
    }
}
