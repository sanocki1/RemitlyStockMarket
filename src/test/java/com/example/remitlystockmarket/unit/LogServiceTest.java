package com.example.remitlystockmarket.unit;

import com.example.remitlystockmarket.log.dto.LogResponseDto;
import com.example.remitlystockmarket.log.entity.LogEntity;
import com.example.remitlystockmarket.log.repository.LogRepository;
import com.example.remitlystockmarket.log.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class LogServiceTest {

    private LogRepository logRepository;
    private LogService logService;

    @BeforeEach
    void setup() {
        logRepository = Mockito.mock(LogRepository.class);
        logService = new LogService(logRepository);
    }

    @Test
    @DisplayName("Should log a transaction")
    void testLogTransaction() {
        logService.logTransaction("BUY", "wallet123", "AAPL");

        Mockito.verify(logRepository, times(1)).save(Mockito.argThat(logEntry ->
                logEntry.getType().equals("BUY") &&
                logEntry.getWallet_id().equals("wallet123") &&
                logEntry.getStock_name().equals("AAPL")
        ));
    }

    @Test
    @DisplayName("Should return all logs")
    void testGetAllLogs() {
        LogEntity log1 = new LogEntity("BUY", "wallet123", "AAPL");
        LogEntity log2 = new LogEntity("SELL", "wallet456", "GOOGL");
        when(logRepository.findAll()).thenReturn(List.of(log1, log2));

        LogResponseDto result = logService.getAllLogs();

        assertEquals(2, result.log().size());
        assertEquals("BUY", result.log().get(0).type());
        assertEquals("wallet123", result.log().get(0).wallet_id());
        assertEquals("AAPL", result.log().get(0).stock_name());
        assertEquals("SELL", result.log().get(1).type());
        assertEquals("wallet456", result.log().get(1).wallet_id());
        assertEquals("GOOGL", result.log().get(1).stock_name());
        Mockito.verify(logRepository, times(1)).findAll();
    }
}
