package com.example.remitlystockmarket.log.controller;

import com.example.remitlystockmarket.log.dto.LogResponseDto;
import com.example.remitlystockmarket.log.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
@Tag(name = "Log Controller", description = "Endpoints for retrieving logs of stock transactions")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    @Operation(summary = "Get All Logs", description = "Returns a list of all stock transaction logs")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all logs")
    public ResponseEntity<LogResponseDto> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }
}
