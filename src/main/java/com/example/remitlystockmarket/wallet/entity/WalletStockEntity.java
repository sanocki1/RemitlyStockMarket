package com.example.remitlystockmarket.wallet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "wallet_stocks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "wallet_id"}))
@Getter
@Setter
public class WalletStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // using this as PK for simplicity, might make a composite key out of name and wallet_id

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private WalletEntity wallet;
}
