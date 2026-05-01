package com.example.remitlystockmarket.wallet.controller;

import com.example.remitlystockmarket.wallet.dto.WalletStockResponseDto;
import com.example.remitlystockmarket.wallet.dto.WalletTransactionRequestDto;
import com.example.remitlystockmarket.wallet.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{wallet_id}")
    public ResponseEntity<WalletStockResponseDto> getWalletById(@PathVariable String wallet_id) {
        return walletService.getWalletById(wallet_id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("{wallet_id}/stocks/{stock_name}")
    public int getWalletStockQuantity(@PathVariable String wallet_id, @PathVariable String stock_name) {
        return walletService.getWalletStockQuantity(wallet_id, stock_name);
    }

    @PostMapping("/{wallet_id}/stocks/{stock_name}")
    public ResponseEntity<Void> createStockTransaction(@PathVariable String wallet_id,
                                                       @PathVariable String stock_name,
                                                       @RequestBody WalletTransactionRequestDto transactionDto) {
        walletService.createStockTransaction(wallet_id, stock_name, transactionDto);
        return ResponseEntity.ok().build();
    }
}
