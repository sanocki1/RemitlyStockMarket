package com.example.remitlystockmarket.unit;

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
import com.example.remitlystockmarket.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    private WalletRepository walletRepository;
    private WalletStockRepository walletStockRepository;
    private WalletService walletService;
    private BankStockService bankStockService;
    private LogService logService;

    @BeforeEach
    void setup() {
        walletRepository = Mockito.mock(WalletRepository.class);
        walletStockRepository = Mockito.mock(WalletStockRepository.class);
        bankStockService = Mockito.mock(BankStockService.class);
        logService = Mockito.mock(LogService.class);
        walletService = new WalletService(walletRepository, walletStockRepository, bankStockService, logService);
    }

    @Test
    @DisplayName("Should get wallet by ID")
    void testGetWalletById() {
        WalletEntity wallet = new WalletEntity("wallet123");
        WalletStockEntity stock1 = new WalletStockEntity("AAPL", 10, wallet);
        WalletStockEntity stock2 = new WalletStockEntity("GOOGL", 5, wallet);

        when(walletRepository.findById("wallet123")).thenReturn(Optional.of(wallet));
        when(walletStockRepository.findByWallet_Id("wallet123")).thenReturn(List.of(stock1, stock2));

        WalletStockResponseDto result = walletService.getWalletById("wallet123");

        assertEquals("wallet123", result.id());
        assertEquals(2, result.stocks().size());
        assertEquals("AAPL", result.stocks().get(0).name());
        assertEquals(10, result.stocks().get(0).quantity());
        assertEquals("GOOGL", result.stocks().get(1).name());
        assertEquals(5, result.stocks().get(1).quantity());
        verify(walletRepository, times(1)).findById("wallet123");
        verify(walletStockRepository, times(1)).findByWallet_Id("wallet123");
    }

    @Test
    @DisplayName("Should throw exception when wallet not found by id")
    void testGetWalledByIdNotFound() {
        when(walletRepository.findById("wallet123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> walletService.getWalletById("wallet123"));
        verify(walletRepository, times(1)).findById("wallet123");
    }

    @Test
    @DisplayName("Should return wallet stock quantity")
    void testGetWalletStockQuantity() {
        WalletEntity wallet = new WalletEntity("wallet123");
        WalletStockEntity stock = new WalletStockEntity("AAPL", 5, wallet);

        when(walletRepository.existsById("wallet123")).thenReturn(true);
        when(bankStockService.doesStockExist("AAPL")).thenReturn(true);
        when(walletStockRepository.findByWallet_IdAndName("wallet123", "AAPL"))
                .thenReturn(Optional.of(stock));

        int quantity = walletService.getWalletStockQuantity("wallet123", "AAPL");

        assertEquals(5, quantity);
        verify(walletRepository, times(1)).existsById("wallet123");
        verify(bankStockService, times(1)).doesStockExist("AAPL");
        verify(walletStockRepository, times(1)).findByWallet_IdAndName("wallet123", "AAPL");
    }

    @Test
    @DisplayName("Should return 0 when wallet stock doesn't exist")
    void testGetWalletStockQuantityNotFound() {
        when(walletRepository.existsById("wallet123")).thenReturn(true);
        when(bankStockService.doesStockExist("AAPL")).thenReturn(true);
        when(walletStockRepository.findByWallet_IdAndName("wallet123", "AAPL"))
                .thenReturn(Optional.empty());

        int quantity = walletService.getWalletStockQuantity("wallet123", "AAPL");

        assertEquals(0, quantity);
    }

    @Test
    @DisplayName("Should throw exception when wallet not found for stock quantity")
    void testGetWalletStockQuantityWalletNotFound() {
        when(walletRepository.existsById("wallet999")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> walletService.getWalletStockQuantity("wallet999", "AAPL"));
    }

    @Test
    @DisplayName("Should throw exception when stock doesn't exist")
    void testGetWalletStockQuantityStockNotFound() {
        when(walletRepository.existsById("wallet123")).thenReturn(true);
        when(bankStockService.doesStockExist("INVALID")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> walletService.getWalletStockQuantity("wallet123", "INVALID"));
    }

    @Test
    @DisplayName("Should create buy transaction successfully")
    void testCreateStockTransactionBuy() {
        WalletEntity wallet = new WalletEntity("wallet123");
        WalletTransactionRequestDto request = new WalletTransactionRequestDto("buy");

        when(bankStockService.doesStockExist("AAPL")).thenReturn(true);
        when(walletRepository.findById("wallet123")).thenReturn(Optional.of(wallet));
        when(walletStockRepository.findByWallet_IdAndName("wallet123", "AAPL"))
                .thenReturn(Optional.empty());

        walletService.createStockTransaction("wallet123", "AAPL", request);

        verify(bankStockService, times(1)).doesStockExist("AAPL");
        verify(walletRepository, times(1)).findById("wallet123");
        verify(bankStockService, times(1)).changeStockQuantity("AAPL", -1);
        verify(walletStockRepository, times(1)).save(argThat(stock ->
                stock.getName().equals("AAPL") && stock.getQuantity() == 1));
        verify(logService, times(1)).logTransaction("buy", "wallet123", "AAPL");
    }

    @Test
    @DisplayName("Should create sell transaction successfully")
    void testCreateStockTransactionSell() {
        WalletEntity wallet = new WalletEntity("wallet123");
        WalletStockEntity walletStock = new WalletStockEntity("AAPL", 5, wallet);
        WalletTransactionRequestDto request = new WalletTransactionRequestDto("sell");

        when(bankStockService.doesStockExist("AAPL")).thenReturn(true);
        when(walletRepository.findById("wallet123")).thenReturn(Optional.of(wallet));
        when(walletStockRepository.findByWallet_IdAndName("wallet123", "AAPL"))
                .thenReturn(Optional.of(walletStock));

        walletService.createStockTransaction("wallet123", "AAPL", request);

        verify(bankStockService, times(1)).doesStockExist("AAPL");
        verify(walletRepository, times(1)).findById("wallet123");
        verify(bankStockService, times(1)).changeStockQuantity("AAPL", +1);
        verify(walletStockRepository, times(1)).save(argThat(stock ->
                stock.getName().equals("AAPL") && stock.getQuantity() == 4));
        verify(logService, times(1)).logTransaction("sell", "wallet123", "AAPL");
    }

    @Test
    @DisplayName("Should create new wallet on first buy transaction")
    void testCreateStockTransactionNewWallet() {
        WalletEntity newWallet = new WalletEntity("wallet456");
        WalletTransactionRequestDto request = new WalletTransactionRequestDto("buy");

        when(bankStockService.doesStockExist("AAPL")).thenReturn(true);
        when(walletRepository.findById("wallet456")).thenReturn(Optional.empty());
        when(walletRepository.save(any(WalletEntity.class))).thenReturn(newWallet);
        when(walletStockRepository.findByWallet_IdAndName("wallet456", "AAPL"))
                .thenReturn(Optional.empty());

        walletService.createStockTransaction("wallet456", "AAPL", request);

        verify(walletRepository, times(1)).save(argThat(wallet -> wallet.getId().equals("wallet456")));
        verify(bankStockService, times(1)).changeStockQuantity("AAPL", -1);
    }

    @Test
    @DisplayName("Should throw exception for invalid transaction type")
    void testCreateStockTransactionInvalidType() {
        WalletTransactionRequestDto request = new WalletTransactionRequestDto("invalid");

        when(bankStockService.doesStockExist("AAPL")).thenReturn(true);
        when(walletRepository.findById("wallet123")).thenReturn(Optional.of(new WalletEntity("wallet123")));
        when(walletStockRepository.findByWallet_IdAndName("wallet123", "AAPL"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> walletService.createStockTransaction("wallet123", "AAPL", request));
    }

    @Test
    @DisplayName("Should throw exception when stock not found for transaction")
    void testCreateStockTransactionStockNotFound() {
        WalletTransactionRequestDto request = new WalletTransactionRequestDto("buy");

        when(bankStockService.doesStockExist("INVALID")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> walletService.createStockTransaction("wallet123", "INVALID", request));
    }

    @Test
    @DisplayName("Should change wallet stock quantity up")
    void testChangeWalletStockQuantityIncrease() {
        WalletEntity wallet = new WalletEntity("wallet123");
        WalletStockEntity stock = new WalletStockEntity("AAPL", 5, wallet);

        when(walletStockRepository.findByWallet_IdAndName("wallet123", "AAPL"))
                .thenReturn(Optional.of(stock));

        walletService.changeWalletStockQuantity(wallet, "AAPL", 3);

        assertEquals(8, stock.getQuantity());
        verify(walletStockRepository, times(1)).save(stock);
    }

    @Test
    @DisplayName("Should change wallet stock quantity down")
    void testChangeWalletStockQuantityDecrease() {
        WalletEntity wallet = new WalletEntity("wallet123");
        WalletStockEntity stock = new WalletStockEntity("AAPL", 5, wallet);

        when(walletStockRepository.findByWallet_IdAndName("wallet123", "AAPL"))
                .thenReturn(Optional.of(stock));

        walletService.changeWalletStockQuantity(wallet, "AAPL", -2);

        assertEquals(3, stock.getQuantity());
        verify(walletStockRepository, times(1)).save(stock);
    }

    @Test
    @DisplayName("Should create new wallet stock entry if not exists")
    void testChangeWalletStockQuantityNewEntry() {
        WalletEntity wallet = new WalletEntity("wallet123");

        when(walletStockRepository.findByWallet_IdAndName("wallet123", "AAPL"))
                .thenReturn(Optional.empty());

        walletService.changeWalletStockQuantity(wallet, "AAPL", 5);

        verify(walletStockRepository, times(1)).save(argThat(stock ->
                stock.getName().equals("AAPL") &&
                        stock.getQuantity() == 5 &&
                        stock.getWallet().getId().equals("wallet123")));
    }

    @Test
    @DisplayName("Should throw exception when quantity goes negative")
    void testChangeWalletStockQuantityNegative() {
        WalletEntity wallet = new WalletEntity("wallet123");
        WalletStockEntity stock = new WalletStockEntity("AAPL", 3, wallet);

        when(walletStockRepository.findByWallet_IdAndName("wallet123", "AAPL"))
                .thenReturn(Optional.of(stock));

        assertThrows(InsufficientStockException.class,
                () -> walletService.changeWalletStockQuantity(wallet, "AAPL", -5));
    }
}
