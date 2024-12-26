package com.trade.app.entity;

import com.trade.app.enums.WalletTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Wallet wallet;

    private WalletTransactionType transactionType;

    private Long amount;

    private String purpose;

    private LocalDate date;

}
