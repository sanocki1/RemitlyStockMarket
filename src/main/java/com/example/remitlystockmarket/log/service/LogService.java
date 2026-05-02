package com.example.remitlystockmarket.log.service;

import com.example.remitlystockmarket.log.dto.LogResponseDto;
import com.example.remitlystockmarket.log.entity.LogEntity;
import com.example.remitlystockmarket.log.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logTransaction(String type, String walletId, String stockName) {
        LogEntity logEntry = new LogEntity(type, walletId, stockName);
        logRepository.save(logEntry);
    }

    public LogResponseDto getAllLogs() {
        List<LogEntity> log = logRepository.findAll();
        List<LogResponseDto.LogEntry> logEntries = log.stream()
                .map(entry -> new LogResponseDto.LogEntry(entry.getType(), entry.getWallet_id(), entry.getStock_name()))
                .toList();
        return new LogResponseDto(logEntries);
    }
}
