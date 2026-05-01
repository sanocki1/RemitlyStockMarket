package com.example.remitlystockmarket.wallet.dto;

import java.util.List;

public record WalletStockResponseDto(String id, List<StockItem> stocks) {
    public record StockItem(String name, int quantity) {}
}