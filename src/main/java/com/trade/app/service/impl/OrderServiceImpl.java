package com.trade.app.service.impl;

import com.trade.app.entity.*;
import com.trade.app.enums.OrderStatus;
import com.trade.app.enums.OrderType;
import com.trade.app.repository.OrderItemRepository;
import com.trade.app.repository.OrderRepository;
import com.trade.app.repository.WalletRepository;
import com.trade.app.service.AssetService;
import com.trade.app.service.OrderService;
import com.trade.app.service.WalletService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private AssetService assetService;

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()){
            return order.get();
        }
        else{
            throw new Exception("Order not found");
        }

    }

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();
        Order order = Order.builder()
                .orderType(orderType)
                .user(user)
                .price(BigDecimal.valueOf(price))
                .status(OrderStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .build();
        return orderRepository.save(order);

    }


    @Override
    public List<Order> getALlOrdersByUser(Long userId, OrderType orderType, String asset) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {
        if(orderType.equals(OrderType.SELL)){
            return sellAsset(coin,quantity,user);
        }
        else if(orderType.equals(OrderType.BUY)){
            return buyAsset(coin,quantity,user);
        }
        throw new Exception("Invalid order type");
    }

    @Override
    public OrderItem createOrderItem(Coin coin, double quantity, double buyingPrice, double sellingPrice) {
        OrderItem orderItem = OrderItem.builder()
                .buyingPrice(buyingPrice)
                .quantity(quantity)
                .sellingPrice(sellingPrice)
                .coin(coin)
                .build();
        return orderItemRepository.save(orderItem);


    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, User user) throws Exception {
        if(quantity<=0){
            throw new Exception("Quantity should be positive");
        }
        double buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin,quantity,buyPrice,0);
        Order order = createOrder(user,orderItem,OrderType.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(order,user);

        order.setStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepository.save(order);


        //create asset
        Asset oldAsset = assetService.findAssetByUserAndCoinId(order.getUser().getId(),order.getOrderItem().getCoin().getId());
        if(oldAsset==null){
            assetService.createAsset(user,orderItem.getCoin(), orderItem.getQuantity());
        }else {
            assetService.updateAsset(oldAsset.getId(), quantity);
        }

        return savedOrder;

    }

    @Transactional
    public Order sellAsset(Coin coin, double quantity, User user) throws Exception {
        if(quantity<=0){
            throw new Exception("Quantity should be positive");
        }
        double sellPrice = coin.getCurrentPrice();
        Asset asset = assetService.findAssetByUserAndCoinId(user.getId(), coin.getId());
        double buyingPrice = asset.getBuyingPrice();
        if(asset!=null){
            OrderItem orderItem = createOrderItem(coin,quantity,buyingPrice, sellPrice);
        }


        OrderItem orderItem = createOrderItem(coin,quantity,buyingPrice,sellPrice);
        Order order = createOrder(user,orderItem,OrderType.SELL);
        orderItem.setOrder(order);

        if(asset.getQuantity()>=quantity){

            order.setStatus(OrderStatus.SUCCESS);
            order.setOrderType(OrderType.SELL);
            Order savedOrder = orderRepository.save(order);
            walletService.payOrderPayment(order,user);

            Asset updatedAsset = assetService.updateAsset(asset.getId(),-quantity);
            if(updatedAsset.getQuantity()*coin.getCurrentPrice()<=1){
                assetService.deleteAsset(updatedAsset.getId());
            }
            return savedOrder;

        }
        else {
            throw new Exception("Insufficient quantity to sell");
        }


        //create asset


    }



}
