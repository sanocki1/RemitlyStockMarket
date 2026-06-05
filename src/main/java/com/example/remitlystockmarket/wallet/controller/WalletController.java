package com.example.remitlystockmarket.wallet.controller;

import com.example.remitlystockmarket.wallet.dto.WalletStockResponseDto;
import com.example.remitlystockmarket.wallet.dto.WalletTransactionRequestDto;
import com.example.remitlystockmarket.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
@Tag(name = "Wallet Controller", description = "Endpoints for managing user wallets and their stock transactions")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{wallet_id}")
    @Operation(summary = "Get Wallet by ID", description = "Returns stock holdings of a wallet by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved wallet information"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    public ResponseEntity<WalletStockResponseDto> getWalletById(@PathVariable String wallet_id) {
        WalletStockResponseDto wallet = walletService.getWalletById(wallet_id);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("{wallet_id}/stocks/{stock_name}")
    @Operation(summary = "Get Wallet Stock Quantity", description = "Returns the quantity of a specific stock in a wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stock quantity"),
            @ApiResponse(responseCode = "404", description = "Wallet or stock not found")
    })
    public ResponseEntity<Integer> getWalletStockQuantity(@PathVariable String wallet_id,
                                                          @PathVariable String stock_name) {
        Integer walletStockQuantity = walletService.getWalletStockQuantity(wallet_id, stock_name);
        return ResponseEntity.ok(walletStockQuantity);
    }

    @PostMapping("/{wallet_id}/stocks/{stock_name}")
    @Operation(summary = "Create Stock Transaction", description = "Creates a stock transaction (buy/sell) for a specific stock in a wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created stock transaction"),
            @ApiResponse(responseCode = "404", description = "Stock not found"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction type or insufficient stock quantity")
    })
    public ResponseEntity<Void> createStockTransaction(@PathVariable String wallet_id,
                                                       @PathVariable String stock_name,
                                                       @RequestBody WalletTransactionRequestDto transactionDto) {
        walletService.createStockTransaction(wallet_id, stock_name, transactionDto);
        return ResponseEntity.ok().build();
    }
}
