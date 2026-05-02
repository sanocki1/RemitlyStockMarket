package com.example.remitlystockmarket.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Getter
@Setter
@NoArgsConstructor
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String wallet_id;

    @Column(nullable = false)
    private String stock_name;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public LogEntity(String type, String wallet_id, String stock_name) {
        this.type = type;
        this.wallet_id = wallet_id;
        this.stock_name = stock_name;
        this.timestamp = LocalDateTime.now();
    }
}