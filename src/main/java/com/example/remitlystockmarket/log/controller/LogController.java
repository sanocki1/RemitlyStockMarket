package com.example.remitlystockmarket.log.controller;

import com.example.remitlystockmarket.log.dto.LogResponseDto;
import com.example.remitlystockmarket.log.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<LogResponseDto> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }
}
