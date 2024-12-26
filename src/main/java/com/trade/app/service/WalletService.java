package com.trade.app.service;

import com.trade.app.entity.Order;
import com.trade.app.entity.User;
import com.trade.app.entity.Wallet;

public interface WalletService {
    Wallet getUserWallet (User user);
    Wallet addBalance(Wallet wallet,Long money);
    Wallet findWalletById(Long id) throws Exception;
    Wallet transfer(User sender, Wallet receivingWallet, Long amount) throws Exception;
    Wallet payOrderPayment(Order order,User user) throws Exception;
}
