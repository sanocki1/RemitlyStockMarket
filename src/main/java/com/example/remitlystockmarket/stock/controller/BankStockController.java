package com.example.remitlystockmarket.stock.controller;

import com.example.remitlystockmarket.stock.dto.StockDto;
import com.example.remitlystockmarket.stock.service.BankStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
@Tag(name = "Bank Stock Controller", description = "Endpoints for managing the bank's stock inventory")
public class BankStockController {
    private final BankStockService bankStockService;

    public BankStockController(BankStockService bankStockService) {
        this.bankStockService = bankStockService;
    }

    @GetMapping
    @Operation(summary = "Get Bank Stock State", description = "Returns the current state of the bank's stock inventory")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved bank stock state")
    public ResponseEntity<StockDto> getBankState() {
        StockDto bankState = bankStockService.getBankState();
        return ResponseEntity.ok(bankState);
    }

    @PostMapping("/state")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Set Bank Stock State", description = "Sets the bank's stock inventory to the provided state, overrides the previous state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set bank stock state"),
            @ApiResponse(responseCode = "409", description = "Bank stock state already exists")
    })
    public ResponseEntity<StockDto> setBankState(@RequestBody StockDto stockDto) {
        StockDto bankState = bankStockService.setBankState(stockDto);
        return ResponseEntity.ok(bankState);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add Stock to Bank Inventory", description = "Adds a new stock to the bank's inventory with the specified quantity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added stock to bank inventory"),
            @ApiResponse(responseCode = "409", description = "Stock already exists in bank inventory")
    })
    public ResponseEntity<StockDto> addStock(@RequestParam String name, @RequestParam int quantity) {
        StockDto bankState = bankStockService.addStock(name, quantity);
        return ResponseEntity.ok(bankState);
    }
}
