package com.example.remitlystockmarket.log.dto;

import java.util.List;

public record LogResponseDto(List<LogEntry> log) {
    public record LogEntry(String type, String wallet_id, String stock_name) {}
}