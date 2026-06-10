package com.example.remitlystockmarket.unit;

import com.example.remitlystockmarket.exception.InsufficientStockException;
import com.example.remitlystockmarket.exception.ResourceAlreadyExistsException;
import com.example.remitlystockmarket.exception.ResourceNotFoundException;
import com.example.remitlystockmarket.stock.dto.StockDto;
import com.example.remitlystockmarket.stock.entity.BankStockEntity;
import com.example.remitlystockmarket.stock.repository.BankStockRepository;
import com.example.remitlystockmarket.stock.service.BankStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankStockServiceTest {

    private BankStockRepository bankStockRepository;
    private BankStockService bankStockService;

    @BeforeEach
    void setup() {
        bankStockRepository = Mockito.mock(BankStockRepository.class);
        bankStockService = new BankStockService(bankStockRepository);
    }

    @Test
    @DisplayName("Should return bank stock state")
    void testGetBankState() {
        BankStockEntity stock1 = new BankStockEntity();
        stock1.setName("AAPL");
        stock1.setQuantity(100);
        BankStockEntity stock2 = new BankStockEntity();
        stock2.setName("GOOGL");
        stock2.setQuantity(50);

        when(bankStockRepository.findAll()).thenReturn(List.of(stock1, stock2));
        StockDto result = bankStockService.getBankState();

        assertEquals(2, result.stocks().size());
        assertEquals("AAPL", result.stocks().get(0).name());
        assertEquals(100, result.stocks().get(0).quantity());
        assertEquals("GOOGL", result.stocks().get(1).name());
        assertEquals(50, result.stocks().get(1).quantity());
        verify(bankStockRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should set bank stock state when repository is empty")
    void testSetBankState() {
        StockDto.StockItem item1 = new StockDto.StockItem("AAPL", 100);
        StockDto.StockItem item2 = new StockDto.StockItem("GOOGL", 50);
        StockDto input = new StockDto(List.of(item1, item2));

        when(bankStockRepository.count()).thenReturn(0L);
        when(bankStockRepository.findAll()).thenReturn(List.of(
                new BankStockEntity("AAPL", 100),
                new BankStockEntity("GOOGL", 50)
        ));
        StockDto result = bankStockService.setBankState(input);

        assertEquals(2, result.stocks().size());
        assertEquals("AAPL", result.stocks().get(0).name());
        assertEquals(100, result.stocks().get(0).quantity());
        assertEquals("GOOGL", result.stocks().get(1).name());
        assertEquals(50, result.stocks().get(1).quantity());
        verify(bankStockRepository, times(1)).count();
        verify(bankStockRepository, times(1)).saveAll(anyList());
        verify(bankStockRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should throw exception when setting bank stock state with existing state")
        void testSetBankStateWithExistingState() {
            StockDto.StockItem item1 = new StockDto.StockItem("AAPL", 100);
            StockDto input = new StockDto(List.of(item1));

            when(bankStockRepository.count()).thenReturn(1L);
            assertThrows(ResourceAlreadyExistsException.class, () -> bankStockService.setBankState(input));
            verify(bankStockRepository, times(1)).count();
            verify(bankStockRepository, never()).saveAll(anyList());
        }

        @Test
        @DisplayName("Should add new stock to bank")
        void testAddStock() {
            when(bankStockRepository.existsById("AAPL")).thenReturn(false);
            when(bankStockRepository.findAll()).thenReturn(List.of(
                    new BankStockEntity("AAPL", 100)
            ));

            StockDto result = bankStockService.addStock("AAPL", 100);

            assertEquals(1, result.stocks().size());
            assertEquals("AAPL", result.stocks().get(0).name());
            assertEquals(100, result.stocks().get(0).quantity());
            verify(bankStockRepository, times(1)).existsById("AAPL");
            verify(bankStockRepository, times(1)).save(any(BankStockEntity.class));
            verify(bankStockRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should throw exception when adding stock that already exists")
        void testAddStockThrowsExceptionWhenStockExists() {
            when(bankStockRepository.existsById("AAPL")).thenReturn(true);

            assertThrows(ResourceAlreadyExistsException.class, () -> bankStockService.addStock("AAPL", 100));
            verify(bankStockRepository, times(1)).existsById("AAPL");
            verify(bankStockRepository, never()).save(any(BankStockEntity.class));
        }

        @Test
        @DisplayName("Should check if stock exists")
        void testDoesStockExist() {
            when(bankStockRepository.existsById("AAPL")).thenReturn(true);
            boolean exists = bankStockService.doesStockExist("AAPL");
            assertTrue(exists);
            verify(bankStockRepository, times(1)).existsById("AAPL");
        }

        @Test
        @DisplayName("Should check if stock doesn't exist")
        void testDoesStockNotExist() {
            when(bankStockRepository.existsById("AAPL")).thenReturn(false);
            boolean exists = bankStockService.doesStockExist("AAPL");
            assertFalse(exists);
            verify(bankStockRepository, times(1)).existsById("AAPL");
        }

        @Test
        @DisplayName("Should change stock quantity")
        void testChangeStockQuantity() {
            BankStockEntity stock = new BankStockEntity();
            stock.setName("AAPL");
            stock.setQuantity(100);

            when(bankStockRepository.findById("AAPL")).thenReturn(Optional.of(stock));
            bankStockService.changeStockQuantity("AAPL", -10);
            assertEquals(90, stock.getQuantity());
            verify(bankStockRepository, times(1)).findById("AAPL");
            verify(bankStockRepository, times(1)).save(stock);
        }

        @Test
        @DisplayName("Should throw exception when changing stock quantity of non-existent stock")
        void testChangeStockQuantityNonExistent() {
            when(bankStockRepository.findById("AAPL")).thenReturn(Optional.empty());
            assertThrows(ResourceNotFoundException.class, () -> bankStockService.changeStockQuantity("AAPL", -10));
            verify(bankStockRepository, times(1)).findById("AAPL");
        }

        @Test
        @DisplayName("Should throw exception when changing stock quantity to negative")
        void testChangeStockQuantityToNegative() {
            BankStockEntity stock = new BankStockEntity();
            stock.setName("AAPL");
            stock.setQuantity(5);

            when(bankStockRepository.findById("AAPL")).thenReturn(Optional.of(stock));
            assertThrows(InsufficientStockException.class, () -> bankStockService.changeStockQuantity("AAPL", -10));
        }
}
