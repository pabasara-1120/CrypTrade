package com.trade.app.request;

import com.trade.app.enums.OrderType;
import lombok.Data;

@Data
public class OrderRequest {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}
