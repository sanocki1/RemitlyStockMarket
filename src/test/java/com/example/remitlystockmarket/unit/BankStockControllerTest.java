package com.example.remitlystockmarket.unit;

import com.example.remitlystockmarket.stock.controller.BankStockController;
import com.example.remitlystockmarket.stock.dto.StockDto;
import com.example.remitlystockmarket.stock.service.BankStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BankStockControllerTest {

    private BankStockService bankStockService;
    private BankStockController bankStockController;

    @BeforeEach
    void setup() {
        bankStockService = Mockito.mock(BankStockService.class);
        bankStockController = new BankStockController(bankStockService);
    }

    @Test
    @DisplayName("Should get bank stock state")
    void testGetBankState() {
        StockDto.StockItem item1 = new StockDto.StockItem("AAPL", 100);
        StockDto.StockItem item2 = new StockDto.StockItem("GOOGL", 50);
        StockDto expected = new StockDto(List.of(item1, item2));

        when(bankStockService.getBankState()).thenReturn(expected);

        ResponseEntity<StockDto> result = bankStockController.getBankState();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(expected, result.getBody());
        assertEquals(2, result.getBody().stocks().size());
        assertEquals("AAPL", result.getBody().stocks().get(0).name());
        assertEquals(100, result.getBody().stocks().get(0).quantity());
        assertEquals("GOOGL", result.getBody().stocks().get(1).name());
        assertEquals(50, result.getBody().stocks().get(1).quantity());
        verify(bankStockService, times(1)).getBankState();
    }

    @Test
    @DisplayName("Should set bank stock state")
    void testSetBankState() {
        StockDto.StockItem item1 = new StockDto.StockItem("AAPL", 100);
        StockDto input = new StockDto(List.of(item1));

        when(bankStockService.setBankState(input)).thenReturn(input);

        ResponseEntity<StockDto> result = bankStockController.setBankState(input);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(input, result.getBody());
        verify(bankStockService, times(1)).setBankState(input);
    }

    @Test
    @DisplayName("Should add stock to bank inventory")
    void testAddStock() {
        StockDto.StockItem item1 = new StockDto.StockItem("AAPL", 100);
        StockDto expected = new StockDto(List.of(item1));

        when(bankStockService.addStock("AAPL", 100)).thenReturn(expected);

        ResponseEntity<StockDto> result = bankStockController.addStock("AAPL", 100);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(expected, result.getBody());
        verify(bankStockService, times(1)).addStock("AAPL", 100);
    }
}