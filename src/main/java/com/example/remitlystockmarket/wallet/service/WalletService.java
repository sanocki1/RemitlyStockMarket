package com.example.remitlystockmarket.wallet.service;

import com.example.remitlystockmarket.exception.InsufficientStockException;
import com.example.remitlystockmarket.exception.ResourceNotFoundException;
import com.example.remitlystockmarket.log.service.LogService;
import com.example.remitlystockmarket.stock.service.BankStockService;
import com.example.remitlystockmarket.wallet.dto.WalletStockResponseDto;
import com.example.remitlystockmarket.wallet.dto.WalletTransactionRequestDto;
import com.example.remitlystockmarket.wallet.entity.WalletEntity;
import com.example.remitlystockmarket.wallet.entity.WalletStockEntity;
import com.example.remitlystockmarket.wallet.repository.WalletRepository;
import com.example.remitlystockmarket.wallet.repository.WalletStockRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletStockRepository walletStockRepository;
    private final BankStockService bankStockService;
    private final LogService logService;

    public WalletService(WalletRepository walletRepository, WalletStockRepository walletStockRepository,
                         BankStockService bankStockService, LogService logService) {
        this.walletRepository = walletRepository;
        this.walletStockRepository = walletStockRepository;
        this.bankStockService = bankStockService;
        this.logService = logService;
    }

    public Optional<WalletStockResponseDto> getWalletById(String walletId) {
        return walletRepository.findById(walletId)
                .map(wallet -> {
                    List<WalletStockEntity> stocks = walletStockRepository.findByWallet_Id(wallet.getId());
                    List<WalletStockResponseDto.StockItem> stockItems = stocks.stream()
                            .map(stock -> new WalletStockResponseDto.StockItem(stock.getName(), stock.getQuantity()))
                            .toList();
                    return new WalletStockResponseDto(walletId, stockItems);
                });
    }

    public int getWalletStockQuantity(String walletId, String stockName) {
        return walletStockRepository
                .findByWallet_IdAndName(walletId, stockName)
                .map(WalletStockEntity::getQuantity)
                .orElse(0);
    }

    @Transactional
    public void createStockTransaction(String walletId, String stockName, WalletTransactionRequestDto transactionDto) {
        if (!bankStockService.doesStockExist(stockName)) {
            throw new ResourceNotFoundException("Stock not found");
        }

        WalletEntity wallet = walletRepository.findById(walletId)
                .orElseGet(() -> walletRepository.save(new WalletEntity(walletId)));

        if (transactionDto.type().equals("buy")) {
            bankStockService.changeStockQuantity(stockName, -1);
            changeWalletStockQuantity(wallet, stockName, +1);
            logService.logTransaction("buy", walletId, stockName);
        } else if (transactionDto.type().equals("sell")) {
            changeWalletStockQuantity(wallet, stockName, -1);
            bankStockService.changeStockQuantity(stockName, +1);
            logService.logTransaction("sell", walletId, stockName);
        }
    }

    public void changeWalletStockQuantity(WalletEntity wallet, String stockName, int delta){
        WalletStockEntity stock = walletStockRepository.findByWallet_IdAndName(wallet.getId(), stockName)
                .orElse(new WalletStockEntity(stockName, 0, wallet));

        int currentQuantity = stock.getQuantity();
        if (currentQuantity + delta < 0) {
            throw new InsufficientStockException("No stock available");
    }
        stock.setQuantity(currentQuantity + delta);
        walletStockRepository.save(stock);
    }
}
