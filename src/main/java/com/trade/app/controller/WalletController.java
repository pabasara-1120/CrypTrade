package com.trade.app.controller;

import com.trade.app.entity.Order;
import com.trade.app.entity.User;
import com.trade.app.entity.Wallet;
import com.trade.app.entity.WalletTransaction;
import com.trade.app.service.OrderService;
import com.trade.app.service.UserService;
import com.trade.app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/get")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader("Authorization") String jwt, @PathVariable Long walletId,
                                                         @RequestBody WalletTransaction transaction) throws Exception {

        User sender = userService.findUserProfileByJwt(jwt);
        Wallet recieverWallet = walletService.findWalletById(walletId);
        Wallet wallet = walletService.transfer(sender,recieverWallet,transaction.getAmount());
        return ResponseEntity.ok(wallet);

    }

    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> orderPayment(@RequestHeader("Authorization") String jwt,@PathVariable Long orderId) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.findOrderById(orderId);
        Wallet wallet = walletService.payOrderPayment(order,user);
        return ResponseEntity.ok(wallet);
    }

}
