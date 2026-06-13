package com.example.remitlystockmarket.unit;

import com.example.remitlystockmarket.wallet.controller.WalletController;
import com.example.remitlystockmarket.wallet.dto.WalletStockResponseDto;
import com.example.remitlystockmarket.wallet.dto.WalletTransactionRequestDto;
import com.example.remitlystockmarket.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WalletControllerTest {

    private WalletService walletService;
    private WalletController walletController;

    @BeforeEach
    void setup() {
        walletService = Mockito.mock(WalletService.class);
        walletController = new WalletController(walletService);
    }

    @Test
    @DisplayName("Should get wallet by ID")
    void testGetWalletById() {
        WalletStockResponseDto.StockItem stock1 = new WalletStockResponseDto.StockItem("AAPL", 10);
        WalletStockResponseDto.StockItem stock2 = new WalletStockResponseDto.StockItem("GOOGL", 5);
        WalletStockResponseDto wallet = new WalletStockResponseDto("wallet123", List.of(stock1, stock2));

        when(walletService.getWalletById("wallet123")).thenReturn(wallet);

        ResponseEntity<WalletStockResponseDto> result = walletController.getWalletById("wallet123");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(wallet, result.getBody());
        assertEquals("wallet123", result.getBody().id());
        assertEquals(2, result.getBody().stocks().size());
        assertEquals("AAPL", result.getBody().stocks().get(0).name());
        assertEquals(10, result.getBody().stocks().get(0).quantity());
        assertEquals("GOOGL", result.getBody().stocks().get(1).name());
        assertEquals(5, result.getBody().stocks().get(1).quantity());
        verify(walletService, times(1)).getWalletById("wallet123");
    }

    @Test
    @DisplayName("Should get wallet stock quantity")
    void testGetWalletStockQuantity() {
        when(walletService.getWalletStockQuantity("wallet123", "AAPL")).thenReturn(7);

        ResponseEntity<Integer> result = walletController.getWalletStockQuantity("wallet123", "AAPL");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(7, result.getBody());
        verify(walletService, times(1)).getWalletStockQuantity("wallet123", "AAPL");
    }

    @Test
    @DisplayName("Should create stock transaction")
    void testCreateStockTransaction() {
        WalletTransactionRequestDto request = new WalletTransactionRequestDto("buy");

        ResponseEntity<Void> result = walletController.createStockTransaction("wallet123", "AAPL", request);

        assertEquals(200, result.getStatusCode().value());
        verify(walletService, times(1)).createStockTransaction("wallet123", "AAPL", request);
    }
}