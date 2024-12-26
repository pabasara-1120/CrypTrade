package com.trade.app.service.impl;

import com.trade.app.entity.Order;
import com.trade.app.entity.User;
import com.trade.app.entity.Wallet;
import com.trade.app.enums.OrderType;
import com.trade.app.repository.WalletRepository;
import com.trade.app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if(wallet==null){
            Wallet newWallet = Wallet.builder()
                    .user(user)
                    .balance(BigDecimal.valueOf(0))
                    .build();
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long amount) {
        BigDecimal newBalance;
        if(amount>=0){
            newBalance = wallet.getBalance().add(BigDecimal.valueOf(amount));

        }
        else{
            newBalance = wallet.getBalance().subtract(BigDecimal.valueOf(amount));

        }
        wallet.setBalance(newBalance);
        return wallet;
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if(wallet.isPresent()){
            return wallet.get();
        }
        throw new Exception("Wallet not found");
    }

    @Override
    public Wallet transfer(User sender, Wallet receivingWallet, Long amount) throws Exception {
        Wallet sendingWallet = getUserWallet(sender);
        if(sendingWallet.getBalance().equals(BigDecimal.valueOf(0))){
            throw new Exception("Insufficient Account Balance");
        }
        sendingWallet = addBalance(sendingWallet,-amount);
        walletRepository.save(sendingWallet);
        receivingWallet = addBalance(receivingWallet,amount);
        walletRepository.save(receivingWallet);

        return sendingWallet;


    }

    @Override
    public Wallet payOrderPayment(Order order,User user) throws Exception {
        Wallet wallet = getUserWallet(user);

        if(order.getOrderType().equals(OrderType.BUY)){

            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
            if(newBalance.compareTo(order.getPrice())<0){
                throw new Exception("Insufficient Account Balance");
            }
            wallet.setBalance(newBalance);

        } else if (order.getOrderType().equals(OrderType.SELL)) {

            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);


        }
        walletRepository.save(wallet);
        return wallet;

    }
}
