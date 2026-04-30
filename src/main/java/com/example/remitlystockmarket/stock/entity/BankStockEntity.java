package com.example.remitlystockmarket.stock.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bank_stocks")
@Getter
@Setter
public class BankStockEntity {

    @Id
    private String name;

    @Column(nullable = false)
    private int quantity;
}
