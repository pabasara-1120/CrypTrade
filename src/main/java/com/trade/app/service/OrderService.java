package com.trade.app.service;

import com.trade.app.entity.Coin;
import com.trade.app.entity.Order;
import com.trade.app.entity.OrderItem;
import com.trade.app.entity.User;
import com.trade.app.enums.OrderType;

import java.util.List;

public interface OrderService {

    Order findOrderById(Long orderId) throws Exception;
    Order createOrder(User user, OrderItem orderItem, OrderType orderType);
    List<Order> getALlOrdersByUser(Long userId,OrderType orderType,String asset);
    Order processOrder(Coin coin, double quantity, OrderType orderType,User user) throws Exception;
    OrderItem createOrderItem(Coin coin,double quantity,double buyingPrice,double sellingPrice);
}
