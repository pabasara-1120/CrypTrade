package com.trade.app.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double quantity;
    private double buyingPrice;

    @ManyToOne
    private Coin coin;

    @ManyToOne
    private User user;
}
