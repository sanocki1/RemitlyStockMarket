package com.example.remitlystockmarket.stock.controller;

import com.example.remitlystockmarket.stock.dto.StockDto;
import com.example.remitlystockmarket.stock.service.BankStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
public class BankStockController {
    private final BankStockService bankStockService;

    public BankStockController(BankStockService bankStockService) {
        this.bankStockService = bankStockService;
    }

    @GetMapping
    public ResponseEntity<StockDto> getBankState() {
        StockDto bankState = bankStockService.getBankState();
        return ResponseEntity.ok(bankState);
    }

    @PostMapping
    public ResponseEntity<StockDto> setBankState(@RequestBody StockDto stockDto) {
        StockDto bankState = bankStockService.setBankState(stockDto);
        return ResponseEntity.ok(bankState);
    }
}
