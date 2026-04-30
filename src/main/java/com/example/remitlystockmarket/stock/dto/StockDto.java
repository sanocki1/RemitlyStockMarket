package com.example.remitlystockmarket.stock.dto;

import java.util.List;

public record StockDto(List<StockItem> stocks) {
    public record StockItem(String name, int quantity) {}
}
