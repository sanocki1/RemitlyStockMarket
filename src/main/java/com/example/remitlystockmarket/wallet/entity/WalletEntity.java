package com.example.remitlystockmarket.wallet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "wallets")
@Getter
@Setter
public class WalletEntity {

    @Id
    private Long id;
}
