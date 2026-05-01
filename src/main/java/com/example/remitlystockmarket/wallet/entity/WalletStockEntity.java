package com.example.remitlystockmarket.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "wallet_stocks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "wallet_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletStockEntity {

    public WalletStockEntity(String name, int quantity, WalletEntity wallet) {
        this.name = name;
        this.quantity = quantity;
        this.wallet = wallet;
    }

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
