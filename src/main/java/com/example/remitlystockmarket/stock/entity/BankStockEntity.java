package com.example.remitlystockmarket.stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bank_stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankStockEntity {

    @Id
    private String name;

    @Column(nullable = false)
    private int quantity;
}
